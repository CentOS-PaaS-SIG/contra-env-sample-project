multibranchPipelineJob('testPipeline') {
    branchSources {
        github {
            scanCredentialsId('joejstuart - github')
            repoOwner('joejstuart')
            repository('cloud-image-builder')
        }
    }
}

