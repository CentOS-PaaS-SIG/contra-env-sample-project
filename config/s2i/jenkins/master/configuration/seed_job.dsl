dslVar = System.getenv('DSL_JOB_REPO') ?: 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project2.git'

job("seed") {
    scm {
        git(
        dslVar,
        'jobDsl',
        {node -> node / 'extensions' << '' })
    }
    triggers {
        githubPush()
    }
    configure {
        it / builders << 'javaposse.jobdsl.plugin.ExecuteDslScripts'(plugin: "job-dsl@1.70") {
                sandbox('true')
                target('src/jobs/*.groovy')
                ignoreExisting('true')
                removedJobAction('DELETE')
                removedViewAction('DELETE')
                removedConfigFilesAction('DELETE')
                lookupStrategy('JENKINS_ROOT')
                additionalClasspath('src')
        }
    }
}
queue("seed")

