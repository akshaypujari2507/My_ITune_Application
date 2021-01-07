package com.myituneapplication.home

import androidx.recyclerview.widget.RecyclerView
import com.myituneapplication.R
import com.myituneapplication.databinding.ItemPlaySongBinding
import com.myituneapplication.databinding.ItemSearchedSongBinding
import com.myituneapplication.util.toCurrency
import com.myituneapplicationsource.cache.SongEntity
import com.squareup.picasso.Picasso

class SearchResultViewHolder(private val binder: ItemSearchedSongBinding) : RecyclerView.ViewHolder(binder.root) {

    fun init(item: SongEntity) {
        binder.song = item
        binder.apply {
            songTitle.text = item.name
            price.text = item.price.toCurrency(item.currency)
            songShortDesc.text = item.shortDesc
            genre.text = item.genre
            Picasso.get()
                .load(item.image)
                .placeholder(R.drawable.ic_film)
                .centerCrop()
                .fit()
                .into(songImage)
        }
    }

    fun clear() {
        binder.song = null
        binder.apply {
            songImage.setImageBitmap(null)
            songTitle.text = null
            price.text = null
            songShortDesc.text = null
            genre.text = null
        }
    }
}

class PlayResultViewHolder(val binder: ItemPlaySongBinding) : RecyclerView.ViewHolder(binder.root)