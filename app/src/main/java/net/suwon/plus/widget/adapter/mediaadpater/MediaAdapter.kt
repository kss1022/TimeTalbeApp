package net.suwon.plus.widget.adapter.mediaadpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import net.suwon.plus.data.entity.media.MediaItem
import net.suwon.plus.data.entity.media.Selection
import net.suwon.plus.databinding.ViewholderMediaBinding

class MediaAdapter(private val clickListener: MediaClickListener) :
    PagingDataAdapter<MediaItem, MediaViewHolder<ViewholderMediaBinding>>(diffUtil) {

    var selection: Selection? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaViewHolder<ViewholderMediaBinding> =
        MediaViewHolder(
            ViewholderMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            selection
        )

    override fun onBindViewHolder(holder: MediaViewHolder<ViewholderMediaBinding>, position: Int) {
        val item = getItem(position)
        item?.let {
//            selection.isSelected(item.getId())
            holder.bindData(item)
            holder.bindView(item, position, clickListener)
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.media.id == newItem.media.id
            }

            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.media == newItem.media
            }
        }
    }
}