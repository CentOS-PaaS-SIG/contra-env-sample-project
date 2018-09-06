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
  steps {
      dsl {
          external('jobs/*.groovy')
          removeAction('DELETE')
          additionalClasspath('src')
      }   
  }
}
queue("seed")

