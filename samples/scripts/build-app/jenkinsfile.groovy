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
            }
        }
        post {
            always {
                junit '**/target/surefire-reports/**/*.xml'
            }
        }
    }
}