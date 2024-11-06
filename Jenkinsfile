pipeline {
    agent any
    tools {
        maven 'maven_3_9_8' 
    }

    stages {
        stage('Checkout Backend') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/fcr1s/bank_app_backend']])
            }
        }

        stage('Build Backend') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Unit Tests Backend') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Build Docker Image for Backend') {
            steps {
                bat 'docker build -t fcr1s/bank_app_backend:latest .'
            }
        }

        stage('Push Backend Docker Image to Docker Hub') {
            steps {
                script {
                    // Aquí hacemos referencia al ID de las credenciales que creaste
                    withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')])
                    bat 'docker login -u fcr1s -p %dhpsw'
                    
                    bat 'docker push fcr1s/bank_app_backend:latest'
                }
            }
        }
    }
}
