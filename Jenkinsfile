pipeline {
    agent any

    environment {
        GIT_CREDENTIALS = credentials('github-access-token-username-password')
    }

    tools {
        jdk 'JDK21'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Create Release') {
            when {
                expression { 
                    return env.GIT_BRANCH == 'origin/main' 
                }
            }
            steps {
                script {
                    // 최신 태그 가져오기
                    def latestTag = sh(script: "git describe --tags --abbrev=0 || echo 'v0.0.0'", returnStdout: true).trim()
                    def (major, minor, patch) = latestTag.replace('v', '').tokenize('.')
                    
                    // 새 버전 생성 (patch 버전 증가)
                    def newTag = "v${major}.${minor}.${(patch.toInteger() + 1)}"

                    sh """
                        git config --global user.email "ssong99999@naver.com"
                        git config --global user.name "s12171934"
                        
                        # 로컬 태그 삭제
                        git tag -d ${newTag} || true
                        
                        git tag -d ${newTag} || true
                        git push https://${GIT_CREDENTIALS_USR}:${GIT_CREDENTIALS_PSW}@github.com/s12171934/rest-response.git :refs/tags/${newTag} || true
                        
                        git tag -a ${newTag} -m "Release ${newTag}"
                        git push https://${GIT_CREDENTIALS_USR}:${GIT_CREDENTIALS_PSW}@github.com/s12171934/rest-response.git ${newTag}
                    """

                    sh """
                        curl -X POST \
                        -H "Authorization: Bearer ${GIT_CREDENTIALS_PSW}" \
                        -H "Accept: application/vnd.github.v3+json" \
                        https://api.github.com/repos/s12171934/rest-response/releases \
                        -d '{
                            "tag_name": "${newTag}",
                            "name": "Release ${newTag}",
                            "body": "Automated release from Jenkins",
                            "draft": false,
                            "prerelease": false
                        }'
                    """
                }
            }
        }
    }
}