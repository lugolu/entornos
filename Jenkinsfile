pipeline {
    agent any
    stages {
        stage('Prepare') { 
            steps {
                sh "pwd"
            }
        }
        stage('Build') { 
            steps {
                sh "chmod +x -R RecrearContainer.sh"
                sh "./RecrearContainer.sh"
            }
        }
    }
}