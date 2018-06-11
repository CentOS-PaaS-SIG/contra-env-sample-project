FROM fedora:28

LABEL name="contrainfra/linchpin-executor" \
      maintainer="rnester@redhat.com, cmays@redhat.com, ari@redhat.com" \
      version="0.0.4" \
      description="A container to set up infrastructure instances"

ENV APP_ROOT=/linchpin/
ENV PATH=${APP_ROOT}/bin:${PATH} HOME=${APP_ROOT}


COPY ansible.cfg /etc/ansible/ansible.cfg
COPY bin/ ${APP_ROOT}/bin/

RUN dnf install -y git \
        wget \
        libffi-devel \
        openssl-devel \
        libyaml-devel \
        gmp-devel \
        python-devel \
        gmp-devel \
        libselinux-python \
        make \
        gcc \
        libxml2-python \
        libxslt-python && \
    dnf clean all && \
    pip install git+git://github.com/CentOS-PaaS-SIG/linchpin.git#egg=linchpin[beaker] && \
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