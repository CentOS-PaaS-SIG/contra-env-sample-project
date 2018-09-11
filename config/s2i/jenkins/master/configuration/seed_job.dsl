dslVar = 'joejstuart/contra-env-sample-project'
contraLib = 'joejstuart/contra-lib'
// contraBranch = 'refs/tags/v0.0.2'
contraBranch = 'packageUpdate'

dslVarTarget = dslVar.split('/')[1]
contraLibTarget = contraLib.split('/')[1]

job("seed") {
    multiscm {
        git {
            remote {
                github(dslVar)

            }
            branches("commentSamples")
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

