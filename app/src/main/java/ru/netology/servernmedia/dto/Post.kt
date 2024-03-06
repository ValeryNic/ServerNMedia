package ru.netology.servernmedia.dto
import ru.netology.servernmedia.enumeration.AttachmentType
data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
)


