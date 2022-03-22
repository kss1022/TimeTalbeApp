package com.example.suwon_university_community.widget.adapter.viewholder

import com.example.suwon_university_community.databinding.ViewholderLectureBinding
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener

class LectureViewHolder(
    private val binding: ViewholderLectureBinding,
    viewModel : BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<LectureModel>(binding, viewModel ,resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: LectureModel) = with(binding){
        super.bindData(model)

        nameTextView.text = model.name
        locationTextView.text = model.time
        detailTextView.text = "${model.distinguish},${model.grade},${model.collegeCategory},${model.departmentCategory}"
        pfNameTextView.text = model.professorName
    }

    override fun bindViews(model: LectureModel, adapterListener: AdapterListener) = with(binding) {

    }
}