pipeline {
    agent any
    tools {
        maven 'maven_3_9_8' // Usar la versión de Maven 3.9.8
    }
    environment {
        DOCKER_CREDENTIALS_ID = 'dockerHubCredentials' // ID de las credenciales en Jenkins para Docker Hub
        DOCKER_USER = 'fcr1s' // Tu usuario de Docker Hub
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
                    withCredentials([string(credentialsId: DOCKER_CREDENTIALS_ID, variable: 'DOCKER_PASSWORD')]) {
                        bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASSWORD%'
                    }
                    bat 'docker push fcr1s/bank_app_backend:latest'
                }
            }
        }
    }
}
