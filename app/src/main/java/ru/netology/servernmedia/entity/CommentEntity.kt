package ru.netology.servernmedia.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
)
