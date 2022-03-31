package com.example.suwon_university_community.widget.adapter.viewholder

import com.example.suwon_university_community.R
import com.example.suwon_university_community.databinding.ViewholderTimeTableBinding
import com.example.suwon_university_community.model.TimeTableModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import com.example.suwon_university_community.widget.adapter.listener.AdapterListener
import com.example.suwon_university_community.widget.adapter.listener.TimeTableListAdapterListener

class TimeTableViewHolder(
    private val binding: ViewholderTimeTableBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourceProvider
) : ModelViewHolder<TimeTableModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: TimeTableModel) = with(binding) {
        super.bindData(model)

        seasonTextView.text =
            resourcesProvider.getString(R.string.timetable_season, model.year, model.semester)
        tableNameTextView.text =
            if (model.tableName.isEmpty()) resourcesProvider.getString(R.string.time_table) else model.tableName

    }


    override fun bindViews(model: TimeTableModel, adapterListener: AdapterListener) {
        if (adapterListener is TimeTableListAdapterListener) {
            binding.root.setOnClickListener {
                adapterListener.changeTimeTable(model)
            }

            binding.editButton.setOnClickListener {
                adapterListener.editTimeTable(model)
            }

            binding.deleteButton.setOnClickListener {
                adapterListener.deleteTimeTable(model)
            }
        }
    }

}