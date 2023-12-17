package ru.netology.servernmedia.repository

import androidx.lifecycle.LiveData
import okhttp3.Callback
import ru.netology.servernmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun getAllAsinc(callback: repositoryCallback<List <Post>>)
    fun likeById(post: Post):Post
    fun likeByIdAsinc(post:Post, callback: repositoryCallback<Post>)
    fun save(post: Post)
    fun saveAsinc(post: Post,callback: repositoryCallback<Post>)
    fun removeById(id: Long)
    fun removeByIdAsinc(id: Long, callback: repositoryCallback<Post>)
    interface repositoryCallback<T>{
        fun onSuccess(result: T)
        fun onError(e:Exception)
    }


}