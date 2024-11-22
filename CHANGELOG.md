# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed

### Added

### Changed

## [1.0.2] - date 2024-11-22
### Changed
- Update python Docker tag to v3.13 
- Upgrade to Java 21.
- Update dependency gradle to v8.11.1

### Libs
- Bump com.github.jknack:handlebars from 4.3.1 to 4.4.0
- Bump com.gorylenko.gradle-git-properties from 2.4.1 to 2.4.2 
- Bump commons-codec:commons-codec from 1.16.0 to 1.16.1 
- Update dependency edu.kit.datamanager:service-base to v1.3.2
- Update plugin io.freefair.lombok to v8.11 
- Update plugin io.freefair.maven-publish-java to v8.11 
- Update javersVersion to v7.7.0
- Update dependency org.apache.tika:tika-core to v3
- Bump org.asciidoctor.jvm.convert from 4.0.1 to 4.0.2
- Bump org.json:json from 20231013 to 20240205 
- Bump org.mockito:mockito-core from 5.8.0 to 5.10.0 
- Bump org.owasp.dependencycheck from 9.0.8 to 9.0.9 
- Bump org.postgresql:postgresql from 42.7.1 to 42.7.3 
- Update plugin org.springframework.boot to v3.3.6
- Bump org.springframework.data:spring-data-elasticsearch from 5.2.2 to 5.2.3.
- Bump org.springframework:spring-messaging from 6.1.3 to 6.1.6

### Github Actions
- Bump codecov/codecov-action from 3 to 4 

## [1.0.1] - date 2024-01-12
### Changed
- Use softlink to jar file in start script.
- Switch to GitHub Packages.
- Bump com.google.guava:guava from 32.1.3-jre to 33.0.0-jre
- Bump de.codecentric:spring-boot-admin-starter-client from 3.1.8 to 3.2.1
- Bump elasticsearch instance for test from 7.9.3 to 8.11.1
- Bump javersVersion from 7.3.6 to 7.3.7
- Bump org.asciidoctor.jvm.convert from 3.3.2 to 4.0.1
- Bump org.owasp.dependencycheck from 9.0.6 to 9.0.8
- Bump org.springframework.boot from 3.2.0 to 3.2.1
- Bump org.springframework.cloud:spring-cloud-gateway-mvc from 4.1.0 to 4.1.1
- Bump org.springframework.data:spring-data-elasticsearch from 5.2.1 to 5.2.2
- Bump org.springframework:spring-messaging from 6.1.2 to 6.1.3 

## [1.0.0] - date 2023-12-18
### Added
- Enable authentication for RabbitMQ 
- Add interface description

### Changed
- The mappings directory is now a subdirectory of the installation directory by default.
- Bump actions/checkout from 3 to 4
- Bump actions/setup-java from 3 to 4
- Bump crazy-max/ghaction-docker-meta from 4 to 5
- Bump docker/build-push-action from 4 to 5
- Bump docker/setup-buildx-action from 2 to 3
- Bump docker/setup-qemu-action from 2 to 3
- Bump github/codeql-action from 2 to 3
- Bump com.google.guava:guava from 32.1.1-jre to 32.1.3-jre
- Bump com.h2database:h2 from 2.2.220 to 2.2.224
- Bump de.codecentric:spring-boot-admin-starter-client from 3.0.4 to 3.1.8
- Bump edu.kit.datamanager:generic-message-consumer from 1.0.0 to 1.1.1
- Bump io.freefair.lombok from 8.1.0 to 8.4
- Bump io.freefair.maven-publish-java from 8.1.0 to 8.4
- Bump io.spring.dependency-management from 1.1.2 to 1.1.4
- Bump javersVersion from 7.3.0 to 7.3.6
- Bump org.apache.tika:tika-core from 2.8.0 to 2.9.1
- Bump org.json:json from 20230618 to 20231013
- Bump org.mockito:mockito-core from 5.4.0 to 5.8.0
- Bump org.owasp.dependencycheck from 8.3.1 to 9.0.6
- Bump org.postgresql:postgresql from 42.6.0 to 42.7.1
- Bump org.springframework.boot from 3.1.2 to 3.2.0
- Bump org.springframework:spring-messaging from 6.0.2 to 6.1.2
- Bump org.springframework.cloud:spring-cloud-gateway-mvc from 4.0.6 to 4.1.0
- Bump org.springframework.data:spring-data-elasticsearch from 5.1.2 to 5.2.0
- Bump org.springframework.restdocs:spring-restdocs-mockmvc from 3.0.0 to 3.0.1
- Bump springDocVersion from 2.1.0 to 2.3.0
- Bump elasticsearch instance for test from 7.9.3 to 8.11.1

### Fixed
- Settings for health check.
 
## [0.9.0] - date 2023-07-25
Update service to Spring Boot 3.
### Changed
- Bump actions/checkout from 2 to 3 
- Bump actions/setup-java from 2 to 3
- Bump crazy-max/ghaction-docker-meta from 1 to 4 
- Bump codecov/codecov-action from 1 to 3 
- Bump docker/build-push-action from 2 to 4 
- Bump docker/login-action from 1 to 2
- Bump docker/setup-buildx-action from 1 to 2
- Bump docker/setup-qemu-action from 1 to 2 
- Bump github/codeql-action from 1 to 2
- Bump com.h2database:h2 from 2.1.214 to 2.2.220.
- Bump de.codecentric:spring-boot-admin-starter-client from 2.7.10 to 3.0.4.
- Bump io.freefair.lombok from 6.6.1 to 6.6.3 
- Bump io.freefair.maven-publish-java from 6.6.1 to 6.6.3 
- Bump io.spring.dependency-management from 1.1.0 to 1.1.2
- Bump javersVersion from 6.9.1 to 7.3.0
- Bump org.json:json from 20220924 to 20230618.
- Bump org.mockito:mockito-core from 5.1.1 to 5.4.0
- Bump org.owasp.dependencycheck from 8.0.2 to 8.3.1 
- Bump org.postgresql:postgresql from 42.5.3 to 42.6.0 
- Bump org.springframework.boot from 2.7.8 to 3.1.2
- Bump org.springframework:spring-messaging from 5.3.25 to 6.0.2.
- Bump org.springframework.cloud:spring-cloud-gateway-mvc from 3.1.4 to 4.0.6.
- Bump org.springframework.data:spring-data-elasticsearch from 5.1.0 to 5.1.2.
- Bump springDocVersion from 1.6.14 to 2.1.0
- Bump gradle from 7.6 to 8.2.1

## [0.1.3] - date 2023-02-10
### Fixed
- Wrong default settings for dockerhub

## [0.1.2] - date 2023-02-07
### Fixed
- Dockerfile for dockerhub

### Changed
- Bump mockito-core from 5.0.0 to 5.1.1
- Bump javersVersion from 6.8.2 to 6.9.1
- Bump postgresql from 42.5.1 to 42.5.3
- Bump tika-core from 2.6.0 to 2.7.0

## [0.1.1] - date 2023-01-30
### Changed
- Bump mockito-core from 4.8.1 to 5.0.0 by @dependabot in https://github.com/kit-data-manager/indexing-service/pull/20
- Enhance configuration files. 

## [0.1.0] - date 2023-01-27
### Added
- Switch to gitHub Actions for CI
- Add /actuator/info endpoint

### Changed
- Update to gradle version 7.6
- Support for Java 17 (build/tests)
- Update to spring-boot 2.7.8
- Update to spring-doc 1.6.14
- Update to javers 6.8.2
- Update to postgresql 42.5.1
- Update to h2 2.1.214
- Update to io.freefair.lombok 6.6.1
- Remove dependencies of powermock for tests
- Update to org.owasp.dependencycheck 8.0.1
- Update to spring-messaging 5.3.24
- Update to spring-cloud 3.1.4
- Update to service-base 1.1.0
- Update to generic-message-consumer 1.0.0

## [0.0.4] - date 2020-12-16
### Fixed
- Dockerfile for dockerhub

## [0.0.3] - date 2020-12-16
### Added
- Dockerfile for dockerhub
- Travis for CI

## [0.0.2] - date 2020-12-15
First version supporting registering of mappings (Gemma only)
and mapping of metadata documents delivered by RabbitMQ
### Added
- Registration of mapping documents. 
- Mapping of metadata documents with Gemma
- Ingest to elasticsearch

[Unreleased]: https://github.com/kit-data-manager/indexing-service/compare/v1.0.2...HEAD
[1.0.2]: https://github.com/kit-data-manager/indexing-service/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/kit-data-manager/indexing-service/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/kit-data-manager/indexing-service/compare/v0.9.0...v1.0.0
[0.9.0]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.3...v0.9.0
[0.1.3]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.4...v0.1.0
[0.0.4]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.3...v0.0.4
[0.0.3]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/kit-data-manager/metastore2/indexing-service/tag/v0.0.2

