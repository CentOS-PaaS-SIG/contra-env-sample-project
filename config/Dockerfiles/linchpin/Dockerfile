FROM fedora:latest

LABEL maintainer="rnester@redhat.com, cmays@redhat.com, ari@redhat.com"
LABEL version="0.0.1"
LABEL description="A container to set up infrastructure instances"

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
        libxslt-python \
        && dnf clean all

WORKDIR /root/

COPY ansible.cfg .ansible.cfg

WORKDIR /linchpin/

USER root

CMD ["tail", "-f", "/dev/null"]
