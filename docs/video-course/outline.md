# Formatters-KMP Video Course Outline

## Module 1: Introduction (10 min)
- What is Formatters-KMP
- Framework vs library of parsers
- Architecture overview

## Module 2: TextFormat and Detection (15 min)
- TextFormat data class and ID constants
- FormatRegistry: extension, content, and filename detection
- Detection priority and fallback behavior

## Module 3: Parser Interface (15 min)
- TextParser interface contract
- parse(), toHtml(), validate() methods
- Implementing a custom parser

## Module 4: ParsedDocument (15 min)
- Document model: rawContent, parsedContent, metadata, errors
- Lazy HTML caching with light/dark modes
- Injectable HTML generator pattern

## Module 5: Registries (15 min)
- FormatRegistry: configurable format registration
- ParserRegistry: eager vs lazy registration
- Building a complete format system

## Module 6: ParseOptions (10 min)
- Fluent builder pattern
- Standard options: lineNumbers, highlighting, baseUrl
- Custom options for format-specific parsers

## Module 7: Integration with Yole (15 min)
- How Yole uses Formatters-KMP
- Bridging between library types and app singletons
- Adding new formats to an existing application
