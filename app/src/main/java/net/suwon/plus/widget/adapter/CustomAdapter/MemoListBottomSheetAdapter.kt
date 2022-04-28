package net.suwon.plus.widget.adapter.CustomAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import net.suwon.plus.databinding.ViewholderMemoListBottomSheetBinding
import net.suwon.plus.model.FolderModel

class MemoListBottomSheetAdapter(private val folderModelList: List<FolderModel>) :
    RecyclerView.Adapter<MemoListBottomSheetAdapter.MemoListBottomSheetViewHolder>() {

    var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoListBottomSheetViewHolder =
        MemoListBottomSheetViewHolder(
            ViewholderMemoListBottomSheetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: MemoListBottomSheetViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            val beforeSelected = selectedPosition
            selectedPosition = position

            notifyItemChanged(beforeSelected)
            notifyItemChanged(selectedPosition)
        }

        holder.bind(folderModelList[position], position)
    }

    override fun getItemCount(): Int = folderModelList.size

    inner class MemoListBottomSheetViewHolder(val binding: ViewholderMemoListBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(folderModel: FolderModel, position: Int) = with(binding) {
            folderNameTextView.text = folderModel.name

            if (position == selectedPosition){
                checkboxImageView.visibility = View.VISIBLE
            }else{
                checkboxImageView.isGone = true
            }
        }
    }

    fun getSelectedFolderId( ) : Long = folderModelList[selectedPosition].id
}

