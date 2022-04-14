package com.example.suwon_university_community.widget.adapter.viewholder

import com.example.suwon_university_community.databinding.ViewholderMemoBinding
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import com.example.suwon_university_community.widget.adapter.listener.MemoListAdapterListener

class MemoViewHolder(
    private val binding : ViewholderMemoBinding,
    viewModel : BaseViewModel,
    resourceProvider: ResourceProvider
    ) : ModelViewHolder<MemoModel>(binding, viewModel, resourceProvider){

    override fun reset() = Unit

    override fun bindData(model: MemoModel)  = with(binding){
        super.bindData(model)

        titleTextView.text = model.title
        memoPreviewTextView.text = model.memo
        lastEditTextView.text = model.time
    }

    override fun bindViews(model: MemoModel, adapterListener: AdapterListener) = with(binding){
            if(adapterListener is MemoListAdapterListener){
                root.setOnClickListener {
                    adapterListener.selectModel(model)
                }
            }
    }
}