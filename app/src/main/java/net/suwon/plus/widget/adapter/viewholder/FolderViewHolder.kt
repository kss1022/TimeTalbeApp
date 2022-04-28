package net.suwon.plus.widget.adapter.viewholder

import net.suwon.plus.databinding.ViewholderFolderBinding
import net.suwon.plus.model.FolderModel
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.listener.AdapterListener
import net.suwon.plus.widget.adapter.listener.FolderListAdapterListener

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