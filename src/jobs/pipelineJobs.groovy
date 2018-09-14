import org.centos.contra.jobdsl.PipelineJob


def jobs = [
        'samplePipelineJob': [
                'git': ['branch': 'master', 'repoUrl': 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project.git'],
                'ciEvent': ['name': '^kernel$']
        ]
]

jobs.each { name, values ->
    def job = new PipelineJob(this, name)
    job.ciEvent(values['ciEvent'])
    job.addGit(values['git'])
    job.logRotator()
}
