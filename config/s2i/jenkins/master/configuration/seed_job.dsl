dslVar = System.getenv('DSL_JOB_REPO') ?: 'https://github.com/CentOS-PaaS-SIG/contra-env-sample-project.git'

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
    steps {
        dsl {
            external('src/jobs/*.groovy')
            removeAction('DISABLE')
            additionalClasspath('lib')
        }
    }
}
queue("seed")

