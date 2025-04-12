pipeline {
    agent {
        docker {
            image 'maven:3.9.4-eclipse-temurin-17' // Image avec Maven + Java
            args '-v $HOME/.m2:/root/.m2' // Cache Maven partagé
        }
    }

    environment {
        SONAR_PROJECT_KEY = 'ProjetDevops'
        SONAR_PROJECT_NAME = 'ProjetDevops'
        SONAR_XML_REPORT_PATH = 'target/site/jacoco/jacoco.xml'
    }

    stages {

        stage('Git') {
            steps {
                dir('ProjetDevops') {
                    git branch: 'feat/subscription',
                        credentialsId: 'helmi123',
                        url: 'https://github.com/YassineEssid/ProjetDevops.git'
                }
            }
        }

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
                    sh 'mvn test -Dspring.profiles.active=test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('ProjetDevops') {
                    withSonarQubeEnv('sonarqube') {
                        withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_TOKEN')]) {
                            sh """
                                mvn sonar:sonar \\
                                -Dsonar.token=$SONAR_TOKEN \\
                                -Dsonar.projectKey=${SONAR_PROJECT_KEY} \\
                                -Dsonar.projectName=${SONAR_PROJECT_NAME} \\
                                -Dsonar.coverage.jacoco.xmlReportPaths=${SONAR_XML_REPORT_PATH} \\
                                -Dsonar.java.coveragePlugin=jacoco
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                dir('ProjetDevops') {
                    sh 'mvn deploy -DskipTests'
                }
            }
        }
    }
}
