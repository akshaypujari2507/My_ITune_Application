package com.myituneapplication.boundary

import androidx.core.net.toUri
import androidx.paging.PagedList
import com.myituneapplication.IO_EXECUTOR
import com.myituneapplication.NETWORK_EXECUTOR
import com.myituneapplication.source.api.ItunesApiService
import com.myituneapplication.source.api.SongObject
import com.myituneapplication.source.boundary.PagingRequestHelper
import com.myituneapplicationsource.cache.ItunesSongCache
import com.myituneapplicationsource.cache.SearchResultsEntity
import com.myituneapplicationsource.cache.SongEntity
import com.myituneapplicationutil.toDate
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class SongBoundary(
    private val searchKeyword: String,
    private val api: ItunesApiService,
    private val db: ItunesSongCache,
    private val downloadCount: Int
) : PagedList.BoundaryCallback<SongEntity>() {

    private var itemCount = 0
    private val songDao = db.songDao()
    val helper = PagingRequestHelper(IO_EXECUTOR)

    val itemCountSignal: (Int) -> Unit = {
        itemCount = it
    }

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            NETWORK_EXECUTOR.execute {
                api.searchSong(
                    keyword = searchKeyword,
                    limit = downloadCount
                ).callbackSuccess(it) {
                    db.runInTransaction {
                        val songEntities = it.body()?.songObjects?.map(SongObject::toEntity)
                        songEntities?.run { songDao.insertSongs(this) }
                        val searchEntities = songEntities?.map { SearchResultsEntity(searchKeyword, it.id) }
                        searchEntities?.run { songDao.insertSearches(this) }
                    }
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: SongEntity) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            NETWORK_EXECUTOR.execute {
                api.searchSong(
                    keyword = searchKeyword,
                    limit = downloadCount,
                    offset = itemCount.toOffset(downloadCount)
                ).callbackSuccess(it) {
                    db.runInTransaction {
                        val songEntities = it.body()?.songObjects?.map(SongObject::toEntity)
                        songEntities?.run { songDao.insertSongs(this) }
                        val searchEntities = songEntities?.map { SearchResultsEntity(searchKeyword, it.id) }
                        searchEntities?.run { songDao.insertSearches(this) }
                    }
                }
            }
        }
    }
}

private inline fun <T> Call<T>.callbackSuccess(callback: PagingRequestHelper.Callback, func: (Response<T>) -> Unit) {
    try {
        val response = execute()
        if (response.isSuccessful) {
            func(response)
            callback.recordSuccess()
        } else {
            callback.recordFailure(HttpException(response))
        }
    } catch (ex: IOException) {

        callback.recordFailure(ex)
    }
}

private fun SongObject.toEntity() = SongEntity(
    trackId.toString(),
    trackName,
    trackPrice ?: 0.0,
    currency,
    shortDescription,
    longDescription,
    releaseDate!!.toDate(),
    primaryGenreName!!,
    artistName!!,
    previewUrl?.toUri(),
    artworkUrl100?.replace("100x100", "600x600")?.toUri() // we need high res image
)

private fun Int.toOffset(limit: Int) = if (this % limit == 0) {
    this / limit
} else {
    (this / limit) + 1
}
