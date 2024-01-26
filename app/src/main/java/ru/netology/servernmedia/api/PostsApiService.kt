package ru.netology.servernmedia.api

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.servernmedia.BuildConfig
import ru.netology.servernmedia.dto.Post

private val BASE_URL =  "${BuildConfig.BA_URL}/api/slow/"
//private const val BASE_URL = "http://10.0.2.2.9999/api/slow/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {//Если BuildConfig=DEBUG, то уровень устанавливаем в BODY
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface PostsApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>

    @GET("posts/{id}")
    fun getById(@Path("id") id: Long): Call<Post>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>//@Body - говорит о том, что параметр post надо
//отправить в теле запроса на сервер
    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long): Call<Unit>//вид ответа должен быть, его надо
//сформировать -  Call<Unit>, чтобы retrofit закрыл обработку запроса
    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun dislikeById(@Path("id") id: Long): Call<Post>
}

object PostsApi {
    val retrofitService: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}