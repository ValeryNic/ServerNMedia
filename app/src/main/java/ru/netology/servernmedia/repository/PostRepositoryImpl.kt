package ru.netology.servernmedia.repository

import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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


class PostRepositoryImpl: PostRepository {

    override fun getAllAsinc(callback: PostRepository.GetAllCallback) {
        PostsApi.retrofitService.getAll().enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(
                call: retrofit2.Call<List<Post>>,
                response: retrofit2.Response<List<Post>>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
               callback.onError(Exception("${t} -  No address associated with hostname"))
            }
        })
    }

    override fun saveAsinc(post: Post, callback: PostRepository.SaveCallback) {
        PostsApi.retrofitService.save(post).enqueue(object :retrofit2.Callback<Post> {
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
    override fun removeAsinc(id: Long, callback: PostRepository.RemoveCallback) {
        PostsApi.retrofitService.removeById(id).enqueue(object :retrofit2.Callback<Unit>{
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


    override fun likeAsinc(id: Long, callback: PostRepository.LikeCallback) {
        PostsApi.retrofitService.likeById(id).enqueue(object :retrofit2.Callback<Post>{
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException("error cod: ${response.code()} with ${response.message()}"))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                callback.onError(Exception("${t} - No address associated with hostname"))
            }
        })

    }

}