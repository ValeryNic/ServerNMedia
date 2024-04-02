package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

interface PostRepository {
    val dataRep: Flow<List<PostEntity>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
}
