import groovy.json.JsonSlurper
import java.security.*

def getFolderName() {
  def array = pwd().split("/")
  return array[array.length - 2];
}
def parseJson(jsonString) {
    def lazyMap = new JsonSlurper().parseText(jsonString)
    def m = [:]
    m.putAll(lazyMap)
    return m
}

def parseJsonArray(jsonString){
    def datas = readJSON text: jsonString
    return datas
}

def parseJsonString(jsonString, key){
    def datas = readJSON text: jsonString
    String Values = writeJSON returnText: true, json: datas[key]
    return Values
}

def parseYaml(jsonString) {
    def datas = readYaml text: jsonString
    String yml = writeYaml returnText: true, data: datas['kubernetes']
    return yml

}

def createYamlFile(data,filename) {
    writeFile file: filename, text: data
}

def sha1function(data){
    writeFile file: 'data.txt', text: data
    String md5String = sha1 file: 'data.txt'
    return md5String
}

def returnSecret(path,secretValues){
    def secretValueFinal= []
    for(secret in secretValues) {
        def secretValue = [:]
        //secretValue['envVar'] = secret.envVariable
        //secretValue['vaultKey'] = secret.vaultKey
        secretValue.put('envVar',secret.envVariable)
        secretValue.put('vaultKey',secret.vaultKey)
        secretValueFinal.add(secretValue)
    }
    def secrets = [:]
    secrets["path"] = path
    secrets['engineVersion']=  2
    secrets['secretValues'] = secretValueFinal

    return secrets
}

// String str = ''
// loop to create a string as -e $STR -e $PTDF

def dockerVaultArguments(secretValues){
    def data = []
    for(secret in secretValues) {
        data.add('$'+secret.envVariable+' > .'+secret.envVariable)
    }
    return data
}

def dockerVaultArgumentsFile(secretValues){
    def data = []
    for(secret in secretValues) {
        data.add(secret.envVariable)
    }
    return data
}



def returnVaultConfig(vaultURL,vaultCredID){
    echo vaultURL
    echo vaultCredID
    def configurationVault = [:]
    //configurationVault["vaultUrl"] = vaultURL
    configurationVault["vaultCredentialId"] = vaultCredID
    configurationVault["engineVersion"] = 2
    return configurationVault
}

def getValue(metadata, key){
    def metadMap = parseJson(metadata)
    def value = metadMap['general']['contextPath']
    return value;
}

def agentLabel = "${env.JENKINS_AGENT == null ? "":env.JENKINS_AGENT}"

