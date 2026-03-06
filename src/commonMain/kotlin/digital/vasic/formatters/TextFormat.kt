/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Kotlin Multiplatform Text Format System
 * Platform-agnostic format definitions
 *
 *########################################################*/
package digital.vasic.formatters

/**
 * Represents a text format with its metadata.
 *
 * @property id Unique format identifier (e.g., "markdown", "todotxt", "latex")
 * @property name Human-readable format name (e.g., "Markdown", "Todo.txt", "LaTeX")
 * @property defaultExtension Default file extension with dot (e.g., ".md", ".txt", ".tex")
 * @property extensions Supported file extensions for this format
 * @property detectionPatterns Regex patterns for content-based format detection
 */
data class TextFormat(
    val id: String,
    val name: String,
    val defaultExtension: String,
    val extensions: List<String> = listOf(defaultExtension),
    val detectionPatterns: List<String> = emptyList()
) {
    companion object {
        const val ID_UNKNOWN = "unknown"
        const val ID_PLAINTEXT = "plaintext"
        const val ID_MARKDOWN = "markdown"
        const val ID_TODOTXT = "todotxt"
        const val ID_CSV = "csv"
        const val ID_WIKITEXT = "wikitext"
        const val ID_KEYVALUE = "keyvalue"
        const val ID_ASCIIDOC = "asciidoc"
        const val ID_ORGMODE = "orgmode"
        const val ID_LATEX = "latex"
        const val ID_RESTRUCTUREDTEXT = "restructuredtext"
        const val ID_TASKPAPER = "taskpaper"
        const val ID_TEXTILE = "textile"
        const val ID_CREOLE = "creole"
        const val ID_TIDDLYWIKI = "tiddlywiki"
        const val ID_JUPYTER = "jupyter"
        const val ID_RMARKDOWN = "rmarkdown"
        const val ID_BINARY = "binary"
    }
}
