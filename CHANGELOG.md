# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed

### Added

### Changed

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

[Unreleased]: https://github.com/kit-data-manager/indexing-service/compare/v0.9.0...HEAD
[0.9.0]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.3...v0.9.0
[0.1.3]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.4...v0.1.0
[0.0.4]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.3...v0.0.4
[0.0.3]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/kit-data-manager/metastore2/indexing-service/tag/v0.0.2

