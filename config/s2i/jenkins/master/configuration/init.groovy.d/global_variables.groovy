import com.redhat.jenkins.plugins.ci.*
import com.redhat.jenkins.plugins.ci.messaging.*
import hudson.model.*
import hudson.security.*
import jenkins.model.*
import jenkins.plugins.git.GitSCMSource
import jenkins.plugins.git.traits.RefSpecsSCMSourceTrait
import jenkins.security.s2m.*
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import java.util.logging.Logger
import groovy.io.FileType
import org.json.JSONObject


def logger = Logger.getLogger("")

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
// Get list of all sharedLib files
def sharedLibDir = new File("/var/lib/jenkins/init.groovy.d/sharedLibConfigs/")
sharedLibDir.eachFileRecurse (FileType.FILES) { libConfig ->
    JSONObject libVals = new JSONObject(libConfig.getText())
    String libName = libVals['name']
    logger.info("Removing existing global library '${libName}'")
    // Remove existing library with this name to ensure latest configuration
    GlobalLibraries.get().getLibraries().removeAll() { lib ->
        lib.name == libName
    }
    logger.info("Adding Global Pipeline library '${libName}'")
    String gitUrl = libVals['url']
    GitSCMSource source= new GitSCMSource(libName, gitUrl, null, null, null, false)
    // This refspec stuff likely needs to be investigated further
    String refSpecs = libVals['refspec']
    if (refSpecs) {
        RefSpecsSCMSourceTrait refspecs = new RefSpecsSCMSourceTrait(refSpecs)
        source.setTraits([refspecs])
    }
    LibraryConfiguration lib = new LibraryConfiguration(libName, new SCMSourceRetriever(source))
    lib.implicit = libVals.opt('implicit') ?: false
    lib.defaultVersion = libVals.opt('branch') ?: "master"
    GlobalLibraries.get().getLibraries().add(lib)
}
