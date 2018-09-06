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
                external('src/jobs/*.groovy')
                removeAction('DELETE')
                additionalClasspath('src')
        }
    }
}
queue("seed")

