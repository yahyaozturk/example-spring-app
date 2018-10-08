pipeline {
  agent any
  stages {
    stage('Init') {
      steps {
        tool 'MAVEN'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true package'
      }
    }
  }
}