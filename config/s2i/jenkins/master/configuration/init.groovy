import com.redhat.jenkins.plugins.ci.*
import com.redhat.jenkins.plugins.ci.messaging.*
import hudson.markup.RawHtmlMarkupFormatter
import hudson.model.*
import hudson.security.*
import hudson.security.csrf.DefaultCrumbIssuer
import hudson.util.Secret
import jenkins.model.*
import jenkins.plugins.git.GitSCMSource
import jenkins.plugins.git.traits.RefSpecsSCMSourceTrait
import jenkins.security.s2m.*
import jenkins.security.s2m.*
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever

import java.util.logging.Logger

def logger = Logger.getLogger("")
logger.info("Disabling CLI over remoting")
jenkins.CLI.get().setEnabled(false);
logger.info("Enable Slave -> Master Access Control")
Jenkins.instance.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
// Set global read permission
def strategy = Jenkins.instance.getAuthorizationStrategy()
strategy.add(hudson.model.Hudson.READ,'anonymous')
Jenkins.instance.setAuthorizationStrategy(strategy)
// Set Markup Formatter to Safe HTML so PR hyperlinks work
Jenkins.instance.setMarkupFormatter(new RawHtmlMarkupFormatter(false))

logger.info("Enabling CSRF Protection")
Jenkins.instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

logger.info("Disabling deprecated agent protocols (only JNLP4 is enabled)")
Set<String> agentProtocolsList = ['JNLP4-connect']
Jenkins.instance.setAgentProtocols(agentProtocolsList)

Jenkins.instance.save()

logger.info("Setting Time Zone to be EST")
System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'America/New_York')

// Add global variable
instance = Jenkins.getInstance()
globalNodeProperties = instance.getGlobalNodeProperties()
envVarsNodePropertyList = globalNodeProperties.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)

newEnvVarsNodeProperty = null
envVars = null

if ( envVarsNodePropertyList == null || envVarsNodePropertyList.size() == 0 ) {
    newEnvVarsNodeProperty = new hudson.slaves.EnvironmentVariablesNodeProperty()
    globalNodeProperties.add(newEnvVarsNodeProperty)
    envVars = newEnvVarsNodeProperty.getEnvVars()
} else {
    envVars = envVarsNodePropertyList.get(0).getEnvVars()

}

envVars.put("GIT_SSL_NO_VERIFY", "true")
instance.save()

logger.info("Configuring Global Pipeline Libraries")
def sharedLibConfigs = [
        new Tuple(
                "contra-library",
                "https://github.com/CentOS-PaaS-SIG/contra-env-sample-project",
                ["+refs/heads/*:refs/remotes/@{remote}/*", "+refs/merge-requests/*/head:refs/remotes/@{remote}/merge-requests/*"]
        )
]

// remove existing libraries to make sure we always use the latest configuration coming from this file
// this will only remove libraries with matching names; any other libraries will stay untouched
GlobalLibraries.get().getLibraries().removeAll() { lib ->
    lib.name in sharedLibConfigs.collect { it.get(0) }
}

sharedLibConfigs.each { libConfig ->
    String libName = libConfig.get(0)
    logger.info("Adding Global Pipeline library '${libName}'")
    String gitUrl = libConfig.get(1)
    GitSCMSource source= new GitSCMSource(libName, gitUrl, null, null, null, false)
    String[] refSpecs = libConfig.get(2)
    if (refSpecs) {
        RefSpecsSCMSourceTrait refspecs = new RefSpecsSCMSourceTrait(refSpecs)
        source.setTraits([refspecs])
    }
    LibraryConfiguration lib = new LibraryConfiguration(libName, new SCMSourceRetriever(source))
    lib.implicit = false
    lib.defaultVersion = "master"
    GlobalLibraries.get().getLibraries().add(lib)
}