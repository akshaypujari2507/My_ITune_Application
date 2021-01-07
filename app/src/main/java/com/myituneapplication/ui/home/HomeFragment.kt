package com.myituneapplication.home

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.myituneapplication.MainActivity
import com.myituneapplication.R
import com.myituneapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private lateinit var binder: FragmentHomeBinding
    private lateinit var activity: Activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as MainActivity
        val viewModel = activity.homeViewModel

        val playSongsAdapter = PlaySongAdapter(viewModel)
        val searchedSongsAdapter = SearchedSongsAdapter(viewModel)

        binder.playSongs.apply {
            adapter = playSongsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binder.searchedSongs.apply {
            adapter = searchedSongsAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

//        viewModel.search("star")
        viewModel.playSongs.observe(this, Observer {
            if (it.isEmpty()) {
                binder.recentLabel.visibility = View.GONE
                binder.playSongs.visibility = View.GONE
            } else {
                binder.recentLabel.visibility = View.VISIBLE
                binder.playSongs.visibility = View.VISIBLE
            }

            playSongsAdapter.songs = it
        })
        viewModel.searchedSongs.observe(this, Observer {
            viewModel.setCount(it.size)
            searchedSongsAdapter.submitList(it)
            binder.progressBar.visibility = View.GONE
        })

        binder.searchBox.setOnEditorActionListener { _, actionId, _ ->
            val keyword = binder.searchBox.text.toString()
            if ((actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT)
                && keyword.isNotEmpty()) {
                binder.progressBar.visibility = View.VISIBLE
                viewModel.search(keyword)
                hideKeyboard(binder.searchBox)
                true
            }
            false
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}