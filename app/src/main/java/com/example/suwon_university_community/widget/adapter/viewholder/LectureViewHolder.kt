package com.example.suwon_university_community.widget.adapter.viewholder

import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.ViewholderLectureBinding
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import com.example.suwon_university_community.widget.adapter.listener.LectureListAdapterListener

class LectureViewHolder(
    private val binding: ViewholderLectureBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<LectureModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: LectureModel) = with(binding) {
        super.bindData(model)

        nameTextView.text = model.name
        locationTextView.text = model.time
        detailTextView.text = resourcesProvider.getString(
            R.string.lecture_detail,
            model.distinguish ?: "",
            if (model.point?.checkFloat() == true) model.point else model.point?.toInt()  ?: "",
            model.department ?: "",
            model.major ?: "",
            model.grade ?: ""
        )

        pfNameTextView.text = model.professorName
    }

    override fun bindViews(model: LectureModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is LectureListAdapterListener) {
            root.setOnClickListener {
                adapterListener.selectLecture(model)
            }
        }
    }

    private fun Float.checkFloat(): Boolean {
        return this - this.toInt() > 0
    }
}