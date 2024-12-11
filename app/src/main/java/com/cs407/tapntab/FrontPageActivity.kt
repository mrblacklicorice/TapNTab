package com.cs407.tapntab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FrontPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_page)

        // Setup Favorites RecyclerView
        val favoritesRecyclerView: RecyclerView = findViewById(R.id.favoritesRecyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        favoritesRecyclerView.adapter = BarAdapter(getFavoritesData()) { bar ->
            onBarClick(bar)
        }

        // Setup Popular RecyclerView
        val popularRecyclerView: RecyclerView = findViewById(R.id.popularRecyclerView)
        popularRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularRecyclerView.adapter = BarAdapter(getPopularData()) { bar ->
            onBarClick(bar)
        }
    }

    // Data for Favorites Section
    private fun getFavoritesData(): List<Bar> {
        return listOf(
            Bar("Brats Madison", 7, R.drawable.fire_image),
            Bar("Whiskey Jack's Saloon", 15, R.drawable.fire_image)
        )
    }

    // Data for Most Popular Section
    private fun getPopularData(): List<Bar> {
        return listOf(
            Bar("Brats Madison", 7, R.drawable.fire_image),
            Bar("Danny's Pub", 9, R.drawable.fire_image),
            Bar("Whiskey Jack's Saloon", 15, R.drawable.fire_image)
        )
    }

    // Handle Clicks on RecyclerView Items
    private fun onBarClick(bar: Bar) {
        Toast.makeText(this, "Clicked: ${bar.name} with ${bar.votes} votes", Toast.LENGTH_SHORT).show()
    }
}
