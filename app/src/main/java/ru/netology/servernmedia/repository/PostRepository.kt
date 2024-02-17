package ru.netology.servernmedia.repository

import androidx.lifecycle.LiveData
import okhttp3.Callback
import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Comment
import ru.netology.servernmedia.dto.Post

interface PostRepository {
    suspend fun getAllPostsAsinc(callbackPost: GetAllPostsCallback)
    suspend fun getPostById(id: Long, callback: GetPostByIdCallback)
    suspend fun savePostAsinc(post: Post, callback: SaveCallback)

    suspend fun likePostAsinc(id: Long, callback: GetPostByIdCallback)
    suspend fun unlikePostAsinc(id: Long, callback: GetPostByIdCallback)

    suspend fun removePostAsinc(id: Long, callback: RemoveCallback)
    suspend fun getAuthorByIdAsinc(id: Long, callback: GetAuthorByIdCallback)
    suspend fun saveAuthorAsinc(author: Author, callback: SaveCallback)
    suspend fun getAllCommentsAsinc(id: Long, callback: GetAllCommentsCallback)
    suspend fun saveCommentAsinc(comment: Comment, callback: SaveCallback)
    suspend fun removeCommentAsinc(id: Long, callback: RemoveCallback)
    suspend fun likeByCommentIdAsinc(id: Long, callback: GetCommentByIdCallback)
    suspend fun unlikeByCommentIdAsinc(id: Long, callback: GetCommentByIdCallback)
    interface GetAllPostsCallback{
        fun onSuccess(value:List<Post>){}
        fun onError(e:Exception){}
    }
    interface GetPostByIdCallback{
        fun onSuccess(value:Post){}
        fun onError(e:Exception){}
    }
    interface SaveCallback{
        fun onSuccess(value:Unit){}
        fun onError(e:Exception){}
    }
    interface RemoveCallback{
        fun onSuccess(value:Unit){}
        fun onError(e:Exception){}
    }
    interface GetAuthorByIdCallback{
        fun onSuccess(value:Author){}
        fun onError(e:Exception){}
    }
    interface GetAllCommentsCallback{
        fun onSuccess(value:List<Comment>){}
        fun onError(e:Exception){}
    }
    interface GetCommentByIdCallback{
        fun onSuccess(value:Comment){}
        fun onError(e:Exception){}
    }
}