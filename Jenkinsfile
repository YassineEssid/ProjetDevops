pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski'
             IMAGE_TAG = 'latest'
             REGISTRY_URL = 'nexus.example.com:8083' // Replace with your Nexus Docker registry
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
                           dockerImage = docker.build("${REGISTRY_URL}/${DOCKER_IMAGE}:${IMAGE_TAG}")
                       }
                   }
               }

               stage('Push to Nexus') {
                   steps {
                       script {
                           docker.withRegistry("https://${REGISTRY_URL}", "${DOCKER_CREDS}") {
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
