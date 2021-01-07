package com.myituneapplication.source.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myituneapplicationsource.cache.PlayHistoryEntity
import com.myituneapplicationsource.cache.SongEntity

@Dao
abstract class PlayHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(song: PlayHistoryEntity)

    @Query("SELECT songs.* FROM songs INNER JOIN play_history ON songs.song_id = play_history.song_id ORDER BY play_history.last_play DESC")
    abstract fun getSongHistory(): LiveData<List<SongEntity>>
}