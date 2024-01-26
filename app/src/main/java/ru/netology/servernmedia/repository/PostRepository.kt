package ru.netology.servernmedia.repository

import androidx.lifecycle.LiveData
import okhttp3.Callback
import ru.netology.servernmedia.dto.Post

interface PostRepository {
    fun getAllAsinc(callback: GetAllCallback)
    fun saveAsinc(post: Post, callback: SaveCallback)

    fun likeAsinc(id: Long, callback: LikeCallback)

    fun removeAsinc(id: Long, callback: RemoveCallback)
    interface GetAllCallback{
        fun onSuccess(value:List<Post>){}
        fun onError(e:Exception){}
    }
    interface SaveCallback{
        fun onSuccess(value:Unit){}
        fun onError(e:Exception){}
    }
    interface LikeCallback{
        fun onSuccess(value:Post){}
        fun onError(e:Exception){}
    }
    interface RemoveCallback{
        fun onSuccess(value:Unit){}
        fun onError(e:Exception){}
    }
}