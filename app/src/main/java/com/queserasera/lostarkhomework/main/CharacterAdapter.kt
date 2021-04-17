package com.queserasera.lostarkhomework.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.queserasera.lostarkhomework.databinding.ItemCharacterBinding

/**
 * Created by seheelee on 2021-04-07.
 */

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>(){
    var characterList : MutableList<Character> = mutableListOf()
    var selectedIndex : Int = 0
        set(value) {
            val originalSelectedIndex = selectedIndex
            field = value
            notifyItemChanged(originalSelectedIndex)
            notifyItemChanged(value)
        }
    class CharacterViewHolder(val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.binding.run {
            name = characterList[position % characterList.size].name
            level = characterList[position % characterList.size].level
            isSelected = position == selectedIndex

            root.setOnClickListener {
                selectedIndex = position
            }
        }
    }

    fun onArrowDownClicked(){
        selectedIndex = (selectedIndex + 1).coerceAtMost(itemCount)
    }

    fun onArrowUpClicked() {
        selectedIndex = (selectedIndex - 1).coerceAtLeast(0)
    }

    override fun getItemCount(): Int = characterList.size
}