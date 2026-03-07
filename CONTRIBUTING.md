<!-- SPDX-FileCopyrightText: 2025 Milos Vasic -->
<!-- SPDX-License-Identifier: Apache-2.0 -->

# Contributing to Formatters-KMP

Thank you for your interest in contributing to Formatters-KMP!

## Getting Started

1. Fork the repository
2. Clone your fork
3. Create a feature branch: `git checkout -b feature/my-feature`
4. Make your changes
5. Run tests: `./gradlew test`
6. Commit: `git commit -m "feat: add my feature"`
7. Push: `git push origin feature/my-feature`
8. Open a Pull Request

## Development Setup

### Prerequisites
- JDK 11+
- Gradle 8.7+

### Building
```bash
./gradlew build
```

### Testing
```bash
./gradlew desktopTest      # Run desktop tests
./gradlew test             # Run all platform tests
./gradlew koverHtmlReport  # Coverage report
```

## Code Style

- Kotlin primary language
- Follow existing code conventions
- SPDX license headers required on all new files:
```kotlin
/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Brief description
 *
 *########################################################*/
```

## Module-Specific Notes

- New text formats should provide a `TextFormat` definition with id, name, extensions, and detection patterns
- Parser implementations must conform to the `TextParser` interface (parse, toHtml, validate)
- `ParsedDocument` uses lazy HTML caching; the first `toHtml()` call generates HTML, subsequent calls return cached
- Format registration order in `FormatRegistry` matters: more specific formats should come before general ones
- Detection patterns are regex-based; ensure they do not cause catastrophic backtracking
- `ParseOptions` follows the fluent builder pattern for consistency

## Pull Request Process

1. Update tests for any new functionality
2. Ensure all tests pass
3. Update README.md if API changes
4. Update CHANGELOG.md with your changes

## License

By contributing, you agree that your contributions will be licensed under the Apache-2.0 License.
