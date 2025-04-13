pipeline {
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
               dir('ProjetDevops') {
                   sh 'mvn test -Dspring.profiles.active=test'
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
