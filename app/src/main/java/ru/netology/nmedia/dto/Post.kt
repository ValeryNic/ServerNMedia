package ru.netology.nmedia.dto
<<<<<<< HEAD
import ru.netology.nmedia.enumeration.AttachmentType
=======

import ru.netology.nmedia.enumeration.AttachmentType

>>>>>>> new
data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
<<<<<<< HEAD
    val newPostsAdded:Boolean,
    val attachment: Attachment? = null,
)
data class Attachment(
    val url: String,
=======
    var newPostsAdded:Boolean,
    var attachment: Attachment? = null,
)

data class Attachment(
    val url: String,
    val description: String?,
>>>>>>> new
    val type: AttachmentType,
)

