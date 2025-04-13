pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski:'
        DOCKER_USERNAME = 'admin' // Jenkins credentials for Docker username
        DOCKER_PASSWORD = 'nexus'  // Jenkins credentials for Docker password
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
        stage('Authenticate to Nexus Docker Registry') {
            steps {
                script {
                    // Login to Nexus Docker registry
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin http://192.168.33.10:8081'
                }
            }
        }


}

}
