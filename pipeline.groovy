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
               
                sh 'mvn sonar:sonar  -Dsonar.projectKey=studentapp -Dsonar.host.url=http://13.59.55.73:9000 -Dsonar.login=05d72f8340ad25c99e810ea637aa4eb0f7c87745'
            }
        }
        stage('Deploy') {
            steps {
                echo "deploy-sucess"
            }
        }
    }
}