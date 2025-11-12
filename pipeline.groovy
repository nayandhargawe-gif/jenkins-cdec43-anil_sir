  // pipeline {
//     agent  {label 'slave'}
//     stages {
//         stage('git_checkout') {
//             steps {
//                 echo "pull-sucess"
//             }
//         }
//         stage('build-stage') {
//             steps {
               
//                 echo "build-sucess" 
//             }
//         }
//         stage('test-stage') {
//             steps {
               
//                 echo "test-sucess" 
//             }
//         }
//         stage('Deploy') {
//             steps {
//                 echo "deploy-sucess"
//             }
//         }
//     }
// }


// ---


pipeline {
    agent {label 'slave'}
    stages {
        stage('git_checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Anilbamnote/student-ui-app.git'
            }
        }
        stage('build-stage') {
            steps {
               sh '/opt/maven/bin/mvn clean package'
             
            }
        }
        stage('test-stage') {
            steps {
               withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar-cred') {
                    sh '/opt/maven/bin/mvn sonar:sonar' 
            }
            //   withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonarQube') {
            //         // sh '/opt/maven/bin/mvn sonar:sonar'
            // }
                  // sh '''/opt/maven/bin/mvn sonar:sonar -Dsonar.projectKey=studentapp1 -Dsonar.host.url=http://172.31.11.57:9000 -Dsonar.login=c7d0475cab0ac0bc8a86058578e0e85ea1c14b86'''
        }
        }
          stage('Quality-gate') {
            steps {
                timeout(10) {
               
            }
                // waitForQualityGate true
                waitForQualityGate abortPipeline: true, credentialsId: 'sonar-cred'
            }
        }
        stage('S3-Artifactory') {
            steps {
               sh '''aws s3 cp target/studentapp-2.2-SNAPSHOT.war s3:bucketforjenkinspipeline'''
              // sh 'aws s3 cp target/studentapp-2.2-SNAPSHOT.war  s3://new-terr-buck-atrifatory097'
            }
        }
      
        stage('Deploy') {
            steps {
                  deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: 'tomcat', path: '', url: '13.49.137.130:8080')], contextPath: '/', war: '**/*.war'  
              // deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: 'tomcat', path: '', url: 'http://13.53.158.116:8080')], contextPath: '/', war: '**/*.war'
                // echo "deploy successfull"
            }
        }
    }
}
