pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski'
        NEXUS_URL = "192.168.33.10:8083" // adapte le port selon ton Nexus
        NEXUS_REPO = "docker-hosted"     // adapte si le nom du repo est différent
        NEXUS_CREDENTIALS_ID = "nexus-creds" // Jenkins Credentials ID
    }

    stages {

        // 1. Checkout from GitHub
        stage('Checkout') {
            steps {
                git branch: 'feat/youssef',
                    url: 'https://github.com/YassineEssid/ProjetDevops.git'
            }
        }


            // 5. Build Docker Image
            stage('Build Docker Image') {
                steps {
                   script {
                        echo "Checking Docker version..."
                        sh 'docker --version'
                        echo "Checking Docker images..."
                        sh 'docker images'
                        echo "Building Docker image..."
                        sh 'docker build -t youssefbelhadj/4twin3-gestion-station-ski .'                }
                }
        }

        // 6. Push Docker Image Nexus
        stage('Push Docker Image to Nexus') {
           steps {
                sh "docker push ${NEXUS_URL}/docker-hosted/${IMAGE_NAME}"
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
