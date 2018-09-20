import org.centos.contra.jobdsl.PipelineJob


def job = new PipelineJob(this, 'samplePipelineJob')
job.fedMsgTrigger('org.fedoraprojectb', 'fedora-fedmsg', ['check1': 'value1'])
job.addGit([branch: 'master', repoUrl: 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project.git'])
job.logRotate()
