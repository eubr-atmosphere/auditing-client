FROM openjdk:8

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update -y && \
  apt-get upgrade -y && \
  apt-get install -y build-essential && \
  apt-get install openjfx -y && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget maven && \
  apt-get install -y net-tools iputils-ping && \
  rm -rf /var/lib/apt/lists/*

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Installing Common
RUN \
  git clone https://github.com/fogbow/common.git && \
  (cd common && git checkout develop && mvn install -Dmaven.test.skip=true)

# Installing client
RUN \
  git clone https://github.com/eubr-atmosphere/auditing-client.git && \
  (cd auditing-client && mvn install -Dmaven.test.skip=true)

# Define working directory.
WORKDIR /root/auditing-client

RUN \
  mvn dependency:sources