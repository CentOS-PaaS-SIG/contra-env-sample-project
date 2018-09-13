import org.centos.contra.jobdsl.MultiBranchJob


def jobs = [
        'MultiBranchWithCommentTrigger': [
                'comment': "\\[test\\]",
                'owner': 'CentOS-PaaS-SIG',
                'name': 'contra-env-sample-project'
        ],
        'BasicMutliBranchJob': [
                'owner': 'CentOS-PaaS-SIG',
                'name': 'ci-pipeline'
        ]
]

jobs.each { name, values ->
    def job = new MultiBranchJob(this, name)
    job.addGitHub(values['name'], values['owner'])
    if (values['comment']) {
        job.addComment(values['comment'])
    }
    job.discardOldBranches()
}
