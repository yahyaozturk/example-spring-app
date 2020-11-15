pipeline {
  agent any
  stages {
    stage('Initialize Pipeline') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
        script {
          dockerHome = tool 'DOCKER'
          sh "${dockerHome}/bin/docker version"
        }
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
    stage('Execute Tests') {
      parallel {
        stage('Execute Unit Test') {
          steps {
            junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
          }
        }
        stage('Execute Functional Test') {
          steps {
            sh 'echo "Functional Tests are running"'
          }
        }
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
    stage('Build Docker Image') {
      steps {
        sh 'docker version'
        sh "docker tag xaph/example-spring-app:0.0.1-SNAPSHOT yahyaozturk/vf:${env.BUILD_TAG}"
      }
    }
    stage('Publish to Docker Repository') {
      steps {
        sh 'docker login -u yahyaozturk -p Avis1111'
        sh "docker push yahyaozturk/vf:${env.BUILD_TAG}"
      }
    }
    stage('Deployment') {
      parallel {
        stage('DEV') {
          steps {
            sh 'mvn -Dmaven.test.failure.ignore=true clean'
          }
        }
        stage('TEST') {
          steps {
            sh 'echo "HELLO"'
          }
        }
      }
    }
    stage('Testing') {
      parallel {
        stage('Run Health Suite') {
          steps {
            junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
          }
        }
        stage('Run Smoke Suite') {
          steps {
            junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true)
          }
        }
        stage('Run Regression Suite') {
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
