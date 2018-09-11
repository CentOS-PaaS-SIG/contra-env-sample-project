dslVar = 'CentOS-PaaS-SIG/contra-env-sample-project'
contraLib = 'openshift/contra-lib'

dslVarTarget = dslVar.split('/')[1]
contraLibTarget = contraLib.split('/')[1]

job("seed") {
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
            branches('refs/tags/v0.0.2')
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
            external('src/jobs/*.groovy')
            removeAction('DISABLE')
            additionalClasspath(["${contraLibTarget}/src", "${dslVarTarget}/src"].join("\n")) 
        }
    }
}

