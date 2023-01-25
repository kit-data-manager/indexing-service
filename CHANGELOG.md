# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added

### Changed


## [0.1.0] - date 2022-01-25
### Added
- Switch to gitHub Actions for CI
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

[Unreleased]: https://github.com/kit-data-manager/indexing-service/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.4...v0.1.0
[0.0.4]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.3...v0.0.4
[0.0.3]: https://github.com/kit-data-manager/indexing-service/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/kit-data-manager/metastore2/indexing-service/tag/v0.0.2

