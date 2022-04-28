package net.suwon.plus.widget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import net.suwon.plus.databinding.*
import net.suwon.plus.model.CellType
import net.suwon.plus.model.Model
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.provider.ResourceProvider
import net.suwon.plus.widget.adapter.viewholder.*

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
            CellType.TABLE_CELL -> TimeTableViewHolder(
                ViewholderTimeTableBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOLDER_CELL -> FolderViewHolder(
                ViewholderFolderBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.MEMO_CELL -> MemoViewHolder(
                ViewholderMemoBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }

        return viewHolder as ModelViewHolder<M>
    }
}