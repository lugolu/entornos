pipeline {
    agent {
        docker {
            image 'node:12.18.2-alpine' 
            args '-p 3000:3000' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh './RecrearContainer.sh' 
            }
        }
    }
}