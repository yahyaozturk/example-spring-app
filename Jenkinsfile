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
    stage('SonarQube analysis') {
      steps {
        script {
          scannerHome = tool 'SONAR'
        }

        withSonarQubeEnv('SONAR SERVER') {
          sh "${scannerHome}/bin/sonar-scanner"
        }

      }
    }
    stage('Run Unit Test and Publish Report') {
      steps {
        junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
      }
    }
    stage('Quality Gate') {
      steps {
        script {
          timeout(time: 1, unit: 'HOURS') {
            qg = waitForQualityGate()
          }
          if (qg.status != 'OK') {
            error "Pipeline aborted due to quality gate failure: ${qg.status}"
          }
        }

      }
    }
    stage('Deployment') {
      parallel {
        stage('Deployment') {
          steps {
            sh 'mvn -Dmaven.test.failure.ignore=true clean'
          }
        }
        stage('DEV') {
          steps {
            sh 'echo "HELLO"'
          }
        }
        stage('TEST') {
          steps {
            sh 'echo "HELLO"'
          }
        }
      }
    }
    stage('Run Load Test') {
      steps {
        bzt(params: 'config/first_exe.yml', generatePerformanceTrend: true, printDebugOutput: true)
      }
    }
    stage('Deploy to Pre PROD') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean'
      }
    }
  }
  tools {
    maven 'MAVEN'
  }
}