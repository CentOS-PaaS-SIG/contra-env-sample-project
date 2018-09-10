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
    configure {
        it / sources / 'data' / 'jenkins.branch.BranchSource' << {
            strategy(class: 'jenkins.branch.DefaultBranchPropertyStrategy') {
                properties(class: 'java.util.Arrays$ArrayList') {
                    a(class: 'jenkins.branch.BranchProperty-array') {
                        'com.adobe.jenkins.github__pr__comment__build.TriggerPRCommentBranchProperty' {
                            commentBody(comment)
                        }   
                    }
                }
            }
        }
    }
}

