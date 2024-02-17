package ru.netology.servernmedia.dto
import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Comment
import ru.netology.servernmedia.dto.Post
data class PostsWithComments(
    val id: Long,
    val authorName: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    var attachment: Attachment? = null,
    var comments:List<Comment>? = listOfNotNull(Comment(0,0,0,"",0,false,0)),
)
