def sonarqube_analysis() {
    stage('SonarQube Analysis') {
        withCredentials([string(credentialsId: 'SonarqubeGithubToken', variable: 'SonarqubeGithubToken')]) {
            withSonarQubeEnv('FreshdeskSonarqube') {

                if (env.ghprbPullId) {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=${env.sonarProjectKey} \
                    -Dsonar.github.repository=${env.sonarRepository} \
                    -Dsonar.pullrequest.key=${env.ghprbPullId} \
                    -Dsonar.pullrequest.branch=${env.ghprbSourceBranch} \
                    -Dsonar.pullrequest.base=${env.ghprbTargetBranch} \
                    -Dsonar.github.oauth=${SonarqubeGithubToken}
                   """
                } else {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.projectKey=${env.sonarProjectKey} \
                    -Dsonar.github.repository=${env.sonarRepository} \
                    -Dsonar.github.oauth=${SonarqubeGithubToken}
                   """
                }
            }
        }
    }
}


node('fd-jenkins-slave-default') {
    stage('Checkout') {
        sh "rm -rf $WORKSPACE/*"
        sh "rm -rf .git"
        checkout scm
        sh "git config user.email runwayci@freshworks.com"
        sh "git config user.name runway-ci"
    }

    dir('samples') { //Note: Delete this for actual projects
        stage('Build') {
            docker.image('maven:3-jdk-11').inside {
                sh("export MAVEN_CONFIG=''; cp mvnsettings.xml /root/.m2/settings.xml && ./mvnw clean install")
                sonarqube_analysis()
            }
        }
        post {
            always {
                junit '**/target/surefire-reports/**/*.xml'
            }
        }
    }
}
