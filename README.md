# Formatters-KMP

Cross-platform Kotlin Multiplatform library providing text format detection, parsing interfaces, and a configurable format registry.

## Overview

Formatters-KMP defines the core text formatting framework:

- **TextFormat** — Data class representing a text format with id, name, extensions, and detection patterns
- **TextParser** — Interface for format-specific parsers (parse, toHtml, validate)
- **ParsedDocument** — Parsed result with lazy HTML caching and injectable HTML generator
- **FormatRegistry** — Configurable registry for format detection by extension, content, or filename
- **ParserRegistry** — Registry for parser instances with lazy loading support
- **ParseOptions** — Fluent builder for parsing configuration

## Supported Platforms

| Platform | Target |
|----------|--------|
| Android | android |
| Desktop | jvm |
| iOS | iosArm64, iosSimulatorArm64, iosX64 |
| Web | wasmJs |

## Installation

Add as a composite build in your project's `settings.gradle.kts`:

```kotlin
includeBuild("Formatters-KMP")
```

Then add the dependency:

```kotlin
implementation("digital.vasic.formatters:Formatters-KMP")
```

## Quick Start

```kotlin
import digital.vasic.formatters.*

// Create a format registry with your formats
val registry = FormatRegistry(listOf(
    TextFormat(
        id = "markdown",
        name = "Markdown",
        defaultExtension = ".md",
        extensions = listOf(".md", ".markdown"),
        detectionPatterns = listOf("^#+ ")
    ),
    TextFormat(
        id = "plaintext",
        name = "Plain Text",
        defaultExtension = ".txt",
        extensions = listOf(".txt")
    )
))

// Detect format
val format = registry.detectByFilename("README.md")
println(format.name) // "Markdown"

// Detect by content
val detected = registry.detectByContent("# Hello World")
println(detected?.id) // "markdown"
```

## Building

```bash
./gradlew desktopTest    # Run tests
./gradlew build          # Build all targets
```

## License

Apache-2.0
