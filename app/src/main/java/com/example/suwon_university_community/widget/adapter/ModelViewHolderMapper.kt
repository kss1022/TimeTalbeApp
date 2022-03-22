package com.example.suwon_university_community.widget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.suwon_university_community.databinding.ViewholderEmptyBinding
import com.example.suwon_university_community.databinding.ViewholderLectureBinding
import com.example.suwon_university_community.model.CellType
import com.example.suwon_university_community.model.Model
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.viewholder.EmptyViewHolder
import com.example.suwon_university_community.widget.adapter.viewholder.LectureViewHolder
import com.example.suwon_university_community.widget.adapter.viewholder.ModelViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourceProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {

            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.LECTURE_CELL -> LectureViewHolder(
                ViewholderLectureBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }

        return viewHolder as ModelViewHolder<M>
    }
}