package ru.netology.servernmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.servernmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")//перебрать таблицу по id по убыванию(Dest)
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Insert
    fun insert(posts: List<PostEntity>)//метод добавления таблицы целиком

    @Query("UPDATE PostEntity SET content = :text WHERE id = :id")
    fun updateContentById(id: Long, text: String)//изменить контент поста

    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else updateContentById(post.id, post.content)

    @Query("""
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun likeById(id: Long)




    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)
}