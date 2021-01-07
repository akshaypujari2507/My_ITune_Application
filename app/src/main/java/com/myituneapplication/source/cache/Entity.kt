package com.myituneapplicationsource.cache

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey @ColumnInfo(name = "song_id") var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "currency") var currency: String,
    @ColumnInfo(name = "short_desc") var shortDesc: String?,
    @ColumnInfo(name = "long_desc") var longDesc: String?,
    @ColumnInfo(name = "release_date") var releaseDate: Date,
    @ColumnInfo(name = "genre") var genre: String,
    @ColumnInfo(name = "actor") var actor: String,
    @ColumnInfo(name = "preview") var preview: Uri?,
    @ColumnInfo(name = "image") var image: Uri?
)

@Entity(
    tableName = "play_history",
    foreignKeys = [ForeignKey(
        entity = SongEntity::class,
        parentColumns = ["song_id"],
        childColumns = ["song_id"]
    )]
)
data class PlayHistoryEntity(
    @PrimaryKey @ColumnInfo(name = "song_id") var songId: String,
    @ColumnInfo(name = "last_play") var lastPlay: Date
)

@Entity(
    tableName = "search_results",
    primaryKeys = ["song_id", "keyword"],
    foreignKeys = [ForeignKey(
        entity = SongEntity::class,
        parentColumns = ["song_id"],
        childColumns = ["song_id"]
    )]
)
data class SearchResultsEntity(
    @ColumnInfo(name = "keyword") var keyword: String,
    @ColumnInfo(name = "song_id") var songId: String
)