FROM quay.io/ukhomeofficedigital/openjdk8

ENV USER user_sls_data
ENV GROUP group_sls_data
ENV NAME sls-data
ENV JAR_PATH build/libs

RUN yum update -y glibc && \
    yum update -y nss && \
    yum update -y bind-license

WORKDIR /app

RUN groupadd -r ${GROUP} && \
    useradd -r -g ${GROUP} ${USER} -d /app && \
    mkdir -p /app && \
    chown -R ${USER}:${GROUP} /app

COPY ${JAR_PATH}/${NAME}*.jar /app

ADD scripts /app/scripts
ADD data /app/data

RUN chmod a+x /app/scripts/*

EXPOSE 8080

USER ${USER}

CMD /app/scripts/start.sh
