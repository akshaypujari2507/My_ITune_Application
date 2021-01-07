package com.myituneapplication.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.myituneapplicationsource.repository.SongRepository
import com.myituneapplicationutil.SingleLiveEvent

class HomeViewModel : ViewModel() {
    private val repository = SongRepository()
    private val search = MutableLiveData<String>()
    private val selectedSong = MutableLiveData<String>()

    private val searchBundle = Transformations.map(search) {
        if (it.isEmpty()) {
            repository.getSongs("star")
        } else {
            repository.getSongs(it)
        }
    }

    val searchedSongs = Transformations.switchMap(searchBundle) { it.boundary }
    val playSongs = repository.getPlayHistory()
    val viewedSong = Transformations.switchMap(selectedSong) { repository.getSong(it) }

    private val _openFragment = SingleLiveEvent<Void>()
    val openFragment: LiveData<Void>
        get() {
            return _openFragment
        }

    fun search(keyword: String) {
        if (search.value != keyword) search.value = keyword
    }

    fun openSong(songId: String) {
        _openFragment.call()
        selectedSong.value = songId
    }

    fun setCount(count: Int) {
        searchBundle.value?.itemCount?.invoke(count)
    }
}