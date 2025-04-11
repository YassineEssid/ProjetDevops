pipeline {
  agent {
    docker {
      image 'node:18'
      args '-u root:root' // permet d’éviter certains problèmes de permissions
    }
  }
  stages {
    stage('Install dependencies') {
      steps {
        sh 'npm install'
      }
    }

    stage('Unit Test') {
      steps {
        sh 'npm test'
      }
    }

    stage('Build application') {
      steps {
        sh 'npm run build'
      }
    }
  }
}
