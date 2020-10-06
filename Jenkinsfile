pipeline {
    agent any
    stages {
        withEnv(['PATH']) {
            stage('Prepare') { 
                steps {
                    sh "yarn -v"
                }
            }
            stage('Build') { 
                steps {
                    sh "pwd"
                    sh "chmod +x -R RecrearContainer.sh"
                    sh "./RecrearContainer.sh"
                }
            }
        }
    }
}