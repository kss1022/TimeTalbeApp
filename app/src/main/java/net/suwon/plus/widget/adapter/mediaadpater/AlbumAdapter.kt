package net.suwon.plus.widget.adapter.mediaadpater

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import net.suwon.plus.data.entity.media.AlbumItem
import net.suwon.plus.data.entity.media.Selection
import net.suwon.plus.databinding.ViewholderAlbumBinding
import net.suwon.plus.widget.adapter.mediaadpater.AlbumClickListener
import net.suwon.plus.widget.adapter.mediaadpater.AlbumViewHolder

class AlbumAdapter(private val context : Context, private val clickListener: AlbumClickListener) :
    ListAdapter<AlbumItem, AlbumViewHolder<ViewholderAlbumBinding>>(diffUtil) {

    var selection: Selection? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder<ViewholderAlbumBinding> =
        AlbumViewHolder(
            ViewholderAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            context
        )

    override fun onBindViewHolder(holder: AlbumViewHolder<ViewholderAlbumBinding>, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindData(item)
            holder.bindView(item, clickListener)
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AlbumItem>() {
            override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
                return oldItem.album.bucketId == newItem.album.bucketId
            }

            override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
                return oldItem.album == newItem.album
            }
        }
    }


}