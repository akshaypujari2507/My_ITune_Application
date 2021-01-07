package com.myituneapplication.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.myituneapplication.MainActivity
import com.myituneapplication.R
import com.myituneapplication.databinding.FragmentSongDetailsBinding
import com.myituneapplication.util.toCurrency
import com.squareup.picasso.Picasso


class SongDetailsFragment : Fragment() {

    private lateinit var binder: FragmentSongDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_song_details, container, false)

        val mHeight = activity!!.windowManager.defaultDisplay.height
        binder.verticalSpan.layoutParams.height = (mHeight * 0.6).toInt()

        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as MainActivity
        val viewModel = activity.homeViewModel
        viewModel.viewedSong.observe(this, Observer { entity ->
            if (entity != null) {
                binder.apply {
                    songTitle.text = entity.name
                    songDesc.text = entity.longDesc
                    songPrice.text = entity.price.toCurrency(entity.currency)
                    songActor.text = entity.actor
                    songGenre.text = entity.genre
                    Picasso.get()
                        .load(entity.image)
                        .centerCrop()
                        .fit()
                        .into(songImage)

                    playVideo.setOnClickListener {
                        if (entity.preview != null) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(entity.preview, "audio/*")
                            startActivity(intent)
                        }
                    }

                    songTitle.paint
                }
            }
        })
    }

}