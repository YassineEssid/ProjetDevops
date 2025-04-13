pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski'
      registryCredentials = "nexus-creds"
        registry = "192.168.33.10:8081/repository/docker-hosted/"
                DOCKERHUB_CREDENTIALS = credentials('docker-creds')
                        DOCKER_USERNAME = "youssefbelhadj"


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
           stage('Docker Login') {
                       steps {
                           script {
                               sh """
                                   echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin
                               """
                           }
                       }
                   }
          stage('Push to Docker Hub') {
                     steps {
                         sh """
                             docker push ${DOCKER_IMAGE}
                             docker push ${DOCKER_IMAGE}:latest
                         """
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
