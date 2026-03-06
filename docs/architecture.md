# Formatters-KMP Architecture

## Design Philosophy

Formatters-KMP provides a **framework**, not a library of parsers. It defines the contracts and registries; consuming applications bring their own parser implementations.

## Component Relationships

```
┌─────────────────────────────────────────────┐
│         Consuming Application                │
│  (provides TextParser implementations)       │
├─────────────────────────────────────────────┤
│         Formatters-KMP (this library)        │
│  ┌──────────────┐  ┌────────────────────┐   │
│  │ TextFormat    │  │ ParsedDocument     │   │
│  │ (data class)  │  │ (lazy HTML cache)  │   │
│  └──────────────┘  └────────────────────┘   │
│  ┌──────────────┐  ┌────────────────────┐   │
│  │ TextParser   │  │ FormatRegistry     │   │
│  │ (interface)   │  │ (configurable)     │   │
│  └──────────────┘  └────────────────────┘   │
│  ┌──────────────┐  ┌────────────────────┐   │
│  │ ParseOptions │  │ ParserRegistry     │   │
│  │ (builder)     │  │ (lazy loading)     │   │
│  └──────────────┘  └────────────────────┘   │
├─────────────────────────────────────────────┤
│         Kotlin Stdlib (only dependency)      │
└─────────────────────────────────────────────┘
```

## Key Differences from Yole's Built-in System

| Aspect | Yole's FormatRegistry | Formatters-KMP FormatRegistry |
|--------|----------------------|-------------------------------|
| Type | `object` (singleton) | `class` (configurable) |
| Formats | Hardcoded 22 formats | Empty by default, consumer adds |
| Parser lookup | Global ParserRegistry | Injectable, instance-based |
| HTML generation | Coupled to ParserRegistry | Injectable via setHtmlGenerator |
| Dependencies | platformSynchronized | None beyond stdlib |

## Data Flow

1. Consumer creates `FormatRegistry` with their formats
2. Consumer creates `ParserRegistry` and registers parsers
3. Detection: `FormatRegistry.detectByFilename("file.md")` returns `TextFormat`
4. Parsing: `ParserRegistry.getParser(format).parse(content)` returns `ParsedDocument`
5. Display: `document.toHtml()` generates/caches HTML via injected generator
