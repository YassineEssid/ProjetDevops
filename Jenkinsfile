pipeline {
    agent any

    environment {
        imageName = 'youssefbelhadj/4twin3-gestion-station-ski'
             registry= '192.168.33.10/:8083' // Replace with your Nexus Docker registry
             registryCredentials = 'nexus-creds'
             dockerImage="
    }

    stages {

        // 1. Checkout from GitHub
        stage('Checkout') {
            steps {
                git branch: 'feat/youssef',
                    url: 'https://github.com/YassineEssid/ProjetDevops.git'
            }
        }


       stage('Build Docker Image') {
                   steps {
                       script {
                           dockerImage = docker.build imageName
                       }
                   }
               }

               stage('Push to Nexus') {
                   steps {
                       script {
                           docker.withRegistry("http//"+registry, registryCredentials) {
                               dockerImage.push('latest')
                           }
                       }
                   }
               }

        stage('Run Docker Compose') {
            steps {
                script {
                    sh 'docker compose -f docker-compose.yml up -d'
                }
            }
        }

    }
}
