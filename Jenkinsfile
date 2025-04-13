pipeline {
    agent any

    environment {
        REGISTRY = '192.168.33.10:8083' // Your Nexus Docker registry
        IMAGE_NAME = '4twin3-gestion-station-ski'
        IMAGE_TAG = 'latest'
        FULL_IMAGE = "${REGISTRY}/youssefbelhadj/${IMAGE_NAME}:${IMAGE_TAG}"
        DOCKER_USER = 'admin'
        DOCKER_PASS = 'nexus'
    }

    stages {

        // 1. Checkout Code
        stage('Checkout') {
            steps {
                git branch: 'feat/youssef',
                    url: 'https://github.com/YassineEssid/ProjetDevops.git'
            }
        }

        // 2. Build Docker Image
        stage('Build Docker Image') {
            steps {
                script {
                    echo "Checking Docker version..."
                    sh 'docker --version'

                    echo "Building Docker image..."
                    sh "docker build -t ${FULL_IMAGE} ."
                }
            }
        }

        // 3. Push to Nexus Docker Registry
        stage('Push to Nexus') {
            steps {
                script {
                    echo "Logging into Nexus registry..."
                    sh "echo ${DOCKER_PASS} | docker login http://${REGISTRY} -u ${DOCKER_USER} --password-stdin"

                    echo "Pushing Docker image to Nexus..."
                    sh "docker push ${FULL_IMAGE}"

                    echo "Logout from Docker registry"
                    sh 'docker logout'
                }
            }
        }
    }
}
