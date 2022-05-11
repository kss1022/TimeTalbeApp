package net.suwon.plus.widget.adapter.mediaadpater

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.MediaStoreSignature
import net.suwon.plus.R
import net.suwon.plus.data.entity.media.MediaItem
import net.suwon.plus.data.entity.media.Selection
import net.suwon.plus.databinding.ViewholderMediaBinding

open class MediaViewHolder<VB : ViewBinding>(
    private val binding: ViewholderMediaBinding,
    private val selection: Selection?
) : RecyclerView.ViewHolder(binding.root) {



    open fun bindData(item: MediaItem) {
        selection?.let {
            val isSelected = it.isSelected(item.getId())
            binding.dim.isVisible = isSelected

            if (isSelected) {
                binding.checked.setImageResource(R.drawable.check_circle_on_24)
            } else {
                binding.checked.setImageResource(R.drawable.check_circle_off_24)
            }
        }



        Glide.with(binding.image)
            .load(item.getUri())
            .signature(
                MediaStoreSignature(
                    item.media.mimeType,
                    item.media.dateModified,
                    item.media.orientation
                )
            )
            .into(binding.image)
    }

    open fun bindView(item: MediaItem, position : Int, clickListener: MediaClickListener) {
        binding.image.setOnClickListener {
            clickListener.itemClick(binding.root, item, position)
        }


        binding.checked.setOnClickListener {
            clickListener.checkBoxClick(item)

            selection?.let {
                val isSelected = it.isSelected(item.getId())
                binding.dim.isVisible = isSelected
                if (isSelected) {
                    binding.checked.setImageResource(R.drawable.check_circle_on_24)
                } else {
                    binding.checked.setImageResource(R.drawable.check_circle_off_24)
                }
            }

        }


    }


}