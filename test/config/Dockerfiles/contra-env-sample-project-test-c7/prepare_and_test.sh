#!/bin/bash

base_dir=/home

# Set log dir to the WORKSPACE where it can be archived
if [ ! -z "${WORKSPACE}" ]; then
    log_dir=${WORKSPACE}
else
    log_dir=${base_dir}/logs
fi

# Set environment to use ara with ansible
export ara_location=$(python -c "import os,ara; print(os.path.dirname(ara.__file__))")
export ANSIBLE_CALLBACK_PLUGINS=$ara_location/plugins/callbacks
export ANSIBLE_ACTION_PLUGINS=$ara_location/plugins/actions
export ANSIBLE_LIBRARY=$ara_location/plugins/modules

export USER=$(whoami)

cd ${base_dir}

# Prepare repo and logs directories
mkdir -p ${log_dir}

git clone https://github.com/CentOS-PaaS-SIG/contra-env-setup.git

# Run the playbook locally with added hook for debugging variables needed for testing
/usr/bin/ansible-playbook -vv -i "localhost," ${base_dir}/contra-env-setup/playbooks/setup.yml -e user=root \
                          -e ansible_connection=local -e setup_nested_virt=false -e setup_playbook_hooks=true \
                          -e project_repo=${AUTHOR_REPO_URL} -e project_branch=${SOURCE_BRANCH} \
                          -e start_minishift=true --extra-vars='{"hooks": ["/home/debug_vars.yml"]}'

# Run tests with pytest
python -m pytest ${base_dir}/test_contra_env_sample_project.py -v --junitxml=${log_dir}/contra_env_sample_project_centos7.xml > ${log_dir}/contra_env_sample_project_centos7.log
