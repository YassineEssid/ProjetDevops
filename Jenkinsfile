pipeline {
    agent any

    environment {
        SONAR_PROJECT_KEY = 'DevopsYoussef'
        SONAR_PROJECT_NAME = 'DevopsYoussef'
        SONAR_XML_REPORT_PATH = 'target/site/jacoco/jacoco.xml'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'feat/youssef',
                    url: 'https://github.com/YassineEssid/ProjetDevops.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -Dspring.profiles.active=test'
            }
        }

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

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }
    }
}
