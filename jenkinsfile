pipeline {
    agent any

    tools {
        maven 'Maven' // Vérifier le nom dans "Global Tool Configuration"
    }

    environment {
        SONAR_SCANNER = tool 'SonarQubeScanner'
        // Configurer le token dans les credentials Jenkins (ex: SONAR_TOKEN)
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'feat/helmi',
                     url: 'https://github.com/YassineEssid/ProjetDevops.git'
            }
        }

        stage('Build & Test') { // Combine build et tests
            steps {
                sh 'mvn clean verify' // Exécute compile + test + package
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    // Récupérer le token depuis les credentials Jenkins
                    withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                        sh """
                            ${SONAR_SCANNER}/bin/sonar-scanner \
                            -Dsonar.projectKey=devopsSecure \
                            -Dsonar.sources=. \
                            -Dsonar.host.url=http://sonar.example.com \ // Remplacez par votre URL
                            -Dsonar.login=${SONAR_TOKEN}
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // Supprimer l'étape Package redondante
    }
}