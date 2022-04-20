package com.example.suwon_university_community.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.suwon_university_community.model.CellType
import com.example.suwon_university_community.model.Model
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import com.example.suwon_university_community.widget.adapter.viewholder.ModelViewHolder


class ModelRecyclerViewAdapter<M : Model, VM : BaseViewModel>(
    private var modelList: List<Model>,
    private val viewModel: VM,
    private val resourcesProvider: ResourceProvider,
    private val adapterListener: AdapterListener
) : ListAdapter<Model, ModelViewHolder<M>>(Model.DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder<M> =
        ModelViewHolderMapper.map<M>(
            parent,
            CellType.values()[viewType],
            viewModel,
            resourcesProvider
        )


    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ModelViewHolder<M>, position: Int) {
        with(holder) {
            bindData(modelList[position] as M)
            bindViews(modelList[position] as M, adapterListener)
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    override fun getItemViewType(position: Int): Int = modelList[position].type.ordinal


    override fun submitList(list: List<Model>?) {
        list?.let {
            modelList = it
        }
        super.submitList(list)
    }

    fun removeAll() {
        modelList = listOf()
        super.submitList(listOf())
    }
}