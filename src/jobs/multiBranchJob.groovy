multibranchPipelineJob('sampleMultiBranchPipeline') {
    branchSources {
        github {
            repoOwner('CentOS-PaaS-SIG')
            repository('contra-env-sample-project')
        }
    }
}

