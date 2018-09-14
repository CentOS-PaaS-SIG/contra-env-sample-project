FROM fedora:28

LABEL name="contrainfra/ansible-executor" \
      maintainer="rnester@redhat.com, cmays@redhat.com, ari@redhat.com" \
      version="0.0.5" \
      description="A container to run test scripts, set resource configuration, etc."

ENV APP_ROOT=/ansible/
ENV PATH=${APP_ROOT}/bin:${PATH} HOME=${APP_ROOT}

COPY ansible.cfg /etc/ansible/ansible.cfg
COPY bin/ ${APP_ROOT}/bin/

RUN dnf install -y ansible \
        fedpkg \
        libselinux-python \
        standard-test-roles \
        python-boto \
        python-boto3 \
        python2-libcloud \
        python3-libcloud \
	python2-virtualenv \
        rsync && \
    dnf clean all && \
    mkdir -p ${APP_ROOT} && \
    chmod -R u+x ${APP_ROOT}/bin && \
    chgrp -R 0 ${APP_ROOT} && \
    chmod -R g=u ${APP_ROOT} /etc/passwd

### Containers should NOT run as root as a good practice
USER 10001
WORKDIR ${APP_ROOT}

### user name recognition at runtime w/ an arbitrary uid - for OpenShift deployments
ENTRYPOINT [ "uid_entrypoint" ]

CMD run
