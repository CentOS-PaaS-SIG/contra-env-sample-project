import org.contra.sample.pipeline.SamplePipelineUtils

/**
 * A class of methods used in the Jenkinsfile pipeline.
 * These methods are wrappers around methods in the contra-env-sample-project library.
 */
class samplePipelineUtils implements Serializable {

    def samplePipelineUtils = new SamplePipelineUtils()
    def cimetrics

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

    /**
     * Method to set default environmental variables. Performed once at start of Jenkinsfile
     * @param envMap Key/value pairs which will be set as environmental variables.
     * @return
     */
    def setDefaultEnvVars(Map envMap = null) {
        samplePipelineUtils.setDefaultEnvVars(envMap)
    }

    /**
     * Method to set stage specific environmental variables.
     * @param stage Current stage
     * @return
     */
    def setStageEnvVars(String stage) {
        samplePipelineUtils.setStageEnvVars(stage)
    }


    /**
     * Wrap the pipeline with timestamps and ansiColor
     * @param body Pipeline goes in here
     */
    def ciPipeline(Closure body) {
        try {
            samplePipelineUtils.ciPipeline(body)
        } catch(e) {
            throw e
        } finally {
            //cimetrics.writeToInflux()
        }
    }

    def timedPipelineStep(Map config, Closure body) {
        def measurement = timedMeasurement()
        cimetrics.timed measurement, config.stepName, {
            samplePipelineUtils.handlePipelineStep(config, body)
        }
    }

    /**
     * Function to return the job name
     * @return
     */
    def timedMeasurement() {
        return "${influxDBPrefix()}_${samplePipelineUtils.timedMeasurement()}"
    }

    def influxDBPrefix() {
        return "contra-env-sample-project-ci-pipeline"
    }

}