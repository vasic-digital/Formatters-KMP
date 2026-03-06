/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Kotlin Multiplatform Parsed Document
 * Platform-agnostic parsed document model
 *
 *########################################################*/
package digital.vasic.formatters

/**
 * Represents a parsed document with structured content.
 *
 * Contains the original raw content, processed content, metadata, and parsing errors.
 * HTML generation is lazily evaluated and cached via an injectable HTML generator.
 *
 * @property format The format that was used to parse this document
 * @property rawContent Raw text content (original markup)
 * @property parsedContent Parsed content (HTML, structured data, etc.)
 * @property metadata Document metadata extracted during parsing
 * @property errors Any parsing errors or warnings
 */
class ParsedDocument(
    val format: TextFormat,
    val rawContent: String,
    val parsedContent: String,
    val metadata: Map<String, String> = emptyMap(),
    val errors: List<String> = emptyList()
) {
    private var _cachedHtmlLight: String? = null
    private var _cachedHtmlDark: String? = null
    private var _htmlGenerator: ((ParsedDocument, Boolean) -> String)? = null

    /**
     * Set the HTML generator function used by toHtml().
     * This decouples ParsedDocument from any specific parser registry.
     */
    fun setHtmlGenerator(generator: (ParsedDocument, Boolean) -> String) {
        _htmlGenerator = generator
    }

    /**
     * Convert the parsed document to HTML with lazy caching.
     *
     * Requires setHtmlGenerator() to be called first, or returns a default
     * pre-formatted representation.
     */
    fun toHtml(lightMode: Boolean = true): String {
        if (lightMode) {
            return _cachedHtmlLight ?: run {
                val generator = _htmlGenerator ?: { doc, _ ->
                    "<pre>${doc.rawContent.escapeHtml()}</pre>"
                }
                generator(this, lightMode).also { _cachedHtmlLight = it }
            }
        } else {
            return _cachedHtmlDark ?: run {
                val generator = _htmlGenerator ?: { doc, _ ->
                    "<pre>${doc.rawContent.escapeHtml()}</pre>"
                }
                generator(this, lightMode).also { _cachedHtmlDark = it }
            }
        }
    }

    fun clearHtmlCache() {
        _cachedHtmlLight = null
        _cachedHtmlDark = null
    }

    fun hasHtmlCached(lightMode: Boolean = true): Boolean {
        return if (lightMode) _cachedHtmlLight != null else _cachedHtmlDark != null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ParsedDocument
        return format == other.format &&
            rawContent == other.rawContent &&
            parsedContent == other.parsedContent &&
            metadata == other.metadata &&
            errors == other.errors
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + rawContent.hashCode()
        result = 31 * result + parsedContent.hashCode()
        result = 31 * result + metadata.hashCode()
        result = 31 * result + errors.hashCode()
        return result
    }

    override fun toString(): String {
        return "ParsedDocument(format=$format, rawContent='${rawContent.take(50)}...', metadata=$metadata, errors=$errors)"
    }

    fun copy(
        format: TextFormat = this.format,
        rawContent: String = this.rawContent,
        parsedContent: String = this.parsedContent,
        metadata: Map<String, String> = this.metadata,
        errors: List<String> = this.errors
    ): ParsedDocument {
        return ParsedDocument(format, rawContent, parsedContent, metadata, errors)
    }
}
