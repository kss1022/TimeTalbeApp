package com.example.suwon_university_community.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.suwon_university_community.model.Model
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener

abstract class ModelViewHolder< M : Model> (
    binding : ViewBinding,
    protected val viewModel: BaseViewModel,
    protected val resourcesProvider: ResourceProvider
) : RecyclerView.ViewHolder(binding.root){

    abstract fun reset()

    open fun bindData( model : M){
        reset()
    }

    abstract fun bindViews( model: M, adapterListener: AdapterListener)
}