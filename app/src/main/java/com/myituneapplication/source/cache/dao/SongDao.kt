package com.myituneapplicationsource.cache.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myituneapplicationsource.cache.SearchResultsEntity
import com.myituneapplicationsource.cache.SongEntity

@Dao
abstract class SongDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertSong(song: SongEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertSongs(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertSearches(genres: List<SearchResultsEntity>)

    @Query("SELECT songs.* FROM songs INNER JOIN search_results ON songs.song_id = search_results.song_id WHERE search_results.keyword = :keyword")
    abstract fun getSongsByKeyword(keyword: String): DataSource.Factory<Int, SongEntity>

    @Query("SELECT songs.* FROM songs")
    abstract fun getAllSongs(): LiveData<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE song_id = :songId")
    abstract fun getSong(songId: String): LiveData<SongEntity>
}