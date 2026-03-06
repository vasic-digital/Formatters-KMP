/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Kotlin Multiplatform Parser Registry
 * Thread-safe registry for text parsers with lazy loading
 *
 *########################################################*/
package digital.vasic.formatters

/**
 * Registry for text parsers with eager and lazy registration.
 *
 * Manages parser instances and factories. Parsers registered lazily
 * are only instantiated on first access.
 */
class ParserRegistry {
    private val parsers = mutableMapOf<String, TextParser>()
    private val parserFactories = mutableMapOf<String, () -> TextParser>()

    fun register(parser: TextParser) {
        val formatId = parser.supportedFormat.id
        if (parsers.containsKey(formatId) || parserFactories.containsKey(formatId)) {
            throw IllegalArgumentException("Parser for format '$formatId' is already registered")
        }
        parsers[formatId] = parser
    }

    fun registerLazy(formatId: String, factory: () -> TextParser) {
        if (parsers.containsKey(formatId) || parserFactories.containsKey(formatId)) {
            throw IllegalArgumentException("Parser for format '$formatId' is already registered")
        }
        parserFactories[formatId] = factory
    }

    fun getParser(format: TextFormat): TextParser? {
        val formatId = format.id
        parsers[formatId]?.let { return it }

        val factory = parserFactories[formatId]
            ?: return parsers.values.firstOrNull { it.canParse(format) }

        val parser = factory()
        parsers[formatId] = parser
        parserFactories.remove(formatId)
        return parser
    }

    fun getParser(formatId: String, formatRegistry: FormatRegistry): TextParser? {
        val format = formatRegistry.getById(formatId) ?: return null
        return getParser(format)
    }

    fun hasParser(format: TextFormat): Boolean {
        val formatId = format.id
        return parsers.containsKey(formatId) ||
            parserFactories.containsKey(formatId) ||
            parsers.values.any { it.canParse(format) }
    }

    fun getAllParsers(): List<TextParser> = parsers.values.toList()

    fun getPendingParserCount(): Int = parserFactories.size

    fun getInstantiatedParserCount(): Int = parsers.size

    fun clear() {
        parsers.clear()
        parserFactories.clear()
    }
}
