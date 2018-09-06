dslVar = System.getenv('DSL_JOB_REPO') ?: 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project2.git'

job("seed") {
    scm {
        git(
        dslVar,
        'jobDsl',
        {node -> node / 'extensions' << '' })
    }
    configure {
        it / builders << 'javaposse.jobdsl.plugin.ExecuteDslScripts' {
                targets('src/jobs/*.groovy')
                sandbox('true')
                usingScriptText('false')
                ignoreExisting('false')
                removedJobAction('DELETE')
                removedViewAction('DELETE')
                removedConfigFilesAction('DELETE')
                lookupStrategy('JENKINS_ROOT')
                additionalClasspath('src')
        }
    }
}
queue("seed")

