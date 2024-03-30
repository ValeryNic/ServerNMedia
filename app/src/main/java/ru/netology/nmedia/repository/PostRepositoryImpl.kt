package ru.netology.nmedia.repository

import android.provider.MediaStore.Images.Media
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okio.IOException
import ru.netology.nmedia.api.*
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.AttachmentType
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.toDto
import ru.netology.nmedia.entity.toEntity
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.AppError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.model.PhotoModel
import java.io.File

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll().map(List<PostEntity>::toDto)
        //.flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = PostsApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
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
            val response = PostsApi.service.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())//записываем в БД список с новыми постами
            println(body.size)
            emit(body.size)//функция возвращает кол-во новых постов

        }
    }.catch { e -> throw AppError.from(e) }//выделяем здесь, а перехватываем во PostViewModel.newerCount
        .flowOn(Dispatchers.Default)

    override suspend fun save(post: Post) {
        try {
            val response = PostsApi.service.save(post)
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
            val response = PostsApi.service.removeById(id)
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
            var response = PostsApi.service.getById(id)//PostsApi.service.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            var post = response.body() ?: throw ApiError(response.code(), response.message())
            if(post.likedByMe){
                response = PostsApi.service.dislikeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                post = response.body() ?: throw ApiError(response.code(), response.message())
            } else {
                response = PostsApi.service.likeById(id)
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

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            // TODO: add support for other types
            val postWithAttachment = post.copy(attachment = Attachment(media.id, ru.netology.nmedia.enumeration.AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): ru.netology.nmedia.dto.Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody()
            )

            val response = PostsApi.service.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: java.io.IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}
