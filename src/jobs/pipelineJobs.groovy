import org.centos.contra.jobdsl.PipelineJob


def jobs = [
        'samplePipelineJob': [
                'git': ['branch': 'master', 'repoUrl': 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project.git'],
        ]
]

jobs.each { name, values ->
    def job = new PipelineJob(this, name)
    job.fedMsgTrigger('org.fedoraprojectb', 'fedora-fedmsg', ['check1': 'value1'])
    job.addGit(values['git'])
    job.logRotate()
}
