package ru.netology.servernmedia.model

import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Comment
import ru.netology.servernmedia.dto.Post
import ru.netology.servernmedia.dto.PostsWithComments

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false,
    val refreshing: Boolean = false,
)