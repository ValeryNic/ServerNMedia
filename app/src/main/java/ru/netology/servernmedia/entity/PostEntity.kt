package ru.netology.servernmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.servernmedia.dto.Attachment
import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    var attachment: Attachment? = null,
)