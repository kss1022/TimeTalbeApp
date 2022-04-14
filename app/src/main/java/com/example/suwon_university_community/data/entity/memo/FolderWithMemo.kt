package com.example.suwon_university_community.data.entity.memo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


@Entity
data class FolderWithMemo(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "memoFolderId"
    )
    val memos: List<MemoEntity>
)