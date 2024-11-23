package com.cs407.tapntab

import android.content.Context
import androidx.core.content.ContextCompat

class IconManager(private val context: Context) {
    private val iconMap: MutableMap<String, Int> = mutableMapOf()

    init {
        // Initialize the map with icon names and their drawable IDs
        iconMap["default_icon"] = R.drawable.app_logo

        iconMap["add_icon"] = R.drawable.fixed_plus_sign


        iconMap["beers_icon"] = R.drawable.beer
        iconMap["mixers_icon"] = R.drawable.mixers
        iconMap["shots_icon"] = R.drawable.shots

        iconMap["vodkamixer_icon"] = R.drawable.vodkacoke
        iconMap["corona_icon"] = R.drawable.corona
        iconMap["tequilasoda_icon"] = R.drawable.tequilasoda
    }

    fun getDrawableResourceId(iconName: String): Int? {
        return iconMap[iconName]
    }
}
