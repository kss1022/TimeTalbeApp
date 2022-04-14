package com.example.suwon_university_community.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.suwon_university_community.databinding.ViewholderNoticeBinding
import com.example.suwon_university_community.extensions.fromDpToPx
import com.example.suwon_university_community.model.NoticeModel
import com.example.suwon_university_community.model.NoticeDateModel
import com.example.suwon_university_community.util.provider.ResourceProvider

class NoticeAdapter ( private val resourceProvider: ResourceProvider) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: List<DataItem> = emptyList()
    var onItemClickListener: ((NoticeModel) -> Unit)? = null
    var onItemLongClickListener: ((NoticeModel) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType){
            ITEM_VIEW_TYPE_DATE->{
                NoticeDateViewHolder(parent.context)
            }


            ITEM_VIEW_TYPE_CONTENT->{
                NoticeModelViewHolder(
                    ViewholderNoticeBinding.inflate(LayoutInflater.from(parent.context),parent, false )
                )
            }

            else -> throw RuntimeException("알 수 없는 ViewType 입니다.")
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemValue = data[position].value

        when{
            (holder is NoticeDateViewHolder && itemValue is String) ->{
                holder.bind(itemValue)
            }

            (holder is NoticeModelViewHolder && itemValue is NoticeModel)->{
                holder.bind(itemValue)
            }

            else -> throw RuntimeException("알 수 없는 ViewType 입니다.")
        }
    }


    override fun getItemViewType(position: Int): Int = when (data[position].value) {
        is String -> ITEM_VIEW_TYPE_DATE
        else -> ITEM_VIEW_TYPE_CONTENT
    }


    override fun getItemCount(): Int = data.size


    fun addData(noticeDateModelList: List<NoticeDateModel>) {
        val newData = mutableListOf<DataItem>()

        noticeDateModelList.forEach { model ->

            model.date.let {
                newData += DataItem(model.getDate())
            }


            model.noticeModel.forEach {
                newData += DataItem(it)
            }
        }

        data = newData
    }


    class NoticeDateViewHolder(context: Context) : RecyclerView.ViewHolder(
        TextView(context).apply {
            setPadding(24.fromDpToPx(), 12.fromDpToPx(), 24.fromDpToPx(), 6.fromDpToPx())
        }) {

        fun bind(title: String) {
            (itemView as? TextView)?.text = title
        }
    }


    inner class NoticeModelViewHolder(private val binding: ViewholderNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                (data[adapterPosition].value as? NoticeModel)?.let {
                    onItemClickListener?.invoke(it)
                }
            }

            binding.root.setOnLongClickListener {
                (data[adapterPosition].value as? NoticeModel)?.let {
                    onItemLongClickListener?.invoke(it)
                }
                false
            }
        }

        fun bind(noticeModel: NoticeModel) = with(binding) {
            noticeTitleTextView.text = noticeModel.title
            noticeWriterTextView.text = noticeModel.writer
            noticeCategoryTextView.text = resourceProvider.getString(noticeModel.category.categoryNameId)
        }
    }


    data class DataItem(val value: Any)

    companion object {
        private const val ITEM_VIEW_TYPE_DATE = 0
        private const val ITEM_VIEW_TYPE_CONTENT = 1
    }
}

