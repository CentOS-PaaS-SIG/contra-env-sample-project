dslVar = System.getenv('DSL_JOB_REPO') ?: 'CentOS-PaaS-SIG/contra-env-sample-project'
contraLib = 'openshift/contra-lib'
contraBranch = 'master'

dslVarTarget = dslVar.split('/')[1]
contraLibTarget = contraLib.split('/')[1]

job("seed") {
    properties {
        githubProjectUrl("https://github.com/${dslVar}")
    }
    multiscm {
        git {
            remote {
                github(dslVar)

            }
            extensions {
                relativeTargetDirectory(dslVarTarget)
            }
        }   
        git {
            remote {
                github(contraLib)
            }   
            branches(contraBranch)
            extensions {
                relativeTargetDirectory(contraLibTarget)
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

