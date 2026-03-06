# CLAUDE.md

## Project Overview

**Formatters-KMP** is a Kotlin Multiplatform library providing text format detection, parsing interfaces, and configurable format/parser registries. It extracts the reusable formatting framework from Yole.

**Package namespace:** `digital.vasic.formatters`

## Build Commands

```bash
./gradlew desktopTest    # Run tests (primary)
./gradlew build          # Build all targets
```

## Architecture

### Source Layout

```
src/
├── commonMain/kotlin/digital/vasic/formatters/
│   ├── TextFormat.kt       # Format metadata data class + ID constants
│   ├── TextParser.kt       # Parser interface + escapeHtml extension
│   ├── ParsedDocument.kt   # Parsed result with lazy HTML caching
│   ├── ParseOptions.kt     # Fluent parsing options builder
│   ├── FormatRegistry.kt   # Configurable format registry
│   └── ParserRegistry.kt   # Parser registry with lazy loading
└── commonTest/kotlin/digital/vasic/formatters/
    └── FormattersTest.kt   # All tests
```

### Key Design Decisions

- **FormatRegistry is a class, not an object** — consumers configure their own formats
- **ParsedDocument.toHtml() uses injectable generator** — decoupled from any specific parser registry
- **ParserRegistry is a class** — supports multiple independent registries
- **Zero dependencies** — only kotlin-test for testing

## Code Conventions

- All types are in the `digital.vasic.formatters` package
- TextFormat uses companion object for ID constants
- Registries are mutable and configurable
- ParsedDocument caches HTML separately for light/dark modes
