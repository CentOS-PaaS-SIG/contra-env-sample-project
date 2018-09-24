# contra-env-sample-project
This is a sample project to use for OpenShift s2i Templates and Jenkins Pipelines

## Job DSL Support
This project by default will create a freestyle job called "dsl_seed" that is meant to load your pipeline repo along with 
[openshift/contra-lib](https://github.com/openshift/contra-lib). Contra-lib is loaded to provide library support. To use the dsl_seed job,
follow these steps.
#### Create the directories 

```bash
mkdir -p your-pipeline-repo/src/jobs
```
#### Add a script to the jobs directory

This example creates a job called samplePipelineJob and leverages the class PipelineJob in [openshift/contra-lib](https://github.com/openshift/contra-lib). 

```groovy
import org.centos.contra.jobdsl.PipelineJob


def job = new PipelineJob(this, 'samplePipelineJob')
job.fedMsgTrigger('org.fedoraprojectb', 'fedora-fedmsg', ['check1': 'value1'])
job.addGit([branch: 'master', repoUrl: 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project.git'])
job.logRotate()

```
#### Install Jenkins

Use the provided OpenShift template and provide the link to your pipeline repo.

```bash
oc new-app -f jenkins-persistent.yml -p DSL_JOB_REPO=githubOrg/pipeline-repo
```

## Custom Shared Libraries
This project adds contra-lib and contra-library as shared libraries to the Jenkins master automatically.
However, the user can change which shared libraries are added.
To do so, navigate to [sharedLibConfigs](https://github.com/CentOS-PaaS-SIG/contra-env-sample-project/blob/master/config/s2i/jenkins/master/configuration/init.groovy.d/sharedLibConfigs).
Each json file represents a shared library.
Any number of json files can be added or removed from this directory.
Supported fields include the name of the library (required), the url to it (required), and the following optional fields: refspec, implicit, and branch.
