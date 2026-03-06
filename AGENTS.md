# AGENTS.md

## Agent Guidelines for Formatters-KMP

### What This Library Does

Formatters-KMP provides the framework for text format detection and parsing in KMP projects. It does NOT contain format-specific parsers (Markdown, LaTeX, etc.) — those live in the consuming application.

### Key Constraints

1. **Zero dependencies** — Only Kotlin stdlib
2. **Interface-first** — TextParser defines the contract; implementations live elsewhere
3. **Configurable registries** — FormatRegistry and ParserRegistry are classes, not singletons
4. **Decoupled HTML generation** — ParsedDocument accepts an injectable HTML generator

### Common Tasks

**Adding a new format ID constant:**
1. Add to `TextFormat.Companion` in `TextFormat.kt`
2. Add test for the constant value in `FormattersTest.kt`

**Running tests:**
```bash
./gradlew desktopTest
```

### Testing Standards

- Every public type, method, and computed property must have tests
- Test edge cases (empty strings, null-like values)
- All tests in `src/commonTest/kotlin/digital/vasic/formatters/FormattersTest.kt`
