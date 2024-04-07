package ru.netology.nmedia.model

import ru.netology.nmedia.entity.PostEntity

data class FeedModel(
    val posts: List<PostEntity> = emptyList(),
    val empty: Boolean = false,
)
