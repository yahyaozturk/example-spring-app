pipeline {
  agent any
  stages {
    stage('Initialize Pipeline') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
      }
    }
    stage('Clean Workspace') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean'
      }
    }
    stage('Build and Package Microservice') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true package'
      }
    }
    stage('Run Unit Test and Publish Report') {
      steps {
        junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
      }
    }
    stage('Run Load Test') {
      steps {
        bzt(params: 'config/first_exe.yml', generatePerformanceTrend: true, printDebugOutput: true)
      }
    }
  }
  tools {
    maven 'MAVEN'
  }
}