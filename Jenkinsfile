pipeline {
    agent any

    environment {
        SONAR_HOST_URL = 'http://192.168.33.10:9000/'
        SONAR_TOKEN = credentials('sonar-creds') // Utilisez Jenkins Credentials pour le token SonarQube
        DOCKER_IMAGE = 'youssefbelhadj/4twin3-gestion-station-ski:latest'
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

                  // Stop MySQL after tests
                  sh 'docker compose -f docker-compose.yml down'
              }

            }
        }

        // 3. SonarQube Analysis
         stage('SonarQube Analysis') {
             steps {
                 // Run the SonarQube analysis using Maven and send the report to SonarQube server
                 withSonarQubeEnv(SONAR_HOST_URL) {
                     sh 'mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}'
                 }
             }
         }

            // 4. Deploy Maven artifact to Nexus
            stage('Deploy to Nexus') {
                steps {
                    sh 'mvn deploy -DskipTests'
                }
            }

        // 5. Build Docker Image
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}")
                }
            }
        }

        // 6. Push Docker Image Nexus
        stage('Push Docker Image to Nexus') {
            steps {
                script {
                    docker.withRegistry('http://192.168.33.10:8081', 'nexus-creds') {
                        docker.image("${DOCKER_IMAGE}").push("latest")
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
