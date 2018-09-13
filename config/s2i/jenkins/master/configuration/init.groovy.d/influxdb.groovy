import com.redhat.jenkins.plugins.ci.*
import com.redhat.jenkins.plugins.ci.messaging.*
import hudson.model.*
import hudson.security.*
import jenkins.model.*
import jenkins.security.s2m.*
import java.util.logging.Logger


def logger = Logger.getLogger("")

env = System.getenv()
if (env['ENABLE_INFLUXDB'] == 'true') {
    logger.info('Configuring Influxdb plugin')
    def influxdb = Jenkins.instance.getDescriptorByType(jenkinsci.plugins.influxdb.DescriptorImpl)

    try {
        influxdb.removeTarget(env['INFLUXDB_INSTANCE'])
        influxdb.save()
    } finally {
        logger.info("Cleaned up any existing localInflux targets.")
    }

    def route = env['INFLUXDB_ROUTE']
    if (route == 'none') {
        try {
            route = "oc get route influxdb -o jsonpath={.spec.host}".execute().text
        } catch (err) {
            logger.error("Failed to get influxdb route using the oc command: " + err.getMessage())
            throw err
        }
    }

    def target = new jenkinsci.plugins.influxdb.models.Target()

    target.description = env['INFLUXDB_INSTANCE']
    target.url = "http://${route}"
    target.username = env['INFLUXDB_USERNAME']
    target.password = env['INFLUXDB_PASSWORD']
    target.database = env['INFLUXDB_DATABASE']

    influxdb.addTarget(target)
    influxdb.save()
    logger.info("Influxdb Publisher configured")

}
