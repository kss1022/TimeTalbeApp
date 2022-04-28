package net.suwon.plus.widget.adapter.viewholder

import net.suwon.plus.R
import net.suwon.plus.databinding.ViewholderTimeTableBinding
import net.suwon.plus.model.TimeTableModel
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.listener.AdapterListener
import net.suwon.plus.widget.adapter.listener.TimeTableListAdapterListener

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