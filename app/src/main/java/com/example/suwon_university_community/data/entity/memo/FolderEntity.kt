package com.example.suwon_university_community.data.entity.memo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val folderId: Long = 0,
    val name : String,
    val count : Int = 0,
    val category : FolderCategory,
    val isDefault : Boolean = false,
    val timeTableId : Long? = null
)