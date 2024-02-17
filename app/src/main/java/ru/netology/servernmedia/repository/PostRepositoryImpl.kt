package ru.netology.servernmedia.repository

import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.servernmedia.api.PostsApi
import ru.netology.servernmedia.dto.Post
import ru.netology.servernmedia.repository.PostRepository
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import ru.netology.servernmedia.api.PostsApiService
import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Comment
import ru.netology.servernmedia.dto.PostsWithComments
import kotlin.coroutines.EmptyCoroutineContext


class PostRepositoryImpl: PostRepository {




    override suspend fun getAllPostsAsinc(callback: PostRepository.GetAllPostsCallback
    ) {
        with(CoroutineScope(EmptyCoroutineContext)){

            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.getAllPosts()
                        .enqueue(object : retrofit2.Callback<List<Post>> {
                            override fun onResponse(
                                call: retrofit2.Call<List<Post>>,
                                response: retrofit2.Response<List<Post>>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                                callback.onError(Exception("${t} -  No address associated with hostname"))
                            }
                        })


                        }

            }
        }
    }

    override suspend fun getPostById(id: Long, callback: PostRepository.GetPostByIdCallback) {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.getPostById(id)
                        .enqueue(object : retrofit2.Callback<Post> {
                            override fun onResponse(
                                call: retrofit2.Call<Post>,
                                response: retrofit2.Response<Post>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                                callback.onError(Exception("${t} -  No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }
    override suspend fun savePostAsinc(post: Post, callback: PostRepository.SaveCallback) {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.savePost(post)
                        .enqueue(object : retrofit2.Callback<Post> {
                            override fun onResponse(
                                call: retrofit2.Call<Post>,
                                response: retrofit2.Response<Post>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }
                                callback.onSuccess(Unit)
                            }

                            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }
    override suspend fun  removePostAsinc(id: Long, callback: PostRepository.RemoveCallback) {
        with(CoroutineScope(EmptyCoroutineContext)) {
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.removePostById(id)
                        .enqueue(object : retrofit2.Callback<Unit> {
                            override fun onResponse(
                                call: retrofit2.Call<Unit>,
                                response: retrofit2.Response<Unit>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(Unit)
                            }

                            override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }
    }


    override suspend fun  likePostAsinc(id: Long, callback: PostRepository.GetPostByIdCallback) {
        with(CoroutineScope(EmptyCoroutineContext)) {
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.likePostById(id)
                        .enqueue(object : retrofit2.Callback<Post> {
                            override fun onResponse(
                                call: retrofit2.Call<Post>,
                                response: retrofit2.Response<Post>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }
     override suspend fun unlikePostAsinc(id: Long, callback: PostRepository.GetPostByIdCallback) {
        with(CoroutineScope(EmptyCoroutineContext)) {
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.unlikePostById(id)
                        .enqueue(object : retrofit2.Callback<Post> {
                            override fun onResponse(
                                call: retrofit2.Call<Post>,
                                response: retrofit2.Response<Post>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }
    override suspend fun getAuthorByIdAsinc(id: Long, callback: PostRepository.GetAuthorByIdCallback) {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.getAuthorById(id)
                        .enqueue(object : retrofit2.Callback<Author> {
                            override fun onResponse(
                                call: retrofit2.Call<Author>,
                                response: retrofit2.Response<Author>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<Author>, t: Throwable) {
                                callback.onError(Exception("${t} -  No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }

    override suspend fun saveAuthorAsinc(author: Author, callback: PostRepository.SaveCallback) {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.saveAuthor(author)
                        .enqueue(object : retrofit2.Callback<Author> {
                            override fun onResponse(
                                call: retrofit2.Call<Author>,
                                response: retrofit2.Response<Author>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }
                                callback.onSuccess(Unit)
                            }

                            override fun onFailure(call: retrofit2.Call<Author>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }
    override suspend fun getAllCommentsAsinc(id: Long, callback: PostRepository.GetAllCommentsCallback
    ) {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.getAllCommentsByPostId(id)
                        .enqueue(object : retrofit2.Callback<List<Comment>> {
                            override fun onResponse(
                                call: retrofit2.Call<List<Comment>>,
                                response: retrofit2.Response<List<Comment>>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<List<Comment>>, t: Throwable) {
                                callback.onError(Exception("${t} -  No address associated with hostname"))
                            }
                        })

                }
            }
        }
    }
    override suspend fun saveCommentAsinc(comment: Comment, callback: PostRepository.SaveCallback) {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.saveComment(comment)
                        .enqueue(object : retrofit2.Callback<Comment> {
                            override fun onResponse(
                                call: retrofit2.Call<Comment>,
                                response: retrofit2.Response<Comment>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }
                                callback.onSuccess(Unit)
                            }

                            override fun onFailure(call: retrofit2.Call<Comment>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }

    }
    override suspend fun  removeCommentAsinc(id: Long, callback: PostRepository.RemoveCallback) {
        with(CoroutineScope(EmptyCoroutineContext)) {
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.deleteCommentById(id)
                        .enqueue(object : retrofit2.Callback<Unit> {
                            override fun onResponse(
                                call: retrofit2.Call<Unit>,
                                response: retrofit2.Response<Unit>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(Unit)
                            }

                            override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                                callback.onError(Exception("${t} - No address associated with hostname"))
                            }
                        })
                }
            }
        }
    }

    override suspend fun likeByCommentIdAsinc(
        id: Long,
        callback: PostRepository.GetCommentByIdCallback)
    {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.likeCommentById(id)
                        .enqueue(object : retrofit2.Callback<Comment> {
                            override fun onResponse(
                                call: retrofit2.Call<Comment>,
                                response: retrofit2.Response<Comment>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<Comment>, t: Throwable) {
                                callback.onError(Exception("${t} -  No address associated with hostname"))
                            }
                        })

                }
            }
        }
    }
    override suspend fun unlikeByCommentIdAsinc(
        id: Long,
        callback: PostRepository.GetCommentByIdCallback)
    {
        with(CoroutineScope(EmptyCoroutineContext)){
            launch {
                withContext(Dispatchers.IO) {
                    PostsApi.retrofitService.unlikeCommentById(id)
                        .enqueue(object : retrofit2.Callback<Comment> {
                            override fun onResponse(
                                call: retrofit2.Call<Comment>,
                                response: retrofit2.Response<Comment>
                            ) {
                                if (!response.isSuccessful) {
                                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                                    return
                                }

                                callback.onSuccess(
                                    response.body() ?: throw RuntimeException("body is null")
                                )
                            }

                            override fun onFailure(call: retrofit2.Call<Comment>, t: Throwable) {
                                callback.onError(Exception("${t} -  No address associated with hostname"))
                            }
                        })

                }
            }
        }
    }
}