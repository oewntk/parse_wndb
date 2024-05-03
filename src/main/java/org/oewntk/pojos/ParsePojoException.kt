/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.pojos

/**
 * Exception raised when Pojo object can't be parsed
 */
class ParsePojoException : Exception {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}
