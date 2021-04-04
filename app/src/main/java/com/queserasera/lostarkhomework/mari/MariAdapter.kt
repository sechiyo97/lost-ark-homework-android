package com.queserasera.lostarkhomework.mari

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.queserasera.lostarkhomework.databinding.ItemMariBinding

class MariAdapter : RecyclerView.Adapter<MariAdapter.MariViewHolder>(){
    var mariSet : MariItemSet? = null
    var selectedTab : Int = 0
    class MariViewHolder(val binding: ItemMariBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MariViewHolder {
        val binding = ItemMariBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MariViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MariViewHolder, position: Int) {
        mariSet ?: return
        when (selectedTab) {
            0 -> {
                holder.binding.name = mariSet!!.t3[position].name
                holder.binding.price = mariSet!!.t3[position].price
                holder.binding.imageSrc = mariSet!!.t3[position].imageSrc
            }
            1 -> {
                holder.binding.name = mariSet!!.t1t2[position].name
                holder.binding.price = mariSet!!.t1t2[position].price
                holder.binding.imageSrc = mariSet!!.t1t2[position].imageSrc
            }
        }
    }

    override fun getItemCount(): Int = mariSet?.t1t2?.size ?: 0
}