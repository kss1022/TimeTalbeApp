package net.suwon.plus.widget.adapter.mediaadpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.suwon.plus.databinding.ViewholderMemoImageBinding

class MemoImageAdapter(private val clickListener: MediaImageClickListener) :
    RecyclerView.Adapter<MemoImageAdapter.MemoImageViewHolder>() {


    private var urlList = listOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoImageViewHolder =
        MemoImageViewHolder(
            ViewholderMemoImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )

    override fun onBindViewHolder(holder: MemoImageViewHolder, position: Int) {
            holder.bindData(urlList[position])
            holder.bindView(urlList[position], clickListener)
    }


    class MemoImageViewHolder( val binding : ViewholderMemoImageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(url : String){
            Glide.with(binding.image)
                .load(url)
                .into(binding.image)
        }
        fun bindView(url : String, clickListener: MediaImageClickListener){

        }
    }

    override fun getItemCount(): Int = urlList.count()

    @SuppressLint("NotifyDataSetChanged")
    fun setUrlList(list : List<String>){
        urlList = list
        notifyDataSetChanged()
    }
}