package com.example.suwon_university_community.widget.adapter.viewholder

import android.content.ClipData
import android.content.ClipDescription
import android.view.View
import com.example.suwon_university_community.databinding.ViewholderMemoBinding
import com.example.suwon_university_community.extensions.toReadableDateString
import com.example.suwon_university_community.extensions.toReadableTimeString
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import com.example.suwon_university_community.widget.adapter.listener.MemoListAdapterListener
import java.util.*

class MemoViewHolder(
    private val binding: ViewholderMemoBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourceProvider
) : ModelViewHolder<MemoModel>(binding, viewModel, resourceProvider) {

    override fun reset() = Unit

    override fun bindData(model: MemoModel) = with(binding) {
        super.bindData(model)

        titleTextView.text = model.title.replace(" ", "").replace("\n", "")


        val modelDate = Date(model.time)
        val currentDate = Date(System.currentTimeMillis())


        lastEditTextView.text =
            if (modelDate.toReadableDateString() == currentDate.toReadableDateString()) {
                modelDate.toReadableTimeString()
            } else {
                modelDate.toReadableDateString()
            }
    }

    override fun bindViews(model: MemoModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is MemoListAdapterListener) {
            root.setOnClickListener {
                adapterListener.selectModel(model)
            }

            root.id = model.id.toInt()

            root.setOnLongClickListener(longClickListener)


            binding.editButton.setOnClickListener { adapterListener.selectEdit(model) }
            binding.deleteButton.setOnClickListener { adapterListener.selectDelete(model) }
        }
    }

    companion object {
        val longClickListener = View.OnLongClickListener { v ->
            val item = ClipData.Item(v.id.toString() as? CharSequence)

            val dragData = ClipData(
                v.id.toString() as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val myShadow = View.DragShadowBuilder(v);
            v.startDragAndDrop(
                dragData,  // The data to be dragged
                myShadow,  // The drag shadow builder
                v,      // No need to use local data
                0          // Flags (not currently used, set to 0)
            )

//            v.visibility = View.INVISIBLE
            true
        }
    }


}