pipeline {
    agent { label agentLabel }
    environment {
      DEFAULT_STAGE_SEQ = "'CodeCheckout','Build','UnitTests','SonarQubeScan','Deploy','Destroy'"
      CUSTOM_STAGE_SEQ = "${DYNAMIC_JENKINS_STAGE_SEQUENCE}"
      PROJECT_TEMPLATE_ACTIVE = "${DYNAMIC_JENKINS_STAGE_NEEDED}"
      LIST = "${env.PROJECT_TEMPLATE_ACTIVE == 'true' ? env.CUSTOM_STAGE_SEQ : env.DEFAULT_STAGE_SEQ}"
      BRANCHES = "${env.GIT_BRANCH}"
      COMMIT = "${env.GIT_COMMIT}"
      RELEASE_NAME = "java15springbootmaven"
      SERVICE_PORT = "${APP_PORT}"
      DOCKERHOST = "${DOCKERHOST_IP}"
      REGISTRY_URL = "${DOCKER_REPO_URL}"
      ACTION = "${ACTION}"
      DEPLOYMENT_TYPE = "${DEPLOYMENT_TYPE == "" ? "EC2" : DEPLOYMENT_TYPE}"
      KUBE_SECRET = "${KUBE_SECRET}"
      foldername = getFolderName()
      CHROME_BIN = "/usr/bin/google-chrome"
      ARTIFACTORY = "${ARTIFACTORY == "" ? "ECR" : ARTIFACTORY}"
      ARTIFACTORY_CREDENTIALS = "${ARTIFACTORY_CREDENTIAL_ID}"
      SONARQUBE_HOST_URL = "http://idp-sonar-2125129343.us-east-1.elb.amazonaws.com"
      STAGE_FLAG = "${STAGE_FLAG}"
      JENKINS_METADATA = "${JENKINS_METADATA}"
      SONAR_CREDENTIAL_ID= "${env.SONAR_CREDENTIAL_ID}"

    }

    stages {
        stage('Running Stages') {
            agent { label agentLabel }
            steps {
                script {
                    def listValue = "$env.LIST"
                    def list = listValue.split(',')
                    print(list)

                    // For Context Path read
                    String generalProperties = parseJsonString(env.JENKINS_METADATA,'general')
                    generalPresent = parseJsonArray(generalProperties)
                    if(generalPresent.repoName == ''){
                        generalPresent.repoName = env.RELEASE_NAME
                    }
                    env.CONTEXT = generalPresent.contextPath


                    if (env.DEPLOYMENT_TYPE == 'KUBERNETES'){
                        String kubeProperties = parseJsonString(env.JENKINS_METADATA,'kubernetes')
                        kubeVars = parseJsonArray(kubeProperties)
                        if(kubeVars['vault']){
                            String kubeData = parseJsonString(kubeProperties,'vault')
                            def kubeValues = parseJsonArray(kubeData)
                            if(kubeValues.type == 'vault'){
                                String helm_file = parseYaml(env.JENKINS_METADATA)
                                echo helm_file
                                createYamlFile(helm_file,"Helm.yaml")
                            }
                        }else {
                            String helm_file = parseYaml(env.JENKINS_METADATA)
                            echo helm_file
                            createYamlFile(helm_file,"Helm.yaml")
                        }
                    }

                    echo "projectTemplateActive - $env.PROJECT_TEMPLATE_ACTIVE"
                    if (env.CUSTOM_STAGE_SEQ != null) {
                        echo "customStagesSequence - $env.CUSTOM_STAGE_SEQ"
                    }
                    echo "defaultStagesSequence - $env.DEFAULT_STAGE_SEQ"
                    for (int i = 0; i < list.size(); i++) {
                        print(list[i])
                        if (list[i] == "'CodeCheckout'") {
                            print(list[i])
                            stage('Initialisation') {
                                print(list[i])
                                echo "INIT CALL"
                                // stage details here
                                env.BUILD_TAG = "${BUILD_NUMBER}"
                                def job_name = "$env.JOB_NAME"
                                print(job_name)
                                def namespace = ''
                                def values = job_name.split('/')
                                if (env.DEPLOYMENT_TYPE == 'KUBERNETES'){
                                    if (kubeVars.namespace != null && kubeVars.namespace != '') {
                                      // namespace = kubeVars.namespace
                                      namespace_prefix = values[0].replaceAll("[^a-zA-Z0-9]+","").toLowerCase().take(50)
                                      namespace = "$namespace_prefix-$env.foldername".toLowerCase()
                                    } else {
                                        namespace_prefix = values[0].replaceAll("[^a-zA-Z0-9]+","").toLowerCase().take(50)
                                        namespace = "$namespace_prefix-$env.foldername".toLowerCase()
                                    }
                                }
                                service = values[2].replaceAll("[^a-zA-Z0-9]+", "").toLowerCase().take(50)
                                print("kube namespace: $namespace")
                                print("service name: $service")
                                env.namespace_name = namespace
                                env.service = service
                                if (env.STAGE_FLAG != 'null' && env.STAGE_FLAG != null) {
                                    stage_flag = parseJson("$env.STAGE_FLAG")
                                } else {
                                    stage_flag = parseJson('{"qualysScan": false, "sonarScan": true, "zapScan": false, "rapid7Scan": false, "sysdig": false}')
                                }
                                if (!stage_flag) {
                                    stage_flag = parseJson('{"qualysScan": false, "sonarScan": true, "zapScan": false, "rapid7Scan": false, "sysdig": false}')
                                }

                                if (env.ARTIFACTORY == "ECR") {

                                    def url_string = "$REGISTRY_URL"
                                    url = url_string.split('\\.')
                                    env.AWS_ACCOUNT_NUMBER = url[0]
                                    env.ECR_REGION = url[3]
                                    echo "ecr region: $ECR_REGION"
                                    echo "ecr acc no: $AWS_ACCOUNT_NUMBER"

                                } else if (env.ARTIFACTORY == "ACR") {
                                    def url_string = "$REGISTRY_URL"
                                    url = url_string.split('/')
                                    env.ACR_LOGIN_URL = url[0]
                                    echo "Reg Login url: $ACR_LOGIN_URL"
                                }

                            }
                        } else if ("${list[i]}" == "'UnitTests'" && env.ACTION == 'DEPLOY') {
                            stage('UnitTests') {
                                print(list[i])
                                // stage details here
                                sh 'JAVA_HOME=/usr/lib/jvm/jdk-15 mvn clean test --batch-mode'

                            }
                        } else if ("${list[i]}" == "'SonarQubeScan'" && env.ACTION == 'DEPLOY' && stage_flag['sonarScan']) {
                             stage('SonarQube') {
                                // stage details here
                                TEMP_STAGE_NAME = "$STAGE_NAME"
                                    if (env.SONAR_CREDENTIAL_ID != null && env.SONAR_CREDENTIAL_ID != '') {
                                        withCredentials([usernamePassword(credentialsId: "$SONAR_CREDENTIAL_ID", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                            sh """JAVA_HOME=/usr/lib/jvm/jdk-15 mvn --batch-mode -V -U -e org.sonarsource.scanner.maven:sonar-maven-plugin:3.5.0.1254:sonar -Dsonar.java.binaries='.' -Dsonar.exclusions='pom.xml, target/**/*' -Dsonar.projectKey=LogonService -Dsonar.projectName=LogonService -Dsonar.host.url="${generalPresent.sonarHost}" -Dsonar.login=$PASSWORD"""
                                        }
                                    }
                                    else{
                                        withSonarQubeEnv('pg-sonar') {
                                            sh "JAVA_HOME=/usr/lib/jvm/jdk-15 mvn --batch-mode -V -U -e org.sonarsource.scanner.maven:sonar-maven-plugin:3.5.0.1254:sonar -Dsonar.java.binaries='.' -Dsonar.exclusions='pom.xml, target/**/*' -Dsonar.projectKey=LogonService -Dsonar.projectName=LogonService"
                                            }
                                    }
                            }


                        } else if ("${list[i]}" == "'Build'" && env.ACTION == 'DEPLOY') {
                            stage('Build') {
                                // stage details here

                                echo "echoed folder--- $foldername"
                                echo "echoed BUILD_TAG--- $BUILD_TAG"

                                sh 'JAVA_HOME=/usr/lib/jvm/jdk-15 mvn clean install -Dmaven.test.skip=true'
                                sh 'docker build --build-arg STAGEENV=${foldername} -t "$REGISTRY_URL:$BUILD_TAG" -t "$REGISTRY_URL:latest" .'

                            }
                        } else if ("${list[i]}" == "'QualysScan'" && env.ACTION == 'DEPLOY' && stage_flag['qualysScan']) {
                            stage('Qualys Scan') {
                                getImageVulnsFromQualys useGlobalConfig: true,
                                        imageIds: env.REGISTRY_URL + ":" + env.BUILD_TAG
                            }
                        } else if ("${list[i]}" == "'Rapid7Scan'" && env.ACTION == 'DEPLOY' && stage_flag['rapid7Scan']) {
                            stage('Rapid7 Scan') {
                                assessContainerImage failOnPluginError: true,
                                        imageId: env.REGISTRY_URL + ":" + env.BUILD_TAG,
                                        thresholdRules: [
                                                exploitableVulnerabilities(action: 'Mark Unstable', threshold: '1'),
                                                criticalVulnerabilities(action: 'Fail', threshold: '1')
                                        ],
                                        nameRules: [
                                                vulnerablePackageName(action: 'Fail', contains: 'nginx')
                                        ]
                            }
                        } else if ("${list[i]}" == "'SysdigScan'" && env.ACTION == 'DEPLOY' && stage_flag['sysdigScan']) {
                            stage('Sysdig Scan') {
                                sh 'echo  $REGISTRY_URL:$BUILD_TAG > sysdig_secure_images'
                                sysdig inlineScanning: true, bailOnFail: true, bailOnPluginFail: true, name: 'sysdig_secure_images'
                            }
                        } else if ("${list[i]}" == "'Deploy'") {
                            stage('Deploy') {
                                // stage details here
                                if (env.ARTIFACTORY == 'ECR') {
                                    withCredentials([usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                                        sh 'set +x; eval $(aws ecr get-login --no-include-email --registry-ids "$AWS_ACCOUNT_NUMBER" --region "$ECR_REGION" | sed \'s|https://||\') ;set -x'
                                    }
                                }
                                if (env.ARTIFACTORY == 'JFROG') {
                                    withCredentials([usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                        sh 'docker login -u "$USERNAME" -p "$PASSWORD" "$REGISTRY_URL"'

                                    }
                                }
                                if (env.ARTIFACTORY == 'ACR') {
                                    withCredentials([usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                        sh 'docker login -u "$USERNAME" -p "$PASSWORD" "$ACR_LOGIN_URL"'
                                    }
                                }
                                if (env.ACTION == 'DEPLOY') {
                                    sh 'docker push "$REGISTRY_URL:$BUILD_TAG"'
                                    sh 'docker push "$REGISTRY_URL:latest"'
                                }
                                if (env.ACTION == 'DEPLOY' || env.ACTION == 'PROMOTE' || env.ACTION == 'ROLLBACK') {
                                    if (env.ACTION == 'PROMOTE') {
                                        echo "------------------------------ inside promote condition -------------------------------"
                                        sh """ docker pull "${generalPresent.promoteSource}" """
                                        sh """ docker image tag "${generalPresent.promoteSource}" "$REGISTRY_URL:${generalPresent.promoteTag}" """
                                        sh """ docker push "$REGISTRY_URL:${generalPresent.promoteTag}" """
                                        env.BUILD_TAG = "${generalPresent.promoteTag}"

                                    }
                                    if (env.ACTION == 'ROLLBACK') {
                                        echo "-------------------------------------- inside rollback condition -------------------------------"
                                        env.BUILD_TAG = "${generalPresent.rollbackTag}"

                                    }
                                    if (env.DEPLOYMENT_TYPE == 'EC2') {
                                        if (env.ARTIFACTORY == 'ECR') {
                                            withCredentials([usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                                                sh 'set +x; ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY `aws ecr get-login --no-include-email --region "$ECR_REGION" --registry-ids "$AWS_ACCOUNT_NUMBER"` " ;set -x'
                                            }
                                        }
                                        if (env.ARTIFACTORY == 'JFROG') {
                                            withCredentials([usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                                sh 'ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker login -u "$USERNAME" -p "$PASSWORD" "$REGISTRY_URL""'
                                            }
                                        }
                                        if (env.ARTIFACTORY == 'ACR') {
                                            withCredentials([usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                                sh 'ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker login -u "$USERNAME" -p "$PASSWORD" "$ACR_LOGIN_URL""'
                                            }
                                        }

                                        sh 'ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "sleep 5s"'
                                        sh 'ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker pull "$REGISTRY_URL:$BUILD_TAG""'
                                        sh """ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker stop ${generalPresent.repoName} || true && docker rm ${generalPresent.repoName} || true" """

                                            // Read Docker Vault properties
                                            String dockerProperties = parseJsonString(env.JENKINS_METADATA,'docker')
                                            dockerData = parseJsonArray(dockerProperties)
                                            if(dockerData['vault']){

                                                String vaultProperties = parseJsonString(dockerProperties,'vault')
                                                def vaultType = parseJsonArray(vaultProperties)
                                                if (vaultType.type == 'vault') {
                                                    echo "type is vault"
                                                    env.VAULT_TYPE = 'vault'

                                                    String vaultConfiguration = parseJsonString(vaultProperties,'configuration')
                                                    def vaultData = parseJsonArray(vaultConfiguration)
                                                    def vaultConfigurations = returnVaultConfig(vaultData.vaultUrl, vaultData.vaultCredentialID)
                                                    env.VAULT_CONFIG = vaultConfigurations
                                                    // Getting the secret Values
                                                    String vaultSecretValues = parseJsonString(vaultProperties,'secrets')
                                                    def vaultSecretData = parseJsonArray(vaultSecretValues)
                                                    def vaultSecretConfigData = returnSecret(vaultSecretData.path, vaultSecretData.secretValues)
                                                    env.VAULT_SECRET_CONFIG = vaultSecretConfigData
                                                    def dockerEnv = dockerVaultArguments(vaultSecretData.secretValues)
                                                    def secretkeys = dockerVaultArgumentsFile(vaultSecretData.secretValues)

                                                    withVault([configuration: vaultConfigurations, vaultSecrets: [vaultSecretConfigData]]) {
                                                        def data = []
                                                        for(secret in dockerEnv){
                                                            sh "echo $secret"
                                                        }
                                                        for(keys in secretkeys){
                                                            sh "echo $keys=\$(cat .$keys) >> .secrets"
                                                        }
                                                        sh 'cat .secrets;'
                                                    }
                                                    def result = sh(script: 'cat .secrets', returnStdout: true)

                                                    sh 'scp -o "StrictHostKeyChecking=no" .secrets ciuser@$DOCKERHOST:/home/ciuser/docker-env'
                                                    //sh 'ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "echo $SECRETS > secrets"'
                                                    sh 'rm -rf .secrets'
                                                    if (env.DEPLOYMENT_TYPE == 'EC2' && env.CONTEXT == 'null') {
                                                      sh """ ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker run -d --restart always --name ${generalPresent.repoName}  --env-file docker-env -p ${dockerData.hostPort}:$SERVICE_PORT $REGISTRY_URL:$BUILD_TAG" """
                                                    }
                                                    else if (env.DEPLOYMENT_TYPE == 'EC2' && env.CONTEXT != 'null') {
                                                      sh """ ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker run -d --restart always --name ${generalPresent.repoName}  --env-file docker-env   -p ${dockerData.hostPort}:$SERVICE_PORT -e context=$CONTEXT $REGISTRY_URL:$BUILD_TAG" """
                                                    }

                                                }
                                            }
                                            else {

                                                if (env.DEPLOYMENT_TYPE == 'EC2' && env.CONTEXT == 'null') {
                                                    sh """ ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker run -d --restart always --name ${generalPresent.repoName} -p ${dockerData.hostPort}:$SERVICE_PORT $REGISTRY_URL:$BUILD_TAG" """
                                                }
                                                else if (env.DEPLOYMENT_TYPE == 'EC2' && env.CONTEXT != 'null') {
                                                    sh """ ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker run -d --restart always --name ${generalPresent.repoName} -p ${dockerData.hostPort}:$SERVICE_PORT -e context=$CONTEXT $REGISTRY_URL:$BUILD_TAG" """
                                                }
                                            }

                                    }
                                    if (env.DEPLOYMENT_TYPE == 'KUBERNETES') {

                                        if (env.ARTIFACTORY == 'JFROG') {
                                            withCredentials([file(credentialsId: "$KUBE_SECRET", variable: 'KUBECONFIG'), usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                                sh '''
                                                    kubectl create ns "$namespace_name" || true
                                                    kubectl -n "$namespace_name" create secret docker-registry regcred --docker-server="$REGISTRY_URL" --docker-username="$USERNAME" --docker-password="$PASSWORD" || true
                                                '''
                                            }
                                        }
                                        if (env.ARTIFACTORY == 'ACR') {
                                            withCredentials([file(credentialsId: "$KUBE_SECRET", variable: 'KUBECONFIG'), usernamePassword(credentialsId: "$ARTIFACTORY_CREDENTIALS", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                                                sh '''
                                                  kubectl create ns "$namespace_name" || true
                                                  kubectl -n "$namespace_name" create secret docker-registry regcred --docker-server="$ACR_LOGIN_URL" --docker-username="$USERNAME" --docker-password="$PASSWORD" || true
                                                '''
                                            }
                                        }
                                        withCredentials([file(credentialsId: "$KUBE_SECRET", variable: 'KUBECONFIG')]) {
                                            sh """
                                                ls -lart
                                                echo "context: $CONTEXT" >> Helm.yaml
                                                cat Helm.yaml

                                                sed -i s+#SERVICE_NAME#+"$service"+g ./helm_chart/values.yaml ./helm_chart/Chart.yaml
                                                kubectl create ns "$namespace_name" || true
                                                helm upgrade --install "${generalPresent.repoName}" -n "$namespace_name" helm_chart --atomic --timeout 300s --set image.repository="$REGISTRY_URL" --set image.tag="$BUILD_TAG" --set image.registrySecret="regcred"  --set service.internalport="$SERVICE_PORT" -f Helm.yaml

                                            """
                                            script {
                                                env.temp_service_name = "${generalPresent.repoName}E-$service".take(63)
                                                def url = sh(returnStdout: true, script: '''kubectl get svc -n "$namespace_name" | grep "$temp_service_name" | awk '{print $4}' ''').trim()
                                                if (url != "<pending>") {
                                                    print("##\$@\$ http://$url ##\$@\$")
                                                } else {
                                                    currentBuild.result = 'ABORTED'
                                                    error('Aborting the job as access url has not generated')
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        } else if ("${list[i]}" == "'Destroy'" && env.ACTION == 'DESTROY') {
                            stage('Destroy') {
                                // stage details here
                                if (env.DEPLOYMENT_TYPE == 'EC2') {
                                    sh """ssh -o "StrictHostKeyChecking=no" ciuser@$DOCKERHOST "docker stop ${generalPresent.repoName} || true && docker rm ${JOB_BASE_NAME} || true" """
                                }
                                if (env.DEPLOYMENT_TYPE == 'KUBERNETES') {
                                    withCredentials([file(credentialsId: "$KUBE_SECRET", variable: 'KUBECONFIG')]) {
                                        sh """
                                            helm uninstall ${generalPresent.repoName} -n "$namespace_name"
                                        """
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    post {
        cleanup {
            sh 'docker  rmi  $REGISTRY_URL:$BUILD_TAG $REGISTRY_URL:latest || true'
        }
    }

}
