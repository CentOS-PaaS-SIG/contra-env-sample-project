FROM centos:7
MAINTAINER "Ari LiVigni" <ari@redhat.com>
LABEL maintainer "https://github.com/CentOS-PaaS-SIG/contra-env-sample-project"
LABEL description="This container is meant to boot a qcow2 image"

ENV container docker
ENV HOME=/home

WORKDIR $HOME

RUN yum -y install epel-release

RUN yum -y install libguestfs libguestfs-tools-c \
                   git libvirt-client \
                   PyYAML python2-setuptools virt-install \
                   python-pip python-devel gcc net-tools \
                   openssh-clients sshpass && yum clean all

RUN yum -y update && yum clean all
RUN yum -y install systemd
RUN yum -y install libvirt-daemon-driver-* libvirt-daemon libvirt-daemon-kvm qemu-kvm libvirt-daemon-config-network && yum clean all; \
(cd /lib/systemd/system/sysinit.target.wants/; for i in *; do [ $i == systemd-tmpfiles-setup.service ] || rm -f $i; done); \
rm -f /lib/systemd/system/multi-user.target.wants/*;\
rm -f /etc/systemd/system/*.wants/*;\
rm -f /lib/systemd/system/local-fs.target.wants/*; \
rm -f /lib/systemd/system/sockets.target.wants/*udev*; \
rm -f /lib/systemd/system/sockets.target.wants/*initctl*; \
rm -f /lib/systemd/system/basic.target.wants/*;\
rm -f /lib/systemd/system/anaconda.target.wants/*; \
systemctl enable libvirtd; \
systemctl enable virtlockd

RUN pip install ara

COPY execute.sh $HOME/
COPY default.xml /etc/libvirt/qemu/networks/

RUN mkdir -p $HOME/images/
RUN if [ "${ENABLE_VM}" == "true" ]; then echo "ENABLE_VM: true"; curl -o $HOME/images/latest-atomic.qcow2 ${VM_IMG_URL}; else echo "ENABLE_VM: false"; echo "NOT Pulling VM cloud image"; fi

# Edit the service file which includes ExecStartPost to chmod /dev/kvm
RUN sed -i "/Service/a ExecStartPost=\/bin\/chmod 666 /dev/kvm" /usr/lib/systemd/system/libvirtd.service

VOLUME [ "/sys/fs/cgroup"]
CMD ["/usr/sbin/init"]