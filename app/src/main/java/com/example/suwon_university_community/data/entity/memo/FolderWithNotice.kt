package com.example.suwon_university_community.data.entity.memo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


@Entity
data class FolderWithNotice(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "noticeFolderId"
    )
    val notices: List<BookMarkNoticeEntity>
)