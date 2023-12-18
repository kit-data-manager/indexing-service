# Interfaces Overview (Indexing-Service)

This document aims to answer questions on how to configure external dependencies and which public interfaces are offered by Indexing-Service in a comprehensive way. It is meant to be used for getting an overview and guidance in addition to the official documentation, which is available at the official [Indexing-Service Web page (not yet available)](https://kit-data-manager.github.io/webpage/indexing-service/).How can this software be connected with other software? This is the question this document aims to answer.

> ℹ️ **Note:** 
> This document applies to the Indexing-Service version it is shipped with. If you have a specific version running, please refer to `INTERFACE.md` of this particular release.

## TOC

- [Interfaces Overview for Indexing-Service](#interfaces-overview-for-indexing-service)
   * [External Dependencies](#external-dependencies-) 📤
      + [Relational Database (mandatory)](#relational-database-mandatory-) ⛁
      + [Local Filesystem (mandatory)](#local-filesystem-mandatory-) 📂
      + [Messaging (mandatory)](#messaging-mandatory-) 💬
      + [Search (mandatory)](#search-mandatory-) 🔍
      + [Access Control (optional)](#access-control-optional-) 🔐
   * [Public Interfaces](#public-interfaces-) 📥
      + [HTTP / REST](#http--rest)
      + [Monitoring Health/Info](#monitoring-healthinfo) 🔍
      
## Requirements
- [Messaging](#messaging-optional-)
- [Elasticsearch](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/framework/setup-elasticsearch.html)
Messaging is used to inform indexing service about any updates regarding metadata documents. Indexing service will fetch the document, transform it (if mapping available) and send it to elasticsearch for indexing.

## External Dependencies 📤

External dependencies are third-party services that are required for Indexing-Service to work properly or that can be added optionally to provide additional functionality. Typically, external dependencies require additional software to be installed and configured, before they can be included in the Indexing-Service configuration, which is typically done via the main configuration file `application.properties`. If you do not want to lose your default settings, we recommend that you make a copy of "application.properties" and move it to the "config" subfolder. Remove all properties you want to keep from the new file. **All properties in "config/application.properties" override the settings in "application.properties".**


## External Dependencies 📤

External dependencies are third-party services that are required for Indexing-Service to work properly or that can be added optionally to provide additional functionality. Typically, external dependencies require
additional software to be installed and configured, before they can be included in the Indexing-Service configuration, which is should be done via the  configuration file `config/application.properties`.

### Relational Database (mandatory) ⛁
A relational database is required by Indexing-Service to store administrative metadata for mapping documents. If not configured properly, Indexing-Service will fail to start.

#### Configuration ⚙️
 - H2 In-Memory (driver included, used for **testing only**, **not** recommended for **production**) [Example](https://github.com/kit-data-manager/indexing-service/blob/v0.9.0/src/test/resources/test-config/application-test.properties#L14-L18)
 - H2 File-Based (driver included, used for basic Docker setup or for 'quick and dirty' tests, **not** recommended for **production**) [Example](https://github.com/kit-data-manager/indexing-service/blob/v0.9.0/settings/application-docker.properties#L26-L33)
 - Postgres (driver included, **requires a running PostgreSQL server**, **recommended for production**) [PostgreSQL](https://www.postgresql.org/), [Example](https://github.com/kit-data-manager/indexing-service/blob/v0.9.0/settings/application-postgres.properties#L34-L47)

> ℹ️ **Note:** 
> Other relational databases, such as MariaDB, SQLite, or Oracle, may also work, but require additional steps. To allow Indexing-Service to connect, the source code repository must be cloned, an appropriate JDBC driver has to be added to `build.gradle` and Indexing-Service has to be compiled. Proper JDBC drivers are typically provided on the database's web page. Afterwards, the database can be configured in `config/application.properties` similar to PostgreSQL but with database-specific property naming. Please refer to the driver documentation for details.

### Local Filesystem (mandatory) 📂
Indexing-Service requires access to the local file system in order to store and manage uploaded mapping documents. Indexing-Service requires access to one folder, which can be located on the local hard drive or mounted via NFS. It's recommended to create a subfolder named 'mapping' in the installation directory.


#### Configuration ⚙️
 - see `application.properties` 
     - [Configure folders](https://github.com/kit-data-manager/indexing-service/blob/v0.9.0/settings/application-default.properties#L71-L74)

> ℹ️ **Note:** 
> The file path to the folder has to start with three '/'. If you overwrite the default setting we recommend to create or edit 'config/application.properties'. e.g.: 
> ``` title= config/application.properties
> metastore.indexer.mappingsLocation:file:///data/indexing/mapping
>```
> **If the folder do not exist, it will be created.**

### Messaging (mandatory) 💬
AMQP-based messaging is a mandatory feature of Indexing-Service, which allows Indexing-Service to **receive** messages about creation, modification, and deletion events inside MetaStore related to **metadata documents**  and process them in an asynchronous way.
#### Installation
- [Installing RabbitMQ](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/framework/setup-rabbitMq.html)
#### Configuration ⚙️
 - [RabbitMQ](https://www.rabbitmq.com/) (dependencies included, serves as messaging distributor, requires a running RabbitMQ server) 
     - [Introduction Messaging for MetaStore](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/messaging/messaging-introduction.html)
     - [Configuration Messaging for Indexing-Service](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/messaging/messaging-configuration.html)
     - [Example](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/setup-metastore.html#rabbitmq) 

### Enhanced Search (mandatory) 🔍
Indexing-Service requires an [elasticsearch instance](https://www.elastic.co/de/elasticsearch/) for indexing received metadata documents. (JSON metadata documents are provided by default. XML documents should be transformed to JSON using GEMMA (currently the only available mapping)) (In future versions [mapping-service](https://github.com/kit-data-manager/mapping-service) should be integrated.)
#### Configuration ⚙️
- [Configuration Receiver](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/setup-metastore.html#elasticsearch)
 
### Access Control (optional) 🔐
By default, Indexing-Service itself is open for all kinds of operations, i.e., read and write, where write access should be restricted on the user interface level, e.g., by a password-protected area for critical operations. Optionally, authentication and authorization via JSON Web Tokens (JWT) issued by a Keycloak instance, can be configured, which allows a fine-granulated access management on document level.
> ℹ️ **Note:** 
> As Indexing-Service is not planned to be used by humans, no access control is used by default. Therefor the service should not be accessible from outside KIT. Managing mappings should be done by an administrator.

#### Requirements
- [Keycloak](https://www.keycloak.org/)
#### Configuration ⚙️
- [Setup Indexing-Service](https://kit-data-manager.github.io/webpage/metastore/documentation/installation/setup-metastore.html#keycloak)
Configuration is similar to MetaStore.

## Public Interfaces 📥
Public Interfaces are used to access Indexing-Service in order to obtain its contents, typically this happens via HTTP/REST. Depending on the interface, special clients or protocols must be used to access a specific public interface.


Interfaces which trigger functionality or return information **on request**.

### HTTP / REST
A REST interface which allows to access the service functionality, like creating/registering, updating and validating metadata documents/schemas on request.
#### Documentation 📖
 - [OpenAPI](http://localhost:8050/swagger-ui.html) via running instance.
 - [Usage with Examples](https://github.com/kit-data-manager/indexing-service/blob/main/restDocu.md)
    
#### Services known to connect to this interface:
-/-

### Monitoring Health/Info
A REST interface which allows to access the status of the service:
1. *hostname:port/contextpath*/actuator/info (information about version)
2. *hostname:port/contextpath*/actuator/health (information about (connected) services)

