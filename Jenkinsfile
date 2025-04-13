pipeline {
    agent any

    environment {
        SONAR_PROJECT_KEY = 'ProjetDevops'
        SONAR_PROJECT_NAME = 'ProjetDevops'
        SONAR_XML_REPORT_PATH = 'target/site/jacoco/jacoco.xml' // à adapter si nécessaire
    }

    stages {
        stage('Build') {
            steps {
                dir('ProjetDevops') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Run Tests') {
            steps {
                dir('ProjetDevops') {
                    sh 'mvn test jacoco:report'
                }
            }
            post {
                always {
                    junit 'ProjetDevops/target/surefire-reports/*.xml'
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
                                        -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                                        -Dsonar.projectName=$SONAR_PROJECT_NAME \
                                        -Dsonar.coverage.jacoco.xmlReportPaths=$SONAR_XML_REPORT_PATH \
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