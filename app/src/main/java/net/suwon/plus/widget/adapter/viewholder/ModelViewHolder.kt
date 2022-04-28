package net.suwon.plus.widget.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import net.suwon.plus.model.Model
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.listener.AdapterListener

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