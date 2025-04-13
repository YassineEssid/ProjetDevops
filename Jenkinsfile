pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski'
             IMAGE_TAG = 'latest'
             REGISTRY_URL = '192.168.33.10/:8083' // Replace with your Nexus Docker registry
             DOCKER_CREDS = 'nexus-creds'
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
                           dockerImage = docker.build youssefbelhadj/4twin3-gestion-station-ski
                       }
                   }
               }

               stage('Push to Nexus') {
                   steps {
                       script {
                           docker.withRegistry("http//${REGISTRY_URL}", "${DOCKER_CREDS}") {
                               dockerImage.push()
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
