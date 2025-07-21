package com.vpk.hackerfeed.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converters for Room database.
 * Handles conversion between complex types and database-compatible types.
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<Long>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Long>?): String? {
        return if (list == null) null else gson.toJson(list)
    }
}
