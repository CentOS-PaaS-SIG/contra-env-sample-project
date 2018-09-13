import org.centos.contra.jobdsl.MultiBranchJob


jobName = 'MultiBranchWithCommentTrigger'
comment = "\\[test\\]"
rOwner = 'CentOS-PaaS-SIG'
rName = 'contra-env-sample-project'

def m = new MultiBranchJob(this, jobName)
m.addGitHub(rName, rOwner)
m.addComment(comment)
m.discardOldBranches()
