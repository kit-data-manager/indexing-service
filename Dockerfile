####################################################
# START GLOBAL DECLARATION
####################################################
ARG REPO_NAME_DEFAULT=indexing-service
ARG REPO_PORT_DEFAULT=8050
ARG SERVICE_ROOT_DIRECTORY_DEFAULT=/spring/
####################################################
# END GLOBAL DECLARATION
####################################################

####################################################
# Building java environment with git & python
####################################################
FROM eclipse-temurin:25-jdk AS build-env-java
LABEL maintainer=webmaster@datamanager.kit.edu
LABEL stage=build-env

# Install git and Python as additional requirement
RUN apt-get update && \
    apt-get upgrade --no-install-recommends --assume-yes && \
    apt-get install --no-install-recommends --assume-yes git && \
    apt-get install -y python3 python3-pip && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

####################################################
# Building service
####################################################
FROM build-env-java AS build-service-indexing
LABEL maintainer=webmaster@datamanager.kit.edu
LABEL stage=build-contains-sources

# Fetch arguments from above
ARG REPO_NAME_DEFAULT
ARG SERVICE_ROOT_DIRECTORY_DEFAULT

# Declare environment variables
ENV REPO_NAME=${REPO_NAME_DEFAULT}
ENV SERVICE_DIRECTORY=$SERVICE_ROOT_DIRECTORY_DEFAULT$REPO_NAME

# Create directory for repo
RUN mkdir -p /git/${REPO_NAME}
WORKDIR /git/${REPO_NAME}
COPY . .
RUN cp settings/application-docker.properties settings/application-default.properties

# Build service in given directory
RUN bash ./build4docker.sh $SERVICE_DIRECTORY

####################################################
# Runtime environment 4 indexing-service
####################################################
FROM eclipse-temurin:25-jdk AS run-service-indexing
LABEL maintainer=webmaster@datamanager.kit.edu
LABEL stage=run

# Fetch arguments from above
ARG REPO_NAME_DEFAULT
ARG REPO_PORT_DEFAULT
ARG SERVICE_ROOT_DIRECTORY_DEFAULT

# Declare environment variables
ENV REPO_NAME=${REPO_NAME_DEFAULT}
ENV SERVICE_DIRECTORY=${SERVICE_ROOT_DIRECTORY_DEFAULT}${REPO_NAME}
ENV REPO_PORT=${REPO_PORT_DEFAULT}

# Install Python
RUN apt-get update && \
    apt-get upgrade --no-install-recommends --assume-yes && \
    apt-get install -y python3 python3-pip python3-venv && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create venv
RUN python3 -m venv /opt/venv

# Adapt PATH
ENV PATH="/opt/venv/bin:$PATH"
    
# Install additional requirements
RUN pip3 install --no-cache-dir xmltodict==0.13.0 wget==3.2

# Copy service from build container
RUN mkdir -p ${SERVICE_DIRECTORY}
WORKDIR ${SERVICE_DIRECTORY}
COPY --from=build-service-indexing ${SERVICE_DIRECTORY} ./

ENV PYTHONIOENCODING=UTF-8

# Define repo port 
EXPOSE ${REPO_PORT}
ENTRYPOINT ["bash", "./run.sh"]
