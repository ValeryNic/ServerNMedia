package ru.netology.nmedia.repository

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: PostsApiService
) : PostRepository {
    override val dataRep = dao.getAllDao()
        //.flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = apiService.getAllApi()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            println(body)
            dao.insert(body.toEntity())
            delay(10_000L)
        } catch (e: IOException) {
            throw NetworkError

        } catch (e: Exception) {
            throw UnknownError
        }
    }



    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            println("in1")
            kotlinx.coroutines.delay(10_000L)
            println("in2")
            val response = apiService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())//записываем в БД список с новыми постами
            println(body.size)
            emit(body.size)//функция возвращает кол-во новых постов

        }
    }.catch {it.printStackTrace()}//{ e -> throw AppError.from(e) }
        //flowOn - наблюдатель за всеми операторами, находящимися выше
        .flowOn(Dispatchers.Default)

    override suspend fun save(post: Post) {
        try {
            val response = apiService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try{
            val response = apiService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        try{
            var response = apiService.getById(id)//PostsApi.service.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            var post = response.body() ?: throw ApiError(response.code(), response.message())
            if(post.likedByMe){
                response = apiService.dislikeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                post = response.body() ?: throw ApiError(response.code(), response.message())
            } else {
                response = apiService.likeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                post = response.body() ?: throw ApiError(response.code(), response.message())
            }
            dao.insert(PostEntity.fromDto(post))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}
