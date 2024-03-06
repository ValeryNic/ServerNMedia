package ru.netology.servernmedia.model

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)