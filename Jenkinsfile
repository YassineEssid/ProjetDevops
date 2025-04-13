pipeline {
    agent any

    environment {
/*         registryCredentials = "nexus"
        registry = "172.17.0.4:8082"
        imageName = "helmisubscription"
        imageTag = "6.0-SNAPSHOT-${env.BUILD_NUMBER}" */
        IMAGE_NAME = "${DOCKER_USERNAME}/backend"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
        DOCKER_USERNAME = "helmigargouri"
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
                     echo "Using SonarQube URL: ${SONAR_URL}"
                     def scannerHome = tool 'SonarScan'
                     withSonarQubeEnv {
                         sh """
                             ${scannerHome}/bin/sonar-scanner \
                             -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                             -Dsonar.projectName=${SONAR_PROJECT_NAME} \
                             -Dsonar.sources=src \
                             -Dsonar.java.binaries=target/classes \
                             -Dsonar.sourceEncoding=UTF-8 \
                             -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                             -Dsonar.login=${SONAR_TOKEN} \
                             -Dsonar.scanner.force-deprecated-java-version=true
                         """
                     }
                 }
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

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build -t ${IMAGE_NAME}:${VERSION}
                    docker tag ${IMAGE_NAME}:${VERSION} ${IMAGE_NAME}:latest
                """
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



/*
        stage('Run Application') {
            steps {
                script {
                    docker.withRegistry("http://${registry}", "${registryCredentials}") {
                        sh "docker pull ${registry}/${imageName}:${imageTag}"

                        // Replace old image reference with the current one
                        sh "sed -i 's|192.168.1.100:8083/kenzabenslimane_4twin3_thunder_gestionski:IMAGE_TAG|${registry}/${imageName}:${imageTag}|g' docker-compose.yml"
                        sh "cat docker-compose.yml"

                        // Stop and remove existing containers
                        sh "docker-compose down --remove-orphans"

                        // Run the container with updated image tag
                        withEnv(["IMAGE_TAG=${imageTag}"]) {
                            sh "docker-compose up -d"
                        }
                    }
                }
            }
        }*/

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

/*pipeline {
    agent any

    stages {

        stage('Build') {
           steps {
               dir('ProjetDevops') {  // Exécute la commande Maven dans le bon dossier
                   sh 'mvn clean compile'
               }
           }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    junit '**//* target/surefire-reports *//*.xml'
                }
            }
        }


        stage('SonarQube Analysis') {
            steps {
                dir('ProjetDevops') {
                    script {
                        withSonarQubeEnv('sonarqube') {
                            withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
                                docker.image('maven:3.8.6-openjdk-17').inside {
                                    sh """
                                        mvn sonar:sonar \
                                        -Dsonar.token=$SONAR_TOKEN \
                                        -Dsonar.projectKey=ProjetDevops \
                                        -Dsonar.projectName=ProjetDevops \
                                        -Dsonar.coverage.jacoco.xmlReportPaths=${SONAR_XML_REPORT_PATH} \
                                        -Dsonar.java.coveragePlugin=jacoco
                                    """
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                dir('ProjetDevops') {
                    script {
                        docker.image('maven:3.8.6-openjdk-17').inside {
                            sh 'mvn deploy -DskipTests'
                        }
                    }
                }
            }
        }
    }
}
*/