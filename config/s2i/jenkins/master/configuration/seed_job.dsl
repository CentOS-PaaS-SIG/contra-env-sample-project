dslVar = System.getenv('DSL_JOB_REPO') ?: 'CentOS-PaaS-SIG/contra-env-sample-project'
dslBranch = System.getenv('DSL_REPO_BRANCH') ?: 'master'
contraLib = 'openshift/contra-lib'
contraBranch = 'master'

dslVarTarget = dslVar.split('/')[1]
contraLibTarget = contraLib.split('/')[1]

dslJob = 'dsl_seed'

job(dslJob) {
    multiscm {
        git {
            remote {
                github(contraLib)
            }   
            branches(contraBranch)
            extensions {
                relativeTargetDirectory(contraLibTarget)
            }   
        }   
        git {
            remote {
                github(dslVar)

            }
            branches(dslBranch)
            extensions {
                relativeTargetDirectory(dslVarTarget)
            }
        }   
    }   
    triggers {
        githubPush()
    }
    steps {
        dsl {
            external("${dslVarTarget}/src/jobs/*.groovy")
            removeAction('DISABLE')
            additionalClasspath(["${contraLibTarget}/src", "${dslVarTarget}/src"].join("\n")) 
        }
    }
}

queue(dslJob)

