pipeline {
  agent any
  tools {
    maven 'MAVEN'
  }
  stages {
    stage('Initialize') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
      }
    }
    stage('Clean') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean'
      }
    }
    stage('Build and Package') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true package'
      }
    }
    stage('Publish Test Report') {
      steps {
           junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
        }  
      }    
  }
}
