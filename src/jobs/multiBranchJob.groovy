multibranchPipelineJob('testPipeline') {
    branchSources {
        github {
            scanCredentialsId('contra-sample-project-github')
            repoOwner('CentOS-PaaS-SIG')
            repository('cloud-image-builder')
        }
    }
}

