pipeline {
    agent any
    tools {
        maven 'maven_3_9_8' 
    }

    stages {
        stage('Checkout Backend') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/fcr1s/bank_app_backend']])
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
                    withCredentials([string(credentialsId: 'docker_hub_token', variable: 'DOCKER_TOKEN')]) {
                        bat 'docker login -u fcr1s -p %DOCKER_TOKEN%'
                    }
                    bat 'docker push fcr1s/bank_app_backend:latest'
                }
            }
        }
    }
}
