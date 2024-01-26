package ru.netology.servernmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.servernmedia.dto.Post
import ru.netology.servernmedia.model.FeedModel
import ru.netology.servernmedia.repository.*
import ru.netology.servernmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
//        val old = _data.value?.posts.orEmpty()
        _data.value=FeedModel(loading = true)
        repository.getAllAsinc(object: PostRepository.GetAllCallback {
            override fun onSuccess(result: List<Post>) {
                //_data.value=... -можно, т.к.retrofit через looper передаёт данные на главный поток
                _data.value=FeedModel(posts = result, empty = result.isEmpty())
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
//                _data.postValue(_data.value?.copy(posts = old))
            }
        })

    }

    fun save() {
//        val old = _data.value?.posts.orEmpty()
        edited.value?.let {
           repository.saveAsinc(it, object :PostRepository.SaveCallback{
               override fun onSuccess(result: Unit) {
                   _postCreated.value = Unit
//                   _postCreated.postValue(Unit)
//                   _data.postValue(
//                       _data.value?.copy(posts = _data.value?.posts.orEmpty()))
               }
               override fun onError(e: Exception) {
                   _data.postValue(FeedModel(error = true))
//                   _data.postValue(_data.value?.copy(posts = old))
               }
           })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
    //вариант
    //fun likeById(id: Long) {
    //        val post = _data.value?.posts?.find { id == it.id } ?: return
    //        thread {
    fun likeById(post: Post) {
        repository.likeAsinc(post.id, object :PostRepository.LikeCallback {
            override fun onSuccess(result: Post) {
                _postCreated.postValue(Unit)
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }
//        thread {
//            val posts = _data.value?.posts?.map { // Формируем новый список постов
//                if (it.id != post.id) it else repository.likeById(post)
//            } ?: emptyList()
//            _data.postValue(_data.value?.copy(posts = posts)) // Обновляем список постов в ленте
//        }
  // }

    fun removeById(id: Long){
    repository.removeAsinc(id, object : PostRepository.RemoveCallback {
//        val old = _data.value?.posts.orEmpty()
        override fun onSuccess(result: Unit) {
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
        }

        override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
 //               _data.postValue(_data.value?.copy(posts = old))
            }
        })
    }
}