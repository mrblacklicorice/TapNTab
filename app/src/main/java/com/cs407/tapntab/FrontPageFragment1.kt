package com.cs407.tapntab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FrontPageFragment1 : Fragment() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var popularRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_front_page1, container, false)

        // Initialize RecyclerViews
        favoritesRecyclerView = rootView.findViewById(R.id.favoritesRecyclerView)
        popularRecyclerView = rootView.findViewById(R.id.popularRecyclerView)

        setupFavoritesRecyclerView()
        setupPopularRecyclerView()

        return rootView
    }

    private fun setupFavoritesRecyclerView() {
        val favorites = listOf(
            Bar("Whiskey Jack's Saloon", imageResId = R.drawable.whiskey_jacks_main),
            Bar("Danny's Pub", imageResId = R.drawable.dannys_pub_main),
            Bar("Brat's Madison", imageResId = R.drawable.brats_madison_main),
            Bar("Monday's", imageResId = R.drawable.mondays_main)
        )
        favoritesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        favoritesRecyclerView.adapter = BarAdapter(favorites, isHorizontal = true)
    }

    private fun setupPopularRecyclerView() {
        val popular = listOf(
            Bar("Brat's Madison", imageResId = R.drawable.brats_madison_main, upvotes = 15),
            Bar("Whiskey Jack's Saloon", imageResId = R.drawable.whiskey_jacks_main, upvotes = 20)
        )
        popularRecyclerView.layoutManager = LinearLayoutManager(context)
        popularRecyclerView.adapter = BarAdapter(popular)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FrontPageFragment1()
    }
}
