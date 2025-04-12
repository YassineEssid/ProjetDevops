pipeline {
    agent any

    environment {
        SONAR_PROJECT_KEY = 'DevopsYoussef'
        SONAR_PROJECT_NAME = 'DevopsYoussef'
        SONAR_XML_REPORT_PATH = 'target/site/jacoco/jacoco.xml'
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
                sh 'mvn clean test -Dspring.profiles.active=test'
            }
        }

        // 3. SonarQube Analysis
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        mvn sonar:sonar \\
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \\
                        -Dsonar.projectName=${SONAR_PROJECT_NAME} \\
                        -Dsonar.coverage.jacoco.xmlReportPaths=${SONAR_XML_REPORT_PATH} \\
                        -Dsonar.java.coveragePlugin=jacoco
                    """
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
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
            }
        }

    }
}
