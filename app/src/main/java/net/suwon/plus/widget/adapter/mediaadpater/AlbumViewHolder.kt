package net.suwon.plus.widget.adapter.mediaadpater

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import net.suwon.plus.R
import net.suwon.plus.data.entity.media.AlbumItem
import net.suwon.plus.databinding.ViewholderAlbumBinding
import net.suwon.plus.util.MeasureUtil
import net.suwon.plus.util.glide.RoundedCornersLineOverDrawTransFormation

open class AlbumViewHolder<VB : ViewBinding>(
    private val binding: ViewholderAlbumBinding,
    context: Context
) : RecyclerView.ViewHolder(binding.root) {

    private val folderRadius =
        MeasureUtil.getDimension(context, resId = R.dimen.gallery_folder_corner_radius)
    private val outlineWidth =
        MeasureUtil.getDimension(context, resId = R.dimen.gallery_folder_outline_width)
    private val outlineColor = ContextCompat.getColor(context, R.color.light_white)


    open fun bindData(item: AlbumItem) {
        Glide.with(binding.image)
            .load(item.getRecentMediaUri())
            .transform(
                CenterCrop(),
                RoundedCornersLineOverDrawTransFormation(
                    folderRadius.toInt(),
                    outlineWidth,
                    outlineColor
                )
            )
            .into(binding.image)

        binding.name.text = item.album.name
        binding.count.text = item.album.count.toString()
    }

    open fun bindView(item: AlbumItem, clickListener: AlbumClickListener) {
        binding.image.setOnClickListener {
            clickListener.itemClick(item)
        }
    }


}