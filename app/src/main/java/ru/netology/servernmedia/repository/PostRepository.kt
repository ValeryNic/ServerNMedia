package ru.netology.servernmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.servernmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(post: Post):Post
    fun save(post: Post)
    fun removeById(id: Long)
}