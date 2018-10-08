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
  stage("Quality Gate"){
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
   stage('Deploy to DEV') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean'
      }
    }
  stage('Deploy to TEST') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean'
      }
    }
    stage('Run Load Test') {
      steps {
        bzt(params: 'config/first_exe.yml', generatePerformanceTrend: true, printDebugOutput: true)
      }
    }
   stage('Deploy to STAGING') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean'
      }
    }
  }
  tools {
    maven 'MAVEN'
  }
}
