pipeline {
    agent any
    stages {
        stage('Build') { 
            steps {
                sh "pwd"
                sh "chmod +x -R RecrearContainer.sh"
                sh "./RecrearContainer.sh"
            }
        }
    }
}