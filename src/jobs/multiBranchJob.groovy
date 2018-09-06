multibranchPipelineJob('testPipeline') {
    branchSources {
        github {
            scanCredentialsId('contra-sample-project-github')
            repoOwner('joejstuart')
            repository('cloud-image-builder')
        }
    }
}

