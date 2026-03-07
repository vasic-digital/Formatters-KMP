<!-- SPDX-FileCopyrightText: 2025 Milos Vasic -->
<!-- SPDX-License-Identifier: Apache-2.0 -->

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-03-06

### Added
- Initial release extracted from Yole monolith
- `TextFormat` - Data class representing a text format with id, name, extensions, and detection patterns
- `TextParser` - Interface for format-specific parsers (parse, toHtml, validate)
- `ParsedDocument` - Parsed result with lazy HTML caching and injectable HTML generator
- `FormatRegistry` - Configurable registry for format detection by extension, content, or filename
- `ParserRegistry` - Registry for parser instances with lazy loading support
- `ParseOptions` - Fluent builder for parsing configuration
- Kotlin Multiplatform support (Android, Desktop/JVM, iOS, Wasm/JS)
- Comprehensive test suite
- CI/CD via GitHub Actions

### Infrastructure
- Gradle build with version catalog
- Kover code coverage
- SPDX license headers (Apache-2.0)
