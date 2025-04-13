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
             stage("Run Prometheus") {
                      steps {
                          script {
                              sh 'docker start prometheus || docker run -d --name prometheus -p 9090:9090 -v ${WORKSPACE}/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus'
                          }
                      }
                  }

                  stage("Run Grafana") {
                      steps {
                          script {
                              sh 'docker start grafana || docker run -d --name grafana -p 3000:3000 grafana/grafana'
                          }
                      }
                  }

        stage('Automated Test') {
            steps {
                script {
                    // Add an element via POST request
                    echo "Adding a new skier..."
                    def postResponse = sh(script: '''
                        curl -X POST http://192.168.33.10:8089/api/skier/add -H "Content-Type: application/json" -d '{
                                                                                                                       "firstName": "Youssef",
                                                                                                                       "lastName": "Ben Ali",
                                                                                                                       "dateOfBirth": "1998-07-20",
                                                                                                                       "city": "Tunis",
                                                                                                                       "subscription": {
                                                                                                                         "startDate": "2025-04-01",
                                                                                                                         "typeSub": "ANNUAL"  // replace with your enum value if needed
                                                                                                                       }

                                                                                                                     }
                    ''', returnStdout: true).trim()

                    echo "POST Response: ${postResponse}"

                    // Retrieve the added element via GET request
                    echo "Retrieving the added element..."
                    def getResponse = sh(script: '''
                        curl http://192.168.33.10:8089/api/skier/get/1
                    ''', returnStdout: true).trim()

                    echo "GET Response: ${getResponse}"

                    // Simple check to confirm the element was added
                    if (!postResponse.contains("success") || !getResponse.contains("New Element")) {
                        error "Automated test failed: Element not added correctly!"
                    }
                }
            }
        }


}

}
