/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Kotlin Multiplatform Format Registry
 * Configurable registry for text format detection
 *
 *########################################################*/
package digital.vasic.formatters

/**
 * Registry for text formats with detection and lookup.
 *
 * Unlike a hardcoded registry, this is configurable — consumers register
 * their own formats. Provides detection by extension, content, and filename.
 */
class FormatRegistry(formats: List<TextFormat> = emptyList()) {
    private val _formats = formats.toMutableList()

    val formats: List<TextFormat> get() = _formats.toList()

    fun register(format: TextFormat) {
        _formats.add(format)
    }

    fun registerAll(formats: List<TextFormat>) {
        _formats.addAll(formats)
    }

    fun getById(id: String): TextFormat? {
        return _formats.firstOrNull { it.id == id }
    }

    fun getByExtension(extension: String): TextFormat? {
        val clean = extension.trim().lowercase().let { if (it.startsWith(".")) it else ".$it" }
        return _formats.firstOrNull { format ->
            format.extensions.any { it.equals(clean, ignoreCase = true) }
        }
    }

    fun detectByContent(content: String, maxLines: Int = 10): TextFormat? {
        if (content.isEmpty()) return null
        val sampleText = content.lines().take(maxLines).joinToString("\n")
        return _formats.firstOrNull { format ->
            format.detectionPatterns.any { pattern ->
                Regex(pattern, RegexOption.MULTILINE).containsMatchIn(sampleText)
            }
        }
    }

    fun detectByExtension(extension: String, fallbackId: String = TextFormat.ID_PLAINTEXT): TextFormat {
        return getByExtension(extension)
            ?: _formats.firstOrNull { it.id == fallbackId }
            ?: TextFormat(id = fallbackId, name = "Plain Text", defaultExtension = ".txt")
    }

    fun detectByFilename(filename: String, fallbackId: String = TextFormat.ID_PLAINTEXT): TextFormat {
        val ext = filename.substringAfterLast('.', "")
        return if (ext.isNotEmpty()) {
            detectByExtension(ext, fallbackId)
        } else {
            _formats.firstOrNull { it.id == fallbackId }
                ?: TextFormat(id = fallbackId, name = "Plain Text", defaultExtension = ".txt")
        }
    }

    fun getFormatsByExtension(extension: String): List<TextFormat> {
        val clean = extension.trim().lowercase().let { if (it.startsWith(".")) it else ".$it" }
        return _formats.filter { format ->
            format.extensions.any { it.equals(clean, ignoreCase = true) }
        }
    }

    fun isSupported(formatId: String): Boolean = getById(formatId) != null

    fun isExtensionSupported(extension: String): Boolean = getByExtension(extension) != null

    fun getFormatNames(): List<String> = _formats.map { it.name }

    fun getAllExtensions(): List<String> = _formats.flatMap { it.extensions }.distinct()

    fun clear() {
        _formats.clear()
    }
}
