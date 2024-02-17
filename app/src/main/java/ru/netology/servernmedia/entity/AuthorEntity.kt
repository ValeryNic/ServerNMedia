package ru.netology.servernmedia.entity

import androidx.room.PrimaryKey
import androidx.room.Entity
@Entity
data class AuthorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val avatar: String,
)
