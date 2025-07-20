package com.vpk.hackerfeed.data.provider

/**
 * Interface for providing string resources.
 * This allows ViewModels to access string resources without depending on Android Context directly.
 */
interface StringResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg formatArgs: Any): String
}
