/*#######################################################
 *
 * SPDX-FileCopyrightText: 2025 Milos Vasic
 * SPDX-License-Identifier: Apache-2.0
 *
 * Kotlin Multiplatform Parse Options Builder
 *
 *########################################################*/
package digital.vasic.formatters

/**
 * Builder for common parsing configuration options.
 */
class ParseOptions {
    private val options = mutableMapOf<String, Any>()

    fun enableLineNumbers(enable: Boolean = true): ParseOptions {
        options["lineNumbers"] = enable
        return this
    }

    fun enableHighlighting(enable: Boolean = true): ParseOptions {
        options["highlighting"] = enable
        return this
    }

    fun setBaseUrl(url: String): ParseOptions {
        options["baseUrl"] = url
        return this
    }

    fun set(key: String, value: Any): ParseOptions {
        options[key] = value
        return this
    }

    fun build(): Map<String, Any> = options.toMap()

    companion object {
        fun create(): ParseOptions = ParseOptions()
    }
}
