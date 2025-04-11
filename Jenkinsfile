pipeline {
    agent any

    stages {
        stage('Install dependencies') {
            steps {
                sh '/usr/bin/npm install' // ← remplace ce chemin selon ton système
            }
        }

        stage('Unit Test') {
            steps {
                sh '/usr/bin/npm test'
            }
        }

        stage('Build application') {
            steps {
                sh '/usr/bin/npm run build-dev'
            }
        }
    }
}
