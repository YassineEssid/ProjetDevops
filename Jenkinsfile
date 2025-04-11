pipeline {
    agent any

    environment {
        // Si nécessaire, adapte ce PATH pour que npm soit bien trouvé
        PATH = "/usr/local/bin:/usr/bin:/bin:$PATH"
    }

    stages {
        stage('Install dependencies') {
            steps {
                echo '🔧 Étape : Installation des dépendances...'
                script {
                    def status = sh(script: 'npm install', returnStatus: true)
                    if (status != 0) {
                        error("❌ npm install a échoué. Vérifie si Node.js est bien installé.")
                    }
                }
            }
        }

        stage('Unit Test') {
            steps {
                echo '🧪 Étape : Lancement des tests unitaires...'
                script {
                    def status = sh(script: 'npm test', returnStatus: true)
                    if (status != 0) {
                        error("❌ Les tests unitaires ont échoué.")
                    }
                }
            }
        }

        stage('Build application') {
            steps {
                echo '🏗️ Étape : Build de l’application...'
                script {
                    def status = sh(script: 'npm run build-dev', returnStatus: true)
                    if (status != 0) {
                        error("❌ Le build a échoué.")
                    }
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès !'
        }
        failure {
            echo '❗ Pipeline échoué. Consulte la sortie console pour plus de détails.'
        }
    }
}
