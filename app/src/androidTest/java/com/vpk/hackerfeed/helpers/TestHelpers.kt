package com.vpk.hackerfeed.helpers

import android.content.Context
import androidx.room.Room
import com.vpk.hackerfeed.database.AppDatabase

object TestHelpers {
    fun createInMemoryDb(context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}
