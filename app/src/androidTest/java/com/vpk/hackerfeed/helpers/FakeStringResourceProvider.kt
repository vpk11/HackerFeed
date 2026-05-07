package com.vpk.hackerfeed.helpers

import com.vpk.hackerfeed.data.provider.StringResourceProvider

class FakeStringResourceProvider : StringResourceProvider {
    override fun getString(resId: Int): String = "string_$resId"
    override fun getString(resId: Int, vararg formatArgs: Any): String =
        "string_$resId(${formatArgs.joinToString()})"
}
