pipeline {
    agent any
    stages {
        stage('Git') {
            steps {
                dir('kaddem') {
                    git branch: 'feat/subscription', credentialsId: 'helmi123', url: 'https://github.com/YassineEssid/ProjetDevops.git'
                }
            }
        }

        stage('Build') {
            steps {
                dir('kaddem') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Run Tests') {
            steps {
                dir('kaddem') {
                    sh 'mvn test -Dspring.profiles.active=test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('kaddem') {
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
                dir('kaddem') {
                    sh 'mvn deploy -e -X -DskipTests'
                }
            }
        }

        // stage('Nexus') {
        //     steps {
        //         dir('kaddem') {
        //             sh 'mvn clean deploy -Dmaven.test.skip=true'
        //         }
        //     }
        // }

    }
}
