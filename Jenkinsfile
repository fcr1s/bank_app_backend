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
                dir('bank_app_backend/src/test/java/com/cris/bank_app_backend/services ') {
                    bat 'mvn test'
                }
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
                    withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')]) {
                        bat 'docker login -u fcr1s -p %dhpsw%'
                    }
                    bat 'docker push fcr1s/bank_app_backend:latest'
                }
            }
        }
    }
}
