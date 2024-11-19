package com.cs407.tapntab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CouponFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_coupon, container, false)

        // Sample coupons
        val couponList = listOf(
            Coupon("50% Off", "Get 50% off on your next purchase!", R.drawable.app_logo),
            Coupon("Buy 1 Get 1", "Buy one item and get another free!", R.drawable.app_logo),
            Coupon("Free Shipping", "Enjoy free shipping on orders above $50!", R.drawable.app_logo)
        )

        // RecyclerView setup
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CouponAdapter(couponList)

        return view
    }
}
