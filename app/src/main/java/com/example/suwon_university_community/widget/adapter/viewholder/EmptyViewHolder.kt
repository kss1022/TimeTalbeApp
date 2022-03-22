package com.example.suwon_university_community.widget.adapter.viewholder

import com.example.suwon_university_community.databinding.ViewholderEmptyBinding
import com.example.suwon_university_community.model.Model
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener

class EmptyViewHolder(
    binding : ViewholderEmptyBinding,
    viewModel : BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<Model>(binding, viewModel,resourcesProvider) {

    override fun reset() = Unit

    override fun bindViews(model: Model, adapterListener: AdapterListener) = Unit
}