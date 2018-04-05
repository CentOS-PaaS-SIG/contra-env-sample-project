import org.contra.sample.pipeline.SamplePipelineUtils

/**
 * A class of methods used in the Jenkinsfile pipeline.
 * These methods are wrappers around methods in the ci-pipeline library.
 */
class samplePipelineUtils implements Serializable {

    def samplePipelineUtils = new SamplePipelineUtils()

    /**
     *
     * @param openshiftProject name of openshift namespace/project.
     * @param nodeName podName we are going to get container logs from.
     * @return
     */
    def getContainerLogsFromPod(String openshiftProject, String nodeName) {
        samplePipelineUtils.getContainerLogsFromPod(openshiftProject, nodeName)
    }

    def verifyPod(openshiftProject, nodeName) {
        samplePipelineUtils.verifyPod(openshiftProject, nodeName)
    }

    def executeInContainer(stageName, containerName, script) {
        samplePipelineUtils.executeInContainer(stageName, containerName, script)
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
        samplePipelineUtils.sendIRCNotification(nick, channel, message, ircServer)
    }
}