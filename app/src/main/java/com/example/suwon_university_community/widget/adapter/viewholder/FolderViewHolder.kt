package com.example.suwon_university_community.widget.adapter.viewholder

import com.example.suwon_university_community.databinding.ViewholderFolderBinding
import com.example.suwon_university_community.model.FolderModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import com.example.suwon_university_community.widget.adapter.listener.FolderListAdapterListener

class FolderViewHolder(
    private val binding: ViewholderFolderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<FolderModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: FolderModel)  = with(binding){
        super.bindData(model)

        folderNameTextView.text = model.name
        folderCountTextView.text = model.count.toString()
    }

    override fun bindViews(model: FolderModel, adapterListener: AdapterListener) {
        if(adapterListener is FolderListAdapterListener){
            binding.root.setOnClickListener {
                adapterListener.selectFolder(model)
            }

            binding.editButton.setOnClickListener {  adapterListener.selectEdit(model) }
            binding.deleteButton.setOnClickListener { adapterListener.selectDelete(model) }
        }
    }
}