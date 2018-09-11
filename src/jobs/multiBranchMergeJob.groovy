import org.centos.sample.jobdsl.MultiBranch


jobName = 'sampleMultiBranchMerge'
comment = '\[merge\]'
owner = 'CentOS-PaaS-SIG'
repo = 'contra-env-sample-project'

multibranchPipelineJob(jobName) {
    branchSources {
        github {
            repoOwner(owner)
            repository(repo)
        }
    }   
    configure MultiBranch.commentTrigger(comment)
}

