job("seed") {
  scm {
      git(
      "${DSL_JOB_REPO}",
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

