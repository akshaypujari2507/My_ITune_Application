package com.myituneapplication.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.myituneapplication.databinding.ItemSearchedSongBinding
import com.myituneapplicationsource.cache.SongEntity

private object DifferSearchSong : DiffUtil.ItemCallback<SongEntity>() {
    override fun areItemsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SongEntity, newItem: SongEntity): Boolean {
        return oldItem == newItem
    }
}

class SearchedSongsAdapter(private val viewModel: HomeViewModel) :
    PagedListAdapter<SongEntity, SearchResultViewHolder>(DifferSearchSong) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemSearchedSongBinding.inflate(inflater, parent, false)
        view.parent.setOnClickListener {
            view.song?.let {
                viewModel.openSong(it.id)
            }
        }
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = getItem(position)
        // When null it means SearchResultViewHolder is a Placeholder
        if (item != null) {
            holder.init(item)
        } else {
            holder.clear()
        }
    }

}