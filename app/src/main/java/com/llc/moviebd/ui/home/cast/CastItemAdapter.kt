package com.llc.moviebd.ui.home.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.llc.moviebd.data.model.CastModel
import com.llc.moviebd.data.model.Genre
import com.llc.moviebd.databinding.ItemCastBinding
import com.llc.moviebd.databinding.ItemGenresBinding
import com.llc.moviebd.extension.loadFromUrl
import com.llc.moviebd.network.IMAGE_URL

class CastItemAdapter : ListAdapter<CastModel, CastItemAdapter.CastViewHolder>(DiffCallBack) {
    companion object DiffCallBack : DiffUtil.ItemCallback<CastModel>() {

        override fun areItemsTheSame(oldItem: CastModel, newItem: CastModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CastModel, newItem: CastModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(ItemCastBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val item: CastModel = getItem(position)
        holder.bind(item)
    }

    class CastViewHolder(private val binding: ItemCastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(castModel: CastModel) {
            binding.ivProfile.loadFromUrl(IMAGE_URL + castModel.profile_path)
            binding.tvName.text = castModel.name
        }
    }
}