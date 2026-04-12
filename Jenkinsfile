pipeline{
    agent any
    tools{
        maven 'Maven'
    }
    stages{
        stage('Build Maven'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Niraj-ace/BankApp']])
                bat 'mvn clean install'
            }
        }
        stage('Docker image build'){
            steps{
                script{
                    bat "docker build -t nirajsaitama/bankapp-springboot:${BUILD_NUMBER} ."
                    bat "docker tag nirajsaitama/bankapp-springboot:${BUILD_NUMBER} nirajsaitama/bankapp-springboot:latest"
                }
            }
        }
        stage('Dockerhub push'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'docker-hub', variable: 'dockerhubpwd')]) {
                        bat "docker login -u nirajsaitama -p ${dockerhubpwd}"
                    }
                    bat "docker push nirajsaitama/bankapp-springboot:${BUILD_NUMBER}"
                    bat "docker push nirajsaitama/bankapp-springboot:latest"
                }
            }
        }
    }
}