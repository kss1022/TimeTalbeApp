package net.suwon.plus.widget.adapter.viewholder

import net.suwon.plus.databinding.ViewholderEmptyBinding
import net.suwon.plus.model.Model
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    binding : ViewholderEmptyBinding,
    viewModel : BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<Model>(binding, viewModel,resourcesProvider) {

    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit
}