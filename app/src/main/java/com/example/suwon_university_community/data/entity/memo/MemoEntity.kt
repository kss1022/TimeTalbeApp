package com.example.suwon_university_community.data.entity.memo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suwon_university_community.model.MemoModel

@Entity
data class MemoEntity (
    @PrimaryKey(autoGenerate = true)
    val memoId : Long = 0,
    val title : String,
    val memo : String,
    val time : Long = System.currentTimeMillis(),
    val memoFolderId : Long,
    val timeTableCellId : Long? = null
) {
    fun toModel(): MemoModel = MemoModel(
        id = memoId,
        title = title,
        memo = memo,
        time= time,
        memoFolderId =memoFolderId,
        timeTableCellId = timeTableCellId,
    )
}