package com.vpk.hackerfeed.data.provider

import android.content.Context

/**
 * Implementation of StringResourceProvider using Android Context.
 * This provides string resources to components that cannot access Context directly.
 */
class AndroidStringResourceProvider(
    private val context: Context
) : StringResourceProvider {
    
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
    
    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }
}
