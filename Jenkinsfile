pipeline {
    agent any
    stages {
        stage('Prepare') { 
            steps {
                sh "pwd"
                sh "printenv"
                sh "env"
                sh "$PATH"
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