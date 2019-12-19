node('fd-jenkins-slave-default') {
    def appImage

    stage('Checkout') {
        sh "rm -rf $WORKSPACE/*"
        sh "rm -rf .git"
        checkout scm
        sh "git config user.email runwayci@freshworks.com"
        sh "git config user.name runway-ci"
    }

    stage('Tag Version') {
        docker.image('maven:3-jdk-11').inside {
            env.OLD_APP_VERSION = sh(returnStdout: true, script: "mvn help:evaluate -Dchangelist= -Dexpression=project.version -q -DforceStdout").trim()
        }
        String[] parts = env.OLD_APP_VERSION.split("\\.")
        switch (env.Release_Version) {
            case "major":
                parts[0] = String.valueOf(Integer.parseInt(parts[0]) + 1)
                parts[1] = '0'
                parts[2] = '0'
                break
            case "minor":
                parts[1] = String.valueOf(Integer.parseInt(parts[1]) + 1)
                parts[2] = '0'
                break
            case "patch":
                parts[2] = String.valueOf(Integer.parseInt(parts[2]) + 1)
                break
        }
        env.APP_VERSION = String.join(".", parts)
        docker.image('maven:3-jdk-11').inside {
            sh("./mvnw versions:set-property -Dproperty=revision -DnewVersion=${APP_VERSION}")
        }
    }

    stage('Image::Build') {
        env.DOCKER_TAG = "${env.DOCKER_IMAGE_URL}:${env.APP_VERSION}"
        appImage = docker.build(env.DOCKER_TAG, ".")
    }

    stage('Image::Push') {
        sh "aws ecr get-login --region us-east-1 --no-include-email --registry-ids ${env.DOCKER_IMAGE_AWS_ACCOUNT_ID}"
        appImage.push()
    }

    stage('Git Commit') {
        withCredentials([usernamePassword(credentialsId: env.Git_Credentials_Id, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
            sh('git commit -m"Bumping version to v${APP_VERSION}"')
            sh('git tag -a v${APP_VERSION} -m"Release v${APP_VERSION}"')
            sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/freshdesk/freshworks-boot-samples.git HEAD:master')
            sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/freshdesk/freshworks-boot-samples.git HEAD:master --tags')
        }
    }

}