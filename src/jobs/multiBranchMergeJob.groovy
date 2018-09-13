import org.centos.contra.jobdsl.MultiBranchJob


jobName = 'sampleMultiBranchMerge2'
comment = "\\[merge\\]"
rOwner = 'CentOS-PaaS-SIG'
rName = 'contra-env-sample-project'

def m = new MultiBranchJob(this, jobName)
m.addGitHub(rName, rOwner)
m.addComment(comment)

