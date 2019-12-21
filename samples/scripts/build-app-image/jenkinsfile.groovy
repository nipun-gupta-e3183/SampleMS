node('fd-jenkins-slave-default') {
    def appImage

    stage('Checkout') {
        sh "rm -rf $WORKSPACE/*"
        sh "rm -rf .git"
        checkout scm
        sh "git config user.email runwayci@freshworks.com"
        sh "git config user.name runway-ci"
    }

    dir('samples') { //Note: Delete this for actual projects
        stage('Maven::UpdateVersion') {
            docker.image('maven:3-jdk-11').inside {
                env.OLD_APP_VERSION = sh(returnStdout: true, script: "export MAVEN_CONFIG=''; ./scripts/build-app-image/get_version.sh").trim()
            }
            String[] parts = env.OLD_APP_VERSION.split("\\.")
            switch (env.ReleaseType) {
                case "Major":
                    parts[0] = String.valueOf(Integer.parseInt(parts[0]) + 1)
                    parts[1] = '0'
                    parts[2] = '0'
                    break
                case "Minor":
                    parts[1] = String.valueOf(Integer.parseInt(parts[1]) + 1)
                    parts[2] = '0'
                    break
                case "Patch":
                    parts[2] = String.valueOf(Integer.parseInt(parts[2]) + 1)
                    break
                default:
                    echo("ReleaseType param not specified. Not changing the version.")
            }
            env.APP_VERSION = String.join(".", parts)
            docker.image('maven:3-jdk-11').inside {
                sh("export MAVEN_CONFIG=''; ./scripts/build-app-image/update_version.sh ${APP_VERSION}")
            }
        }

        // TODO: Run tests & other checks

        stage('Image::Build') {
            env.DOCKER_TAG = "${env.DOCKER_IMAGE_URL}:${env.APP_VERSION}"
            appImage = docker.build(env.DOCKER_TAG, ".")
        }

        stage('Image::Push') {
            sh("./scripts/build-app-image/ecr_login.sh")
            appImage.push()
        }

        stage('Git::Commit') {
            withCredentials([usernamePassword(credentialsId: env.Git_Credentials_Id, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                sh('git commit -am"Bumping version to v${APP_VERSION}"')
                sh('git tag -a v${APP_VERSION} -m"Release v${APP_VERSION}"')
                sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/freshdesk/freshworks-boot-samples.git HEAD:master')
                sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/freshdesk/freshworks-boot-samples.git HEAD:master --tags')
            }
        }
    }
}