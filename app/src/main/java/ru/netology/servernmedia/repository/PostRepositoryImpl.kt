package ru.netology.servernmedia.repository

import androidx.room.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.servernmedia.db.AppDb.Companion.getInstance
import ru.netology.servernmedia.dto.Post
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


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

    override fun likeById(post: Post) {
        val request: Request =  if(post?.likedByMe==true) {
            Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$post.id/likes")
            .build()
        } else {
           Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/$post.id/likes")
            .build()
        }
        return client.newCall(request)
            .execute()
            .let{it  ?: throw RuntimeException("body is null")}
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

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()

    }
}