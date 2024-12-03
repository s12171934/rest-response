pipeline {
    agent any

    environment {
        GIT_CREDENTIALS = credentials('github-access-token-username-password')
        GIT_EMAIL = 'ssong99999@naver.com'
        GIT_USERNAME = 's12171934'
        REPO_NAME = 's12171934/rest-response'
        NOTIFICATION_EMAIL = 'ssong9520@kakao.com'
        GITHUB_API_URL = "https://api.github.com/repos/${REPO_NAME}"
        JITPACK_API_URL = "https://jitpack.io/api/builds/com.github.${REPO_NAME}"
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
                expression { return env.GIT_BRANCH == 'origin/main' }
            }
            steps {
                script {
                    def newTag = generateNewTag()
                    configureGit()
                    createAndPushTag(newTag)
                    createGithubRelease(newTag)
                }
            }
            post {
                success {
                    script {
                        def tag = sh(script: "git describe --tags --abbrev=0", returnStdout: true).trim()
                        waitForJitPackBuild(tag)
                    }
                }
            }
        }
    }
}

// Helper functions
def generateNewTag() {
    def latestTag = sh(script: "git describe --tags --abbrev=0 || echo 'v0.0.0'", returnStdout: true).trim()
    def (major, minor, patch) = latestTag.replace('v', '').tokenize('.')
    return "${major}.${minor}.${(patch.toInteger() + 1)}"
}

def configureGit() {
    sh """
        git config --global user.email "${env.GIT_EMAIL}"
        git config --global user.name "${env.GIT_USERNAME}"
    """
}

def createAndPushTag(String newTag) {
    sh """
        # 기존 태그 정리
        git tag -d ${newTag} || true
        git push https://${GIT_CREDENTIALS_USR}:${GIT_CREDENTIALS_PSW}@github.com/${REPO_NAME}.git :refs/tags/${newTag} || true
        
        # 새 태그 생성 및 푸시
        git tag -a ${newTag} -m "Release ${newTag}"
        git push https://${GIT_CREDENTIALS_USR}:${GIT_CREDENTIALS_PSW}@github.com/${REPO_NAME}.git ${newTag}
    """
}

def createGithubRelease(String newTag) {
    sh """
        curl -X POST \
        -H "Authorization: Bearer ${GIT_CREDENTIALS_PSW}" \
        -H "Accept: application/vnd.github.v3+json" \
        ${env.GITHUB_API_URL}/releases \
        -d '{
            "tag_name": "${newTag}",
            "name": "Release ${newTag}",
            "body": "Automated release from Jenkins",
            "draft": false,
            "prerelease": false
        }'
    """
}

def waitForJitPackBuild(String tag) {
    retry(30) {
        sleep(time: 20, unit: 'SECONDS')
        def response = httpRequest(
            url: "${env.JITPACK_API_URL}/${tag}",
            validResponseCodes: '200, 404'
        )

        if (response.content.status == 'SUCCESS') {
            echo 'JitPack build successful'
            sendSuccessEmail(tag)
            return true
        }
        error 'JitPack build failed'
    }
}

def sendSuccessEmail(String tag) {
    emailext(
        to: env.NOTIFICATION_EMAIL,
        subject: "JitPack build successful: ${tag}",
        body: "JitPack build successful: ${tag}"
    )
}