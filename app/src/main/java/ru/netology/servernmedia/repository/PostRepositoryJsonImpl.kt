package ru.netology.nmedia.repository
import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL


//class PostRepositoryInMemoryImpl: PostRepository {
class PostRepositoryJsonImpl(context: Context,):PostRepository{
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val postKey = "posts"
    private val nextIdKey = "next_id"
    private var nextId:Long=1

   var posts=listOf(
       Post (
           id = 0,
           author = "Me",
           published = "",
           likedByMe = false,
           countShare = 0,
           content = "",
           countLikes = 0,
           videoURL = URL("https://www.youtube.com/watch?v=WhWc3b3KhnY")
       ),
       Post(
           id = 0,
           author = "Me",
           published = "",
           likedByMe = false,
           countShare = 0,
           content = "",
           countLikes = 0,
           videoURL = URL("https://www.youtube.com/watch?v=WhWc3b3KhnY")
       ),
       Post(
       id = 0,
       author = "Me",
       published = "",
       likedByMe = false,
       countShare = 0,
       content = "",
       countLikes = 0,
       videoURL = URL("https://www.youtube.com/watch?v=WhWc3b3KhnY")
       ),
   )



    private val data=MutableLiveData(posts)
    init {
        prefs.getString(postKey, null)?.let {
            posts = gson.fromJson(it, type)

        }
        nextId = prefs.getLong(nextIdKey, nextId)
        data.value = posts
    }




    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id:Long) {
            posts = posts.map {
                if (it.id != id) it else it.copy(likedByMe = !it.likedByMe, countLikes = if(it.likedByMe) it.countLikes-1 else it.countLikes+1)

            }
        }

    override fun shareById(id:Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(countShare = it.countShare + 1)
        }

    }


    override fun removeById(id: Long) {
        posts=posts.filter { it.id !=id }

    }

    override fun save(post: Post) {
        posts = if(post.id==0L){
            listOf(
                post.copy(
                    id=nextId++,
                    author = "Me",
                    published = "now",
                    likedByMe = false,
                    countLikes = 0,
                    countShare = 0
                )
            ) + posts
        } else {
            posts.map{ if(it.id!=post.id) it else it.copy(content=post.content)}
        }

        return
    }
    private fun sync() {
        with(prefs.edit()) {
            putString(postKey, gson.toJson(posts))
            putLong(nextIdKey, nextId)
            apply()
        }
    }
}