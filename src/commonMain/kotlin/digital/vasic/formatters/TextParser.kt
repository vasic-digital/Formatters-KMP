/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Kotlin Multiplatform Text Parser Interface
 * Platform-agnostic text parsing contract
 *
 *########################################################*/
package digital.vasic.formatters

/**
 * Interface for text format parsers.
 *
 * Each format (Markdown, LaTeX, etc.) implements this interface to provide
 * parsing, HTML generation, and validation capabilities.
 */
interface TextParser {
    val supportedFormat: TextFormat

    fun canParse(format: TextFormat): Boolean {
        return supportedFormat.id == format.id
    }

    fun parse(content: String, options: Map<String, Any> = emptyMap()): ParsedDocument

    fun toHtml(document: ParsedDocument, lightMode: Boolean = true): String {
        return buildString {
            append("<pre>")
            append(document.rawContent.escapeHtml())
            append("</pre>")
        }
    }

    fun validate(content: String): List<String> {
        return emptyList()
    }
}

/**
 * Escape HTML special characters in a string.
 */
fun String.escapeHtml(): String {
    return this
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
}
