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
                    -Dsonar.github.oauth=${SonarqubeGithubToken} \
                    -Dsonar.branch.name=${env.BRANCH_NAME}
                   """
                }
            }
        }
    }
}

def checkout_files(){
    stage('Checkout') {
        sh "rm -rf $WORKSPACE/*"
        sh "rm -rf .git"
        scmVars = checkout scm
        env.BRANCH_NAME = scmVars.GIT_BRANCH.split('/')[1]
        sh "git config user.email runwayci@freshworks.com"
        sh "git config user.name runway-ci"
    } 
}

def checkout_with_merge(){
    stage('Checkout') {
        checkout([
                $class: 'GitSCM',
                branches: scm.branches,
                doGenerateSubmoduleConfigurations: false,
                userRemoteConfigs: scm.userRemoteConfigs,
                extensions: [[$class: 'CleanCheckout']],

        ])
    }

}

def build() {
    stage('Build & Test') {
        docker.image('maven:3-jdk-11').inside {
            sh("export MAVEN_CONFIG=''; cp mvnsettings.xml /root/.m2/settings.xml && ./mvnw clean install")
            sonarqube_analysis()
            junit '**/target/surefire-reports/**/*.xml'
            step( [ $class: 'JacocoPublisher' ] )
        }
    } 
}

def quality_gate(){
    // No need to occupy a node
    stage("Quality Gate"){
        timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
            def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
            if (qg.status != 'OK') {
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }
}

def run_ci_checks_for_master_commit(){

    node(env.slaveLabel) {

        checkout_files()

        dir('samples') { //Note: Delete this for actual projects
            build()
        }
    }

    // Run quality gate in the master node.
    quality_gate()
}


def run_ci_checks_for_pull_request(){
    node(env.slaveLabel) {

        checkout_with_merge()

        dir('samples') { //Note: Delete this for actual projects
            build()
        }
    }

    // Run quality gate in the master node.
    quality_gate()
}

echo "Hello world"

if(env.ghprbPullId){
    run_ci_checks_for_pull_request()
} else {
    run_ci_checks_for_master_commit()
}
