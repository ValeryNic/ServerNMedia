package ru.netology.servernmedia.api

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.servernmedia.BuildConfig
import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Comment
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
    suspend fun getAllPosts(): Call<List<Post>>
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Call<Post>
    @POST("posts")
    suspend fun savePost(@Body post: Post): Call<Post>//@Body - говорит о том, что параметр post надо
    //отправить в теле запроса на сервер
    @DELETE("posts/{id}")
    suspend fun removePostById(@Path("id") id: Long): Call<Unit>//вид ответа должен быть, его надо
    //сформировать -  Call<Unit>, чтобы retrofit закрыл обработку запроса
    @POST("posts/{id}/likes")
    fun likePostById(@Path("id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikePostById(@Path("id") id: Long): Call<Post>
    @GET("authors/{id}")
    suspend fun getAuthorById(@Path("id") id: Long): Call<Author>
    @POST("authors")
    suspend fun saveAuthor(@Body author: Author): Call<Author>
    @GET("posts/{postId}/comments")
    suspend fun getAllCommentsByPostId(@Path("id") id: Long):Call<List<Comment>>
    @POST("posts/{postId}/comments")
suspend fun saveComment(@Body comment: Comment): Call<Comment>
    @DELETE("posts/{postId}/comments/{id}")
    suspend fun deleteCommentById(@Path("id") id: Long):Call<Unit>
    @POST("posts/{postId}/comments/{id}/likes")
    suspend fun likeCommentById(@Path("id") id: Long): Call<Comment>
    @DELETE("posts/{postId}/comments/{id}/likes")
    suspend fun unlikeCommentById(@Path("id") id: Long): Call<Comment>





}

object PostsApi {
    val retrofitService: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }
}