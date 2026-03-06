# Formatters-KMP User Guide

## Getting Started

Formatters-KMP provides a reusable framework for text format detection and parsing across Kotlin Multiplatform targets.

### Adding to Your Project

```kotlin
// settings.gradle.kts
includeBuild("Formatters-KMP")

// build.gradle.kts
dependencies {
    implementation("digital.vasic.formatters:Formatters-KMP")
}
```

## Core Concepts

### TextFormat

Defines a text format with metadata for detection:

```kotlin
val markdown = TextFormat(
    id = "markdown",
    name = "Markdown",
    defaultExtension = ".md",
    extensions = listOf(".md", ".markdown", ".mkd"),
    detectionPatterns = listOf("^#+ ", "^\\[.*\\]\\(.*\\)")
)
```

### FormatRegistry

Configurable registry for format lookup and detection:

```kotlin
val registry = FormatRegistry()
registry.register(markdown)
registry.register(plaintext)

// Detect by extension
val format = registry.detectByExtension(".md")

// Detect by content
val detected = registry.detectByContent("# Hello")

// Detect by filename
val byName = registry.detectByFilename("README.md")
```

### TextParser

Interface for format-specific parsers:

```kotlin
class MarkdownParser : TextParser {
    override val supportedFormat = markdown

    override fun parse(content: String, options: Map<String, Any>): ParsedDocument {
        val html = convertMarkdownToHtml(content)
        return ParsedDocument(
            format = supportedFormat,
            rawContent = content,
            parsedContent = html,
            metadata = mapOf("lines" to content.lines().size.toString())
        )
    }

    override fun toHtml(document: ParsedDocument, lightMode: Boolean): String {
        val theme = if (lightMode) "light" else "dark"
        return "<div class='$theme'>${document.parsedContent}</div>"
    }
}
```

### ParserRegistry

Registry for parser instances with lazy loading:

```kotlin
val parsers = ParserRegistry()

// Eager registration
parsers.register(MarkdownParser())

// Lazy registration (instantiated on first use)
parsers.registerLazy("latex") { LaTeXParser() }

// Get parser
val parser = parsers.getParser(markdown)
val doc = parser?.parse("# Hello World")
```

### ParsedDocument

Parsed result with lazy HTML caching:

```kotlin
val doc = parser.parse("# Title")

// HTML not generated yet
println(doc.metadata["lines"])

// First call generates and caches
val html = doc.toHtml(lightMode = true)

// Second call returns cached
val html2 = doc.toHtml(lightMode = true) // instant

// Clear cache to free memory
doc.clearHtmlCache()
```

### ParseOptions

Fluent builder for parser options:

```kotlin
val options = ParseOptions.create()
    .enableLineNumbers()
    .enableHighlighting()
    .setBaseUrl("https://example.com")
    .build()

val doc = parser.parse(content, options)
```
