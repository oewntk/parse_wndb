/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Normalized string with possible suffix (adj position) stripped
 * - underscore converted to space
 * - double single quote converted to double quote
 * - adjective position ```(a,ip,p)``` trimmed
 *
 * @author Bernard Bou
 */
class TrimmedNormalizedString : NormalizedString {

    /**
     * Constructor
     *
     * @param normalized string with possible suffix
     */
    constructor(normalized: NormalizedString) : super(normalized) {
        // remove possible trailing adj position between parentheses
        this.normalized = strip(this.normalized)
    }

    /**
     * Constructor
     *
     * @param raw string with possible suffix
     */
    constructor(raw: String) : super(raw) {
        // remove possible trailing adj position between parentheses
        this.normalized = strip(this.normalized)
    }

    companion object {

        fun strip(str: String): String {
            return str.replace("\\(.*\\)".toRegex(), "")
        }
    }
}
