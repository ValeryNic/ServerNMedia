package ru.netology.servernmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.servernmedia.dto.Post
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class PostRepositoryImpl: PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)//задержка для отработки сервера
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}//костыль для получения списка постов из gson

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()//тип данных для запроса от сервера
    }
    private fun <T> enqueueRepository(
        request: Request,
        typeRepository: Type,
        callback: PostRepository.repositoryCallback<T>) {
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val result = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(result, typeRepository))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun getAll(): List<Post> {//создать запрос
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)//запустить запрос
            .execute()//синхронный вызов - ждём, пока не получим ответ с сервера
            .let { it.body?.string() ?: throw RuntimeException("body is null") }//получаем строку либо выбрасываем исключение
            .let {
                gson.fromJson(it, typeToken.type)//сохраняем список постов в gson
            }
    }

    override fun getAllAsinc(callback: PostRepository.repositoryCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        enqueueRepository(request, typeToken.type, callback)

//        client.newCall(request)
//            .enqueue(object :Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    println("111 from ${Thread.currentThread().name}")
//                    try {
//                        val body = response.body?.string() ?: throw RuntimeException("body is null")
//                        callback.onSuccess(gson.fromJson(body, typeToken.type))
//                    } catch (e:Exception){//приём ошибки ОТ сервера
//                        callback.onError(e)
//                    }
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)//приём ошибки обращения К серверу с запросом
//                }
//            }
//
//            )
        println("222 from ${Thread.currentThread().name}")
    }
    override fun likeById(post: Post):Post {
        val request: Request =  if(post.likedByMe) {
            Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/${post.id}/likes")
            .build()
        } else {
           Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/${post.id}/likes")
            .build()
        }
        return client.newCall(request)
            .execute()
            .let{it.body?.string()  ?: throw RuntimeException("body is null")}
            .let{gson.fromJson(it, Post::class.java)}
    }

    override fun likeByIdAsinc(post: Post,callback: PostRepository.repositoryCallback<Post>){
        val request: Request =  if(post.likedByMe) {
            Request.Builder()
                .delete()
                .url("${BASE_URL}/api/slow/posts/${post.id}/likes")
                .build()
        } else {
            Request.Builder()
                .post(gson.toJson(post).toRequestBody(jsonType))
                .url("${BASE_URL}/api/slow/posts/${post.id}/likes")
                .build()
        }
        enqueueRepository(request, Post::class.java, callback)
    }
    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun saveAsinc(post: Post, callback: PostRepository.repositoryCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()
        enqueueRepository(request,Post::class.java, callback)
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeByIdAsinc(id: Long, callback: PostRepository.repositoryCallback<Post>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        enqueueRepository(request, Post::class.java, callback)
    }
}