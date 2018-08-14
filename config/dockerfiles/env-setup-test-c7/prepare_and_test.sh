#!/bin/bash

base_dir=/home
export ara_location=$(python -c "import os,ara; print(os.path.dirname(ara.__file__))")
export ANSIBLE_CALLBACK_PLUGINS=$ara_location/plugins/callbacks
export ANSIBLE_ACTION_PLUGINS=$ara_location/plugins/actions
export ANSIBLE_LIBRARY=$ara_location/plugins/modules

cd ${base_dir}
git clone https://github.com/dirgim/contra-env-setup.git

pushd ${PROJECT_REPO}

# sync repository
if [ -z ${PR_NUM} ]; then
  git fetch origin ${ACTUAL_COMMIT}
  git checkout FETCH_HEAD
else
  # PR was specified so we need to fetch it
  git fetch origin pull/${PR_NUM}/head:local-testing-branch
  git checkout local-testing-branch
fi

popd

/usr/bin/ansible-playbook -vv -i "localhost," ${base_dir}/contra-env-setup/playbooks/setup.yml -e user=root \
                          -e ansible_connection=local -e setup_playbook_hooks=true \
                          --extra-vars='{"hooks": ["/home/debug_vars.yml"]}'

python -m pytest ${base_dir}/test_contra_env_setup.py > ${base_dir}/env_setup.log
