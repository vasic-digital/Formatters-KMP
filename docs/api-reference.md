# Formatters-KMP API Reference

## Package: `digital.vasic.formatters`

### Data Classes

#### `TextFormat`

| Property | Type | Description |
|----------|------|-------------|
| `id` | `String` | Unique format identifier |
| `name` | `String` | Human-readable name |
| `defaultExtension` | `String` | Default extension with dot |
| `extensions` | `List<String>` | All supported extensions |
| `detectionPatterns` | `List<String>` | Regex patterns for content detection |

**Companion Constants:** `ID_UNKNOWN`, `ID_PLAINTEXT`, `ID_MARKDOWN`, `ID_TODOTXT`, `ID_CSV`, `ID_WIKITEXT`, `ID_KEYVALUE`, `ID_ASCIIDOC`, `ID_ORGMODE`, `ID_LATEX`, `ID_RESTRUCTUREDTEXT`, `ID_TASKPAPER`, `ID_TEXTILE`, `ID_CREOLE`, `ID_TIDDLYWIKI`, `ID_JUPYTER`, `ID_RMARKDOWN`, `ID_BINARY`

---

### Interfaces

#### `TextParser`

| Member | Type | Description |
|--------|------|-------------|
| `supportedFormat` | `TextFormat` | Format this parser handles |
| `canParse(format)` | `Boolean` | Check format compatibility |
| `parse(content, options)` | `ParsedDocument` | Parse content |
| `toHtml(document, lightMode)` | `String` | Generate HTML |
| `validate(content)` | `List<String>` | Validate syntax |

---

### Classes

#### `ParsedDocument`

| Property | Type | Description |
|----------|------|-------------|
| `format` | `TextFormat` | Source format |
| `rawContent` | `String` | Original markup |
| `parsedContent` | `String` | Processed content |
| `metadata` | `Map<String, String>` | Extracted metadata |
| `errors` | `List<String>` | Parsing errors |

| Method | Return | Description |
|--------|--------|-------------|
| `setHtmlGenerator(fn)` | `Unit` | Set HTML generation function |
| `toHtml(lightMode)` | `String` | Generate/return cached HTML |
| `clearHtmlCache()` | `Unit` | Free cached HTML |
| `hasHtmlCached(lightMode)` | `Boolean` | Check cache status |
| `copy(...)` | `ParsedDocument` | Create modified copy |

#### `FormatRegistry`

| Method | Return | Description |
|--------|--------|-------------|
| `register(format)` | `Unit` | Add a format |
| `registerAll(formats)` | `Unit` | Add multiple formats |
| `getById(id)` | `TextFormat?` | Lookup by ID |
| `getByExtension(ext)` | `TextFormat?` | Lookup by extension |
| `detectByExtension(ext)` | `TextFormat` | Detect with fallback |
| `detectByContent(content)` | `TextFormat?` | Detect by content |
| `detectByFilename(name)` | `TextFormat` | Detect by filename |
| `getFormatsByExtension(ext)` | `List<TextFormat>` | All formats for extension |
| `isSupported(id)` | `Boolean` | Check format exists |
| `isExtensionSupported(ext)` | `Boolean` | Check extension exists |
| `getFormatNames()` | `List<String>` | All format names |
| `getAllExtensions()` | `List<String>` | All extensions |
| `clear()` | `Unit` | Remove all formats |

#### `ParserRegistry`

| Method | Return | Description |
|--------|--------|-------------|
| `register(parser)` | `Unit` | Register parser (eager) |
| `registerLazy(id, factory)` | `Unit` | Register factory (lazy) |
| `getParser(format)` | `TextParser?` | Get parser for format |
| `getParser(id, registry)` | `TextParser?` | Get parser by ID |
| `hasParser(format)` | `Boolean` | Check parser exists |
| `getAllParsers()` | `List<TextParser>` | All instantiated parsers |
| `getPendingParserCount()` | `Int` | Lazy factories count |
| `getInstantiatedParserCount()` | `Int` | Instantiated count |
| `clear()` | `Unit` | Remove all parsers |

#### `ParseOptions`

| Method | Return | Description |
|--------|--------|-------------|
| `enableLineNumbers(enable)` | `ParseOptions` | Toggle line numbers |
| `enableHighlighting(enable)` | `ParseOptions` | Toggle highlighting |
| `setBaseUrl(url)` | `ParseOptions` | Set base URL |
| `set(key, value)` | `ParseOptions` | Set custom option |
| `build()` | `Map<String, Any>` | Build options map |

### Extension Functions

#### `String.escapeHtml()`

Escapes `&`, `<`, `>`, `"`, `'` for safe HTML insertion.
