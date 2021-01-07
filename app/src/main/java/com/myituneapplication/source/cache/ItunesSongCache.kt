package com.myituneapplicationsource.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myituneapplication.AppApplication
import com.myituneapplication.source.cache.dao.PlayHistoryDao
import com.myituneapplicationsource.cache.dao.SongDao

@Database(
    entities = [SongEntity::class,
        SearchResultsEntity::class,
        PlayHistoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [
        DateConverter::class,
        UriConverter::class
    ]
)
abstract class ItunesSongCache : RoomDatabase() {

    abstract fun songDao(): SongDao

    abstract fun playHistoryDao(): PlayHistoryDao

    companion object {
        const val DATABASE_NAME = "ItunesSongCache"

        val database = Room.databaseBuilder(
            AppApplication.globalContext,
            ItunesSongCache::class.java,
            DATABASE_NAME
        ).build()
    }
}
