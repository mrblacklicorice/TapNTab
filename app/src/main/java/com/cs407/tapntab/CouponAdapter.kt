package com.cs407.tapntab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CouponAdapter(private val couponList: List<Coupon>) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    class CouponViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val logoImageView: ImageView = view.findViewById(R.id.coupon_image)
        val titleTextView: TextView = view.findViewById(R.id.coupon_title)
        val descriptionTextView: TextView = view.findViewById(R.id.coupon_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.coupon, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = couponList[position]
        holder.logoImageView.setImageResource(coupon.logoResId)
        holder.titleTextView.text = coupon.title
        holder.descriptionTextView.text = coupon.description
    }

    override fun getItemCount(): Int {
        return couponList.size
    }
}
