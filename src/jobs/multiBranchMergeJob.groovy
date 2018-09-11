import org.centos.jobdsl.multiBranchJob


jobName = 'sampleMultiBranchMerge'
comment = "\\[merge\\]"
rOwner = 'CentOS-PaaS-SIG'
rName = 'contra-env-sample-project'

multibranchPipelineJob(jobName) {
    branchSources {
        github {
            repoOwner(rOwner)
            repository(rName)
        }
    }   
    configure multiBranchJob.commentTrigger(comment)
}

