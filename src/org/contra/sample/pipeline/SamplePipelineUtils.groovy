#!/usr/bin/groovy
package org.contra.sample.pipeline

import org.centos.*

/**
 * Library to execute script in container
 * Container must have been defined in a podTemplate
 *
 * @param containerName Name of the container for script execution
 * @param script Complete path to the script to execute
 * @return
 */
def executeInContainer(String stageName, String containerName, String script) {
    //
    // Kubernetes plugin does not let containers inherit
    // env vars from host. We force them in.
    //
    containerEnv = env.getEnvironment().collect { key, value -> return key+'='+value }
    sh "mkdir -p ${stageName}"
    try {
        withEnv(containerEnv) {
            container(containerName) {
                sh script
            }
        }
    } catch (err) {
        throw err
    } finally {
        sh "mv -vf logs ${stageName}/logs || true"
    }
}

/**
 *
 * @param openshiftProject name of openshift namespace/project.
 * @param nodeName podName we are going to get container logs from.
 * @return
 */
@NonCPS
def getContainerLogsFromPod(String openshiftProject, String nodeName) {
    sh 'mkdir -p podInfo'
    openshift.withCluster() {
        openshift.withProject(openshiftProject) {
            names       = openshift.raw("get", "pod",  "${env.NODE_NAME}", '-o=jsonpath="{.status.containerStatuses[*].name}"')
            String containerNames = names.out.trim()

            containerNames.split().each {
                String log = containerLog name: it, returnLog: true
                writeFile file: "podInfo/containerLog-${it}-${nodeName}.txt",
                        text: log
            }
            archiveArtifacts "podInfo/containerLog-*.txt"
        }
    }
}

/**
 *
 * @param nick nickname to connect to IRC with
 * @param channel channel to connect to
 * @param message message to send
 * @param ircServer optional IRC server defaults to irc.freenode.net:6697
 * @return
 */
def sendIRCNotification(String nick, String channel, String message, String ircServer="irc.freenode.net:6697") {
    sh """
        (
        echo NICK ${nick}
        echo USER ${nick} 8 * : ${nick}
        sleep 5
        echo "JOIN ${channel}"
        sleep 10
        echo "NOTICE ${channel} :${message}"
        echo QUIT
        ) | openssl s_client -connect ${ircServer}
    """
}

/**
 *
 * @param openshiftProject name of openshift namespace/project.
 * @param nodeName podName we are going to verify.
 * @return
 */
def verifyPod(String openshiftProject, String nodeName) {
    openshift.withCluster() {
        openshift.withProject(openshiftProject) {
            def describeStr = openshift.selector("pods", nodeName).describe()
            out = describeStr.out.trim()

            sh 'mkdir -p podInfo'

            writeFile file: 'podInfo/node-pod-description-' + nodeName + '.txt',
                    text: out
            archiveArtifacts 'podInfo/node-pod-description-' + nodeName + '.txt'

            timeout(60) {
                echo "Ensuring all containers are running in pod: ${env.NODE_NAME}"
                echo "Container names in pod ${env.NODE_NAME}: "
                names       = openshift.raw("get", "pod",  "${env.NODE_NAME}", '-o=jsonpath="{.status.containerStatuses[*].name}"')
                containerNames = names.out.trim()
                echo containerNames

                waitUntil {
                    def readyStates = openshift.raw("get", "pod",  "${env.NODE_NAME}", '-o=jsonpath="{.status.containerStatuses[*].ready}"')

                    echo "Container statuses: "
                    echo containerNames
                    echo readyStates.out.trim().toUpperCase()
                    def anyNotReady = readyStates.out.trim().contains("false")
                    if (anyNotReady) {
                        echo "One or more containers not ready...see above message ^^"
                        return false
                    } else {
                        echo "All containers ready!"
                        return true
                    }
                }
            }
        }
    }
}

/**
 * Library to set default environmental variables. Performed once at start of Jenkinsfile
 * @param envMap: Key/value pairs which will be set as environmental variables.
 * @return
 */
def setDefaultEnvVars(Map envMap=null){
    // If we've been provided an envMap, we set env.key = value
    // Note: This may overwrite above specified values.
    envMap.each { key, value ->
        env."${key.toSTring().trim()}" = value.toString().trim()
    }
}

/**
 * Library to set stage specific environmental variables.
 * @param stage - Current stage
 * @return
 */
def setStageEnvVars(String stage){
    def stages =
            ["test-contra-env-sample-project"       : [
                    PROJECT_REPO        : env.PROJECT_REPO,
                    PR_NUM              : env.PR_NUM,
                    AUTHOR_REPO_URL     : env.AUTHOR_REPO_URL,
                    SOURCE_BRANCH       : env.SOURCE_BRANCH,
            ],
            ]

    // Get the map of env var keys and values and write them to the env global variable
    if(stages.containsKey(stage)) {
        stages.get(stage).each { key, value ->
            env."${key}" = value
        }
    }
}

/**
 * Wrap the pipeline with timestamps and ansiColor
 * @param body Pipeline goes in here
 */
def ciPipeline(Closure body) {
    ansiColor('xterm') {
        deleteDir()
        body()
    }
}

def handlePipelineStep(Map config, Closure body) {
    try {

        if (config.debug) {
            echo "Starting ${config.stepName}"
        }

        body()

    } catch (Throwable err) {

        echo err.getMessage()
        throw err

    } finally {

        if (config.debug) {
            echo "end of ${config.stepName}"
        }
    }
}


/**
 * Function to return the job name
 * @return
 */
def timedMeasurement() {
    return env.JOB_NAME
}