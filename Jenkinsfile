pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski'
      registryCredentials = "nexus-creds"
        registry = "192.168.33.10:8083/repository/docker-hosted/"
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
          stage('Push to Nexus') {
                  steps {
                      script {
                          docker.withRegistry("http://${registry}", registryCredentials) {
                              sh "docker push --quiet $registry/$DOCKER_IMAGE"
                          }
                      }
                  }
              }


}

}
