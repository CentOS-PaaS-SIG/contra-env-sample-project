import com.redhat.jenkins.plugins.ci.*
import com.redhat.jenkins.plugins.ci.messaging.*
import hudson.model.*
import hudson.security.*
import jenkins.model.*
import jenkins.security.s2m.*
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import java.util.logging.Logger


def logger = Logger.getLogger("")
env = System.getenv()
if (env['LOAD_SEED_JOB'] == 'true') {
    logger.info('Disabling job dsl script security')
    GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).useScriptSecurity=false
    def JENKINS_SEED_JOB = env['JENKINS_SEED_JOB'] ?: "${env['JENKINS_HOME']}/seed_job.dsl"
    def config = new File(JENKINS_SEED_JOB).text

    def workspace = new File("${env['JENKINS_HOME']}")

    def jobManagement = new JenkinsJobManagement(System.out, [:], workspace)
    new DslScriptLoader(jobManagement).runScript(config)
    logger.info('Created seed job')
}
