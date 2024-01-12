# Indexing-service

[![Build Status](https://github.com/kit-data-manager/indexing-service/actions/workflows/gradle.yml/badge.svg)](https://github.com/kit-data-manager/indexing-service/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/kit-data-manager/indexing-service/graph/badge.svg)](https://codecov.io/gh/kit-data-manager/indexing-service)
[![CodeQL](https://github.com/kit-data-manager/indexing-service/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/kit-data-manager/indexing-service/actions/workflows/codeql-analysis.yml)
![License](https://img.shields.io/github/license/kit-data-manager/indexing-service.svg)
[![docker]](https://github.com/kit-data-manager/indexing-service/pkgs/container/indexing-service)
[![currentVersion]](https://github.com/kit-data-manager/indexing-service/pkgs/container/indexing-service)
[![size]](https://github.com/kit-data-manager/indexing-service/pkgs/container/indexing-service)


[docker]: <https://ghcr-badge.egpl.dev/kit-data-manager/indexing-service/tags?trim=major&color=steelblue&ignore=main,latest&label=docker versions>
[currentVersion]: <https://ghcr-badge.egpl.dev/kit-data-manager/indexing-service/latest_tag?trim=major&color=steelblue&label=current version>
[size]: <https://ghcr-badge.egpl.dev/kit-data-manager/indexing-service/size?color=steelblue&label=size>


:information_source:
The service should not be directly accessible from the outside. 

:warning:
For mapping documents only Gemma is available currently!

A standalone service which receives messages about changes in metadata documents maps them to a common format and ingests them into elasticsearch.

![Visualization of use case structure.](use-case.drawio.svg)


## How to build

Dependencies that are needed to build and are not being downloaded via gradle:

- OpenJDK 17 or higher
```
$ bash build.sh PATH/TO/INSTALLATION/DIR
```
## How to start
```
$ PATH/TO/INSTALLATION/DIR/run.sh
```

### Prerequisites

You might want to take a look at testbed4inf, which should make it easy to satisfy those.

- Python3
  - PIP
  - xmltodict
  - wget
- a running RabbitMQ instance 
- a running elasticsearch instance

### Setup using Docker
#### Install Python for Gemma
```
sudo apt-get install --assume-yes python3 python3-pip 
pip3 install xmltodict wget
```

#### Install and Start Network
```
docker network create network4datamanager
```

#### Install and Start Elasticsearch
```
docker pull elasticsearch:7.9.3
docker run -d --name elasticsearch4metastore  -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.9.3
```

#### Install and Start RabbitMQ
```
docker run -d --hostname rabbitmq --net network4datamanager --name rabbitmq4docker -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```
#### Managing Services
To start/stop a single service just type
```
docker start/stop name_of_container
```
e.g.:
```
docker stop elasticsearch4metastore
```


## More information

## License

See [LICENSE file in this repository](LICENSE).
