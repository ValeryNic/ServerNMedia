package ru.netology.servernmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.servernmedia.dto.Author
import ru.netology.servernmedia.dto.Comment
import ru.netology.servernmedia.dto.Post
import ru.netology.servernmedia.dto.PostsWithComments
import ru.netology.servernmedia.model.FeedModel
import ru.netology.servernmedia.repository.*
import ru.netology.servernmedia.repository.PostRepository.GetAllCommentsCallback
import ru.netology.servernmedia.util.SingleLiveEvent

private val empty = PostsWithComments(
    id = 0,
    authorName = "",
    authorAvatar = "",
    content = "",
    published = 0,
    likedByMe = false,
    likes = 0,
    attachment = null,
    comments = null
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
    var comments: List<Comment>? = null
    init {
        loadPosts()
    }

    fun loadPosts() {
                val postsWithComments:List<PostsWithComments> = getPosts()
                    .map {
                        PostsWithComments(it.id, authorName = getAuthor(it.id,object: PostRepository.GetAuthorByIdCallback).name
                        , authorAvatar = getAuthor(it.id,object: PostRepository.GetAuthorByIdCallback).avatar, it.content, it.published,
                            it.likedByMe, it.likes, it.attachment,comments = getComments(it.id, object: PostRepository.GetAllCommentsCallback))
                }
    }
    fun getPosts(){
//        val old = _data.value?.posts.orEmpty()
        _data.value=FeedModel(loading = true)
        suspend { repository.getAllPostsAsinc(object: PostRepository.GetAllPostsCallback
        {
            override fun onSuccess(result: List<Post>) {
                //_data.value=... -можно, т.к.retrofit через looper передаёт данные на главный поток
                _data.value=FeedModel(posts = result, empty = result.isEmpty())
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
//                _data.postValue(_data.value?.copy(posts = old))
            }
        })
        repository}

    }
    fun getAuthor(id: Long, param: PostRepository.GetAuthorByIdCallback): Author {
        _data.value=FeedModel(loading = true)
        suspend { repository.getAuthorByIdAsinc(id, object: PostRepository.GetAuthorByIdCallback
        {
            override fun onSuccess(result: Author) {
                //_data.value=... -можно, т.к.retrofit через looper передаёт данные на главный поток
                //_data.value=FeedModel(author = result, empty = result.isEmpty())
                val author = result
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
//                _data.postValue(_data.value?.copy(posts = old))
            }
        })
            repository}

    }
    fun getComments(id:Long, param: PostRepository.GetAllCommentsCallback): List<Comment>? {
        _data.value=FeedModel(loading = true)
        suspend { repository.getAllCommentsAsinc(id, object: PostRepository.GetAllCommentsCallback {
            override fun onSuccess(result: List<Comment>) {
                //_data.value=... -можно, т.к.retrofit через looper передаёт данные на главный поток
                //_data.value=FeedModel(author = result, empty = result.isEmpty())
                val comments = result
            }
            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
//                _data.postValue(_data.value?.copy(posts = old))
            }
        })
            repository}

    }
    fun save(post: Post) {
        edited.value?.let {
           suspend { repository.savePostAsinc(post, object :PostRepository.SaveCallback{
               override fun onSuccess(result: Unit) {
                   _postCreated.value = Unit
               }
               override fun onError(e: Exception) {
                   _data.postValue(FeedModel(error = true))
               }
           })
        }
        edited.value = empty}
    }

//    fun edit(post: Post) {
//        edited.value = post
//    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
    fun likeById(post: Post) {
        suspend {
            repository.likePostAsinc(post.id, object : PostRepository.GetPostByIdCallback{
                override fun onSuccess(result: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
    }

    fun removeById(id: Long) {
        suspend {
            repository.removePostAsinc(id, object : PostRepository.RemoveCallback {
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
}