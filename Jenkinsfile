pipeline {
    agent {
        docker {
            image 'maven:3.8.6-openjdk-17'
        }
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
                        withCredentials([string(credentialsId: 'jenkins-sonar', variable: 'SONAR_TOKEN')]) {
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

        stage('Deploy to Nexus') {
            steps {
                dir('ProjetDevops') {
                    sh 'mvn deploy -DskipTests'
                }
            }
        }
    }
}
