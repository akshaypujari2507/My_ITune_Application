package com.myituneapplication.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myituneapplication.databinding.ItemPlaySongBinding
import com.myituneapplicationsource.cache.SongEntity
import com.squareup.picasso.Picasso

class PlaySongAdapter(private val viewModel: HomeViewModel) : RecyclerView.Adapter<PlayResultViewHolder>() {

    var songs: List<SongEntity>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = songs?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemPlaySongBinding.inflate(inflater, parent, false)
        view.parent.setOnClickListener {
            view.song?.let {
                viewModel.openSong(it.id)
            }
        }
        return PlayResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayResultViewHolder, position: Int) {
        val item = songs?.get(position)!!
        holder.binder.apply {
            song = item
            songTitle.text = item.name
            Picasso.get()
                .load(item.image)
                .centerCrop()
                .fit()
                .into(songImage)
        }
    }

}