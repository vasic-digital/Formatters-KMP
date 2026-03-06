# Formatters-KMP

## Text Format Framework for Kotlin Multiplatform

Formatters-KMP provides text format detection, parsing interfaces, and configurable registries for cross-platform applications.

### Features

- **TextFormat** — Extensible format definitions with detection patterns
- **TextParser** — Clean interface for format-specific parsers
- **ParsedDocument** — Lazy HTML caching with injectable generator
- **FormatRegistry** — Format detection by extension, content, or filename
- **ParserRegistry** — Lazy-loading parser registry
- **Zero Dependencies** — Only Kotlin stdlib

### Getting Started

```kotlin
// settings.gradle.kts
includeBuild("Formatters-KMP")

// build.gradle.kts
dependencies {
    implementation("digital.vasic.formatters:Formatters-KMP")
}
```

### Documentation

- [User Guide](docs/user-guide.md)
- [API Reference](docs/api-reference.md)
- [Architecture](docs/architecture.md)

### Related Libraries

| Library | Purpose |
|---------|---------|
| [Config-KMP](https://github.com/vasic-digital/Config-KMP) | Storage configuration types |
| [Database-KMP](https://github.com/vasic-digital/Database-KMP) | Storage metadata database |
| [Storage-KMP](https://github.com/vasic-digital/Storage-KMP) | Network storage interfaces |
| [Document-KMP](https://github.com/vasic-digital/Document-KMP) | Document model types |

### License

Apache-2.0
