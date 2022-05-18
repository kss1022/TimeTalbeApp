package net.suwon.plus.widget.adapter.mediaadpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.suwon.plus.data.entity.memo.MemoImage
import net.suwon.plus.databinding.ViewholderMemoImageBinding

class MemoImageAdapter(private val defaultUrl : String, private val clickListener: (Int)->Unit) :
    RecyclerView.Adapter<MemoImageAdapter.MemoImageViewHolder>() {


    private var urlList = listOf<MemoImage>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoImageViewHolder =
        MemoImageViewHolder(
            ViewholderMemoImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )

    override fun onBindViewHolder(holder: MemoImageViewHolder, position: Int) {
            holder.bindData(urlList[position])
            holder.bindView(position, clickListener)
    }


    inner class MemoImageViewHolder( val binding : ViewholderMemoImageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(memoImage : MemoImage){
            if(memoImage.isSaved){
                Glide.with(binding.image)
                    .load(defaultUrl +"/" + memoImage.name)
                    .into(binding.image)
            }else{
                Glide.with(binding.image)
                    .load(memoImage.url)
                    .into(binding.image)
            }
        }

        fun bindView(position: Int, clickListener: (Int) -> Unit){
            binding.root.setOnClickListener {
                clickListener(position)
            }
        }
    }

    override fun getItemCount(): Int = urlList.count()

    @SuppressLint("NotifyDataSetChanged")
    fun setUrlList(list : List<MemoImage>){
        urlList = list
        notifyDataSetChanged()
    }
}