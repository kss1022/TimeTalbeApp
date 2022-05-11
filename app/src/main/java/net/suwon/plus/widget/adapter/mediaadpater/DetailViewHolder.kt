package net.suwon.plus.widget.adapter.mediaadpater

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.MediaStoreSignature
import net.suwon.plus.data.entity.media.MediaItem
import net.suwon.plus.data.entity.media.Selection
import net.suwon.plus.databinding.ViewholderMediaDetailBinding

open class DetailViewHolder<VB : ViewBinding>(
    private val binding: ViewholderMediaDetailBinding,
    private val selection: Selection?
) : RecyclerView.ViewHolder(binding.root) {


    open fun bindData(item: MediaItem) {
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

    open fun bindView(item: MediaItem) {
    }


}