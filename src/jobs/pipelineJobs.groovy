import org.centos.contra.jobdsl.PipelineJob
import org.centos.contra.jobdsl.Triggers


def jobs = [
        'samplePipelineJob': [
                'git': ['branch': 'master', 'repoUrl': 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project.git'],
        ]
]

jobs.each { name, values ->
    def job = new PipelineJob(this, name)
    def jobTrigger = Triggers.fedMsgTrigger('org.fedoraproject', 'fedora-fedmsg', ['check1': 'value1'])
    job.trigger(jobTrigger)
    job.addGit(values['git'])
    job.logRotate()
}
