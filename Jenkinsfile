pipeline {
    agent any
    stages {
        stage('Prepare') { 
            steps {
                sh "npm install -g yarn"
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