pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'HOURS')
    }
    environment {
        GIT_CREDENTIALS_ID = '1' // 在 Jenkins 中配置的 GitHub 凭证ID
        REPO_NAME = 'Wittyhuahua/CusRelManage'  // 仓库名称
        BRANCH_NAME = 'master'                  // 分支名称
        ZIP_PATH = ''                           // 这里将被动态设置
        USER_HOME = "${env.HOME}"              // 获取用户的主目录
    }

    parameters {
        string(name: 'branch', defaultValue: 'main', description: 'The branch to build.')
        string(name: 'GIT_URL', defaultValue: 'https://github.com/Wittyhuahua/CusRelManage.git', description: 'The Git repository URL.')
        string(name: 'MODEL', defaultValue: '1', description: 'Model number')
        string(name: 'ZIP_PATH', defaultValue: '', description: 'Zip path')
        string(name: 'COBOT_URL', defaultValue: 'http://127.0.0.1:8088', description: 'COBOT API URL')
        string(name: 'COBOT_TOKEN', defaultValue: '', description: 'COBOT Authentication token')
        string(name: 'PROJECTNAME', defaultValue: '', description: 'Project name')
        string(name: 'DEFECTCONFIGID', defaultValue: '5e57409394bbd91d299f2a1b', description: 'Defect config ID')
    }

    stages {
        stage('Download Code ZIP') {
            steps {
                script {
                    // 动态构建ZIP文件路径
                    def repoName = params.GIT_URL.replaceAll('.*/', '').replaceAll('.git', '')
                    env.ZIP_PATH = "${env.USER_HOME}/${repoName}-${params.branch}.zip"
                    
                    // 下载代码ZIP文件
                    sh "curl -L -u ${credentials(GIT_CREDENTIALS_ID)}:x-oauth-basic ${params.GIT_URL}/archive/refs/heads/${params.branch}.zip -o ${env.ZIP_PATH}"
                }
            }
        }
        
        stage('Run Java Application') {
            steps {
                script {
                    // 构建并执行Java命令
                    def cmd = """
                        java -jar /Users/essie/work/bdsoft/cobot-cmd-api/target/cobot-cmd-api-1.0-SNAPSHOT.jar \\
                        --model=\${MODEL} \\
                        --srcPath=\${ZIP_PATH} \\
                        --url=\${COBOT_URL} \\
                        --token=\${COBOT_TOKEN} \\
                        --projectName=\${PROJECTNAME} \\
                        --defectConfigId=\${DEFECTCONFIGID}
                    """.stripMargin()
                    sh returnStdout: true, script: cmd
                }
            }
        }
    }
}
