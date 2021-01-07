package com.myituneapplicationsource.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import com.myituneapplication.IO_EXECUTOR
import com.myituneapplication.boundary.SongBoundary
import com.myituneapplication.source.api.ItunesApi
import com.myituneapplication.source.boundary.BoundaryBundle
import com.myituneapplicationsource.cache.ItunesSongCache
import com.myituneapplicationsource.cache.SongEntity
import com.myituneapplicationsource.cache.PlayHistoryEntity
import java.util.*

class SongRepository {
    private val api = ItunesApi.api
    private val db = ItunesSongCache.database

    fun getSongs(keyword: String, itemCount: Int = 50): BoundaryBundle<SongEntity> {
        val songBoundary = SongBoundary(keyword, api, db, itemCount)
        val songFactory = db.songDao().getSongsByKeyword(keyword)
        val pageList = LivePagedListBuilder<Int, SongEntity>(songFactory, itemCount).apply {
            setBoundaryCallback(songBoundary)
        }.build()
        return BoundaryBundle(pageList, songBoundary.itemCountSignal)
    }

    fun getSong(songId: String): LiveData<SongEntity> {
        IO_EXECUTOR.execute {
            db.playHistoryDao().insert(PlayHistoryEntity(songId, Date()))
        }
        return db.songDao().getSong(songId)
    }

    fun getPlayHistory() = db.playHistoryDao().getSongHistory()

//    fun getAllSongs() = db.songDao().getAllSongs()
}