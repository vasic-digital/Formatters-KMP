/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 *########################################################*/
package digital.vasic.formatters

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

// =============================================
// TextFormat Tests
// =============================================

class TextFormatBasicTest {
    @Test
    fun createTextFormatWithDefaults() {
        val format = TextFormat(id = "test", name = "Test", defaultExtension = ".tst")
        assertEquals("test", format.id)
        assertEquals("Test", format.name)
        assertEquals(".tst", format.defaultExtension)
        assertEquals(listOf(".tst"), format.extensions)
        assertEquals(emptyList(), format.detectionPatterns)
    }

    @Test
    fun createTextFormatWithAllFields() {
        val format = TextFormat(
            id = "markdown",
            name = "Markdown",
            defaultExtension = ".md",
            extensions = listOf(".md", ".markdown"),
            detectionPatterns = listOf("^#+ ")
        )
        assertEquals(2, format.extensions.size)
        assertEquals(1, format.detectionPatterns.size)
    }

    @Test
    fun textFormatEquality() {
        val f1 = TextFormat(id = "a", name = "A", defaultExtension = ".a")
        val f2 = TextFormat(id = "a", name = "A", defaultExtension = ".a")
        assertEquals(f1, f2)
        assertEquals(f1.hashCode(), f2.hashCode())
    }

    @Test
    fun textFormatCopy() {
        val original = TextFormat(id = "md", name = "Markdown", defaultExtension = ".md")
        val copy = original.copy(name = "MD")
        assertEquals("md", copy.id)
        assertEquals("MD", copy.name)
    }

    @Test
    fun formatIdConstants() {
        assertEquals("unknown", TextFormat.ID_UNKNOWN)
        assertEquals("plaintext", TextFormat.ID_PLAINTEXT)
        assertEquals("markdown", TextFormat.ID_MARKDOWN)
        assertEquals("todotxt", TextFormat.ID_TODOTXT)
        assertEquals("csv", TextFormat.ID_CSV)
        assertEquals("latex", TextFormat.ID_LATEX)
        assertEquals("jupyter", TextFormat.ID_JUPYTER)
        assertEquals("binary", TextFormat.ID_BINARY)
    }
}

// =============================================
// ParsedDocument Tests
// =============================================

class ParsedDocumentTest {
    private val testFormat = TextFormat(id = "test", name = "Test", defaultExtension = ".tst")

    @Test
    fun createParsedDocument() {
        val doc = ParsedDocument(
            format = testFormat,
            rawContent = "hello",
            parsedContent = "<p>hello</p>"
        )
        assertEquals(testFormat, doc.format)
        assertEquals("hello", doc.rawContent)
        assertEquals("<p>hello</p>", doc.parsedContent)
        assertEquals(emptyMap(), doc.metadata)
        assertEquals(emptyList(), doc.errors)
    }

    @Test
    fun parsedDocumentWithMetadata() {
        val doc = ParsedDocument(
            format = testFormat,
            rawContent = "test",
            parsedContent = "test",
            metadata = mapOf("lines" to "1", "words" to "1")
        )
        assertEquals("1", doc.metadata["lines"])
        assertEquals("1", doc.metadata["words"])
    }

    @Test
    fun parsedDocumentWithErrors() {
        val doc = ParsedDocument(
            format = testFormat,
            rawContent = "bad",
            parsedContent = "",
            errors = listOf("Line 1: syntax error")
        )
        assertEquals(1, doc.errors.size)
        assertEquals("Line 1: syntax error", doc.errors[0])
    }

    @Test
    fun parsedDocumentToHtmlDefault() {
        val doc = ParsedDocument(
            format = testFormat,
            rawContent = "<b>bold</b>",
            parsedContent = ""
        )
        val html = doc.toHtml()
        assertEquals("<pre>&lt;b&gt;bold&lt;/b&gt;</pre>", html)
    }

    @Test
    fun parsedDocumentToHtmlWithGenerator() {
        val doc = ParsedDocument(
            format = testFormat,
            rawContent = "hello",
            parsedContent = "<p>hello</p>"
        )
        doc.setHtmlGenerator { d, light ->
            if (light) "<div class='light'>${d.parsedContent}</div>"
            else "<div class='dark'>${d.parsedContent}</div>"
        }
        assertEquals("<div class='light'><p>hello</p></div>", doc.toHtml(true))
        assertEquals("<div class='dark'><p>hello</p></div>", doc.toHtml(false))
    }

    @Test
    fun parsedDocumentHtmlCaching() {
        val doc = ParsedDocument(format = testFormat, rawContent = "x", parsedContent = "")
        assertFalse(doc.hasHtmlCached(true))
        assertFalse(doc.hasHtmlCached(false))

        doc.toHtml(true)
        assertTrue(doc.hasHtmlCached(true))
        assertFalse(doc.hasHtmlCached(false))

        doc.toHtml(false)
        assertTrue(doc.hasHtmlCached(false))

        doc.clearHtmlCache()
        assertFalse(doc.hasHtmlCached(true))
        assertFalse(doc.hasHtmlCached(false))
    }

    @Test
    fun parsedDocumentEquality() {
        val d1 = ParsedDocument(format = testFormat, rawContent = "a", parsedContent = "b")
        val d2 = ParsedDocument(format = testFormat, rawContent = "a", parsedContent = "b")
        assertEquals(d1, d2)
        assertEquals(d1.hashCode(), d2.hashCode())
    }

    @Test
    fun parsedDocumentCopy() {
        val original = ParsedDocument(format = testFormat, rawContent = "a", parsedContent = "b")
        val copy = original.copy(rawContent = "c")
        assertEquals("c", copy.rawContent)
        assertEquals("b", copy.parsedContent)
    }

    @Test
    fun parsedDocumentToString() {
        val doc = ParsedDocument(format = testFormat, rawContent = "hello world", parsedContent = "")
        assertTrue(doc.toString().contains("ParsedDocument"))
        assertTrue(doc.toString().contains("hello world"))
    }
}

// =============================================
// TextParser Tests
// =============================================

class TextParserTest {
    private val testFormat = TextFormat(id = "test", name = "Test", defaultExtension = ".tst")

    private val simpleParser = object : TextParser {
        override val supportedFormat = testFormat
        override fun parse(content: String, options: Map<String, Any>): ParsedDocument {
            return ParsedDocument(
                format = testFormat,
                rawContent = content,
                parsedContent = content.uppercase(),
                metadata = mapOf("length" to content.length.toString())
            )
        }
    }

    @Test
    fun parserCanParse() {
        assertTrue(simpleParser.canParse(testFormat))
        assertFalse(simpleParser.canParse(TextFormat(id = "other", name = "Other", defaultExtension = ".oth")))
    }

    @Test
    fun parserParse() {
        val doc = simpleParser.parse("hello")
        assertEquals("hello", doc.rawContent)
        assertEquals("HELLO", doc.parsedContent)
        assertEquals("5", doc.metadata["length"])
    }

    @Test
    fun parserDefaultToHtml() {
        val doc = simpleParser.parse("<script>")
        val html = simpleParser.toHtml(doc)
        assertEquals("<pre>&lt;script&gt;</pre>", html)
    }

    @Test
    fun parserDefaultValidate() {
        val errors = simpleParser.validate("anything")
        assertTrue(errors.isEmpty())
    }
}

// =============================================
// EscapeHtml Tests
// =============================================

class EscapeHtmlTest {
    @Test
    fun escapeAllSpecialChars() {
        assertEquals("&amp;", "&".escapeHtml())
        assertEquals("&lt;", "<".escapeHtml())
        assertEquals("&gt;", ">".escapeHtml())
        assertEquals("&quot;", "\"".escapeHtml())
        assertEquals("&#39;", "'".escapeHtml())
    }

    @Test
    fun escapeFullString() {
        val input = "<script>alert('xss')</script>"
        val expected = "&lt;script&gt;alert(&#39;xss&#39;)&lt;/script&gt;"
        assertEquals(expected, input.escapeHtml())
    }

    @Test
    fun escapeEmptyString() {
        assertEquals("", "".escapeHtml())
    }

    @Test
    fun noEscapeNeeded() {
        assertEquals("hello world", "hello world".escapeHtml())
    }
}

// =============================================
// ParseOptions Tests
// =============================================

class ParseOptionsTest {
    @Test
    fun buildEmptyOptions() {
        val opts = ParseOptions.create().build()
        assertTrue(opts.isEmpty())
    }

    @Test
    fun buildWithLineNumbers() {
        val opts = ParseOptions.create().enableLineNumbers().build()
        assertEquals(true, opts["lineNumbers"])
    }

    @Test
    fun buildWithHighlighting() {
        val opts = ParseOptions.create().enableHighlighting().build()
        assertEquals(true, opts["highlighting"])
    }

    @Test
    fun buildWithBaseUrl() {
        val opts = ParseOptions.create().setBaseUrl("https://example.com").build()
        assertEquals("https://example.com", opts["baseUrl"])
    }

    @Test
    fun buildWithCustomOption() {
        val opts = ParseOptions.create().set("custom", 42).build()
        assertEquals(42, opts["custom"])
    }

    @Test
    fun buildChained() {
        val opts = ParseOptions.create()
            .enableLineNumbers()
            .enableHighlighting()
            .setBaseUrl("https://x.com")
            .set("theme", "dark")
            .build()
        assertEquals(4, opts.size)
    }
}

// =============================================
// FormatRegistry Tests
// =============================================

class FormatRegistryTest {
    private fun createTestRegistry(): FormatRegistry {
        return FormatRegistry(listOf(
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
                extensions = listOf(".txt", ".text")
            ),
            TextFormat(
                id = "csv",
                name = "CSV",
                defaultExtension = ".csv",
                extensions = listOf(".csv"),
                detectionPatterns = listOf("^.*,.*,.*$")
            )
        ))
    }

    @Test
    fun getById() {
        val registry = createTestRegistry()
        val md = registry.getById("markdown")
        assertNotNull(md)
        assertEquals("Markdown", md.name)
        assertNull(registry.getById("nonexistent"))
    }

    @Test
    fun getByExtension() {
        val registry = createTestRegistry()
        val md = registry.getByExtension(".md")
        assertNotNull(md)
        assertEquals("markdown", md.id)

        val md2 = registry.getByExtension("md")
        assertNotNull(md2)
        assertEquals("markdown", md2.id)
    }

    @Test
    fun detectByExtensionWithFallback() {
        val registry = createTestRegistry()
        val format = registry.detectByExtension(".unknown")
        assertEquals("plaintext", format.id)
    }

    @Test
    fun detectByContent() {
        val registry = createTestRegistry()
        val format = registry.detectByContent("# Title\n\nSome content")
        assertNotNull(format)
        assertEquals("markdown", format.id)
    }

    @Test
    fun detectByContentEmpty() {
        val registry = createTestRegistry()
        assertNull(registry.detectByContent(""))
    }

    @Test
    fun detectByFilename() {
        val registry = createTestRegistry()
        assertEquals("markdown", registry.detectByFilename("README.md").id)
        assertEquals("csv", registry.detectByFilename("data.csv").id)
        assertEquals("plaintext", registry.detectByFilename("noextension").id)
    }

    @Test
    fun getFormatsByExtension() {
        val registry = FormatRegistry(listOf(
            TextFormat(id = "plaintext", name = "Plain Text", defaultExtension = ".txt", extensions = listOf(".txt")),
            TextFormat(id = "todotxt", name = "Todo.txt", defaultExtension = ".txt", extensions = listOf(".txt"))
        ))
        val formats = registry.getFormatsByExtension(".txt")
        assertEquals(2, formats.size)
    }

    @Test
    fun isSupported() {
        val registry = createTestRegistry()
        assertTrue(registry.isSupported("markdown"))
        assertFalse(registry.isSupported("nonexistent"))
    }

    @Test
    fun isExtensionSupported() {
        val registry = createTestRegistry()
        assertTrue(registry.isExtensionSupported(".md"))
        assertFalse(registry.isExtensionSupported(".xyz"))
    }

    @Test
    fun getFormatNames() {
        val registry = createTestRegistry()
        val names = registry.getFormatNames()
        assertEquals(3, names.size)
        assertTrue(names.contains("Markdown"))
    }

    @Test
    fun getAllExtensions() {
        val registry = createTestRegistry()
        val exts = registry.getAllExtensions()
        assertTrue(exts.contains(".md"))
        assertTrue(exts.contains(".csv"))
    }

    @Test
    fun registerFormat() {
        val registry = FormatRegistry()
        assertEquals(0, registry.formats.size)
        registry.register(TextFormat(id = "test", name = "Test", defaultExtension = ".tst"))
        assertEquals(1, registry.formats.size)
    }

    @Test
    fun registerAllFormats() {
        val registry = FormatRegistry()
        registry.registerAll(listOf(
            TextFormat(id = "a", name = "A", defaultExtension = ".a"),
            TextFormat(id = "b", name = "B", defaultExtension = ".b")
        ))
        assertEquals(2, registry.formats.size)
    }

    @Test
    fun clearFormats() {
        val registry = createTestRegistry()
        assertEquals(3, registry.formats.size)
        registry.clear()
        assertEquals(0, registry.formats.size)
    }
}

// =============================================
// ParserRegistry Tests
// =============================================

class ParserRegistryTest {
    private val testFormat = TextFormat(id = "test", name = "Test", defaultExtension = ".tst")

    private fun createTestParser(): TextParser = object : TextParser {
        override val supportedFormat = testFormat
        override fun parse(content: String, options: Map<String, Any>): ParsedDocument {
            return ParsedDocument(format = testFormat, rawContent = content, parsedContent = content)
        }
    }

    @Test
    fun registerAndGetParser() {
        val registry = ParserRegistry()
        val parser = createTestParser()
        registry.register(parser)
        val found = registry.getParser(testFormat)
        assertNotNull(found)
        assertEquals("test", found.supportedFormat.id)
    }

    @Test
    fun registerDuplicateThrows() {
        val registry = ParserRegistry()
        registry.register(createTestParser())
        assertFailsWith<IllegalArgumentException> {
            registry.register(createTestParser())
        }
    }

    @Test
    fun registerLazyAndGet() {
        val registry = ParserRegistry()
        var instantiated = false
        registry.registerLazy("test") {
            instantiated = true
            createTestParser()
        }
        assertFalse(instantiated)
        assertEquals(1, registry.getPendingParserCount())
        assertEquals(0, registry.getInstantiatedParserCount())

        val parser = registry.getParser(testFormat)
        assertNotNull(parser)
        assertTrue(instantiated)
        assertEquals(0, registry.getPendingParserCount())
        assertEquals(1, registry.getInstantiatedParserCount())
    }

    @Test
    fun hasParser() {
        val registry = ParserRegistry()
        assertFalse(registry.hasParser(testFormat))
        registry.register(createTestParser())
        assertTrue(registry.hasParser(testFormat))
    }

    @Test
    fun getParserReturnsNullForUnknown() {
        val registry = ParserRegistry()
        val unknown = TextFormat(id = "unknown", name = "Unknown", defaultExtension = ".unk")
        assertNull(registry.getParser(unknown))
    }

    @Test
    fun getAllParsers() {
        val registry = ParserRegistry()
        registry.register(createTestParser())
        assertEquals(1, registry.getAllParsers().size)
    }

    @Test
    fun clearRegistry() {
        val registry = ParserRegistry()
        registry.register(createTestParser())
        registry.registerLazy("other") { createTestParser() }
        registry.clear()
        assertEquals(0, registry.getInstantiatedParserCount())
        assertEquals(0, registry.getPendingParserCount())
    }
}
