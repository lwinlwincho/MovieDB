package com.llc.moviedb.ui.home.detail_movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boyzdroizy.library.tooltipopwordtv.listeners.SelectableWordListeners
import com.boyzdroizy.library.tooltipopwordtv.tooltipopupWindows.ToolPopupWindows
import com.llc.moviebd.R
import com.llc.moviebd.databinding.ItemDescriptionBinding
import com.llc.moviedb.data.model.Description

class DescriptionItemAdapter(private val delegate: DescriptionDelegate) :
    ListAdapter<Description, DescriptionItemAdapter.DescriptionViewHolder>(DiffCallBack) {

    interface DescriptionDelegate{
        fun onTapDescription(view : View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        return DescriptionViewHolder(
            ItemDescriptionBinding.inflate(LayoutInflater.from(parent.context)),
            delegate
        )
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val item: Description = getItem(position)
        holder.bind(item)
    }

    class DescriptionViewHolder(
        private val binding: ItemDescriptionBinding,
        private val delegate: DescriptionDelegate
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieDescription: Description) {

/*            val spannableString = SpannableString(movieDescription.overview)
            if (movieDescription.highlight) {
                spannableString.setSpan(
                    UnderlineSpan(),
                    0,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.light_purple
                        )
                    ),
                    0,
                    spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.root.setOnClickListener {
                    delegate.onTapCategory(binding.root)
                }
            }
            binding.tvDescription.text = spannableString*/

            binding.tvDescription.apply {
                text = movieDescription.overview
                setToolTipListener(object : SelectableWordListeners{
                    override fun onWordSelected(
                        anchorView: TextView,
                        wordSelected: String,
                        lineNumber: Int,
                        width: Int
                    ) {
                        val toolPopupWindows = ToolPopupWindows.ToolTipBuilder(context)
                            .setToolTipListener { Toast.makeText(context, "dismissed", Toast.LENGTH_SHORT).show() }
                            .setTitleTextColor(ContextCompat.getColor(context,R.color.dark_blue))
                            .setTitleTextSize(20f)
                            .setBackgroundColor(ContextCompat.getColor(context,R.color.gray))
                            .setIsOutsideTouchable(false)
                            .build()
                        binding.tvDescription.showToolTipWindow(anchorView, wordSelected, lineNumber, width, toolPopupWindows)
                    }

                })
            }
           // binding.tvDescription.setBackgroundWordColor(ContextCompat.getColor(context, R.color.colorAccent))

        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Description>() {

        override fun areItemsTheSame(oldItem: Description, newItem: Description): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Description, newItem: Description): Boolean {
            return oldItem == newItem
        }
    }
}