pipeline {
    agent any

    environment {
        /* registryCredentials = "nexus"
        registry = "172.17.0.4:8082"
        imageName = "helmisubscription"
        imageTag = "6.0-SNAPSHOT-${env.BUILD_NUMBER}" */
        DOCKER_USERNAME = "helmigargouri"
        IMAGE_NAME = "${DOCKER_USERNAME}/backend"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        VERSION = "1.0.${BUILD_NUMBER}"
        gitBranch = "feat/subscription"
        gitRepo = "https://github.com/YassineEssid/ProjetDevops.git"

        // SonarQube
        SONAR_URL = "http://172.17.0.3:9000"
        SONAR_TOKEN = "squ_af142814424e203d67bb97741a4c3b47adc0cd50"
        SONAR_PROJECT_KEY = "subscription"
        SONAR_PROJECT_NAME = "subscription"
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    git branch: gitBranch, url: gitRepo
                    sh 'ls -l'
                }
            }
        }

        stage('Build') {
            steps {
                dir('ProjetDevops') {  // Exécute la commande Maven dans le bon dossier
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn verify -Dspring.profiles.active=test -T 1C'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    dir('ProjetDevops') {  // Exécute SonarQube dans le dossier du projet
                        echo "Using SonarQube URL: ${SONAR_URL}"
                        def scannerHome = tool 'SonarScan'
                        withSonarQubeEnv {
                            sh """
                                ${scannerHome}/bin/sonar-scanner \\
                                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \\
                                -Dsonar.projectName=${SONAR_PROJECT_NAME} \\
                                -Dsonar.sources=src \\
                                -Dsonar.java.binaries=target/classes \\
                                -Dsonar.sourceEncoding=UTF-8 \\
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \\
                                -Dsonar.login=${SONAR_TOKEN} \\
                                -Dsonar.scanner.force-deprecated-java-version=true
                            """
                        }
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('ProjetDevops') {  // Exécute le build Docker dans le dossier avec le Dockerfile
                    sh """
                        docker build -t ${IMAGE_NAME}:${VERSION} .
                        docker tag ${IMAGE_NAME}:${VERSION} ${IMAGE_NAME}:latest
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh """
                    docker push ${IMAGE_NAME}:${VERSION}
                    docker push ${IMAGE_NAME}:latest
                """
            }
        }

        stage('Run Application') {
            steps {
                dir('ProjetDevops') {  // Exécute docker-compose dans le dossier contenant docker-compose.yml
                    script {
                        sh """
                            export IMAGE_TAG=${VERSION}
                            docker-compose pull || true
                            docker-compose up -d
                        """
                    }
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
    }

    post {
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed! Check the logs."
        }
    }
}