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
        // 2. Run unit tests (Mockito & JUnit)
                stage('Build & Test') {
                    steps {
                      script {
                          // Start MySQL with Docker Compose
                          sh 'docker compose -f docker-compose.yml up -d mysqldb'

                          // Run tests
                          sh 'mvn clean test -Dspring.profiles.active=test'

                      }

                    }
                }

                // 3. SonarQube Analysis
                stage('SonarQube Analysis') {
                    agent any
                    steps {
                        // Set SonarQube environment variables properly
                        withSonarQubeEnv('SonarQube') {
                            sh 'mvn clean package sonar:sonar'
                        }
                    }
                }

                    // 4. Deploy Maven artifact to Nexus
                    stage('Deploy to Nexus') {
                        steps {
                            sh 'mvn deploy -DskipTests -s /usr/share/maven/conf/settings.xml'
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
           stage('DockerHub Login') {
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
                    echo "Waiting for the app to start..."
                    sleep 60
                    echo "Adding a new skier..."
                    def postResponse = sh(script: '''
                        curl -X POST http://192.168.33.10:8089/api/skier/add \
                        -H "Content-Type: application/json" \
                        -d @- <<EOF
        {
          "firstName": "Youssef",
          "lastName": "Ben Ali",
          "dateOfBirth": "1998-07-20",
          "city": "Tunis",
          "subscription": {
            "startDate": "2025-04-01",
            "typeSub": "ANNUAL"
          }
        }
        EOF
                    ''', returnStdout: true).trim()

                    echo "POST Response: ${postResponse}"

                    echo "Retrieving the added element..."
                    def getResponse = sh(script: '''
                        curl http://192.168.33.10:8089/api/skier/get/4
                    ''', returnStdout: true).trim()

                    echo "GET Response: ${getResponse}"

                    // Adjust the checks based on your actual API responses
                    if (!getResponse.contains("Youssef")) {
                        error "Automated test failed: Skier not found!"
                    }
                }
            }
        }



}

}
