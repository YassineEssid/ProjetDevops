pipeline {
    agent any
    stages {
        stage('Git') {
            steps {
                dir('ProjetDevops') {
                    git branch: 'feat/subscription', credentialsId: 'helmi123', url: 'https://github.com/YassineEssid/ProjetDevops.git'
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
                            sh '''
                                mvn sonar:sonar \
                                -Dsonar.token=$SONAR_TOKEN \
                                -Dsonar.projectKey=kaddemm \
                                -Dsonar.projectName=kaddem \
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                                -Dsonar.java.coveragePlugin=jacoco
                            '''
                        }
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                dir('ProjetDevops') {
                    sh 'mvn deploy -e -X -DskipTests'
                }
            }
        }

        // stage('Nexus') {
        //     steps {
        //         dir('ProjetDevops') {
        //             sh 'mvn clean deploy -Dmaven.test.skip=true'
        //         }
        //     }
        // }

    }
}
