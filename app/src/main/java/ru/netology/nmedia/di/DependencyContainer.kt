package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.api.PostsApiService
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

//Данная фабрика дб внедрена во все activity, fragments, viewModels
class DependencyContainer(
    private val context:Context
) {
    companion object{
        //Singleton- еденичный экземпляр
        private const val BASE_URL =  "${BuildConfig.BASE_URL}/api/slow/"
        //"http://10.0.2.2:9999/api/slow/"

        @Volatile
        private  var instance: DependencyContainer? = null

        fun initApp(context: Context){
            instance = DependencyContainer(context)
        }
        fun getInstance(): DependencyContainer {
            return instance!!
            }
        }
        private  fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java,"app.db")




    private val logging = HttpLoggingInterceptor().apply {

        level = HttpLoggingInterceptor.Level.BODY

    }
    val appAuth = AppAuth(context)

    private val okhttp = OkHttpClient.Builder()
        .addInterceptor{chain ->//обращаемся к текущ экз. AppAuth - token есть?
            appAuth.authStateFlow.value.token?.let { token ->
    //Если есть - создаем запрос с авторизацией
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            //если нет - создаем обычный запрос
            chain.proceed(chain.request())
        }
        //addInterceptor(logging) -наблюдатель логинга
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okhttp)
        .build()

    val apiService = retrofit.create<PostsApiService>()

    private  val appDb = Room.databaseBuilder(context, AppDb::class.java, "app.db")
    .fallbackToDestructiveMigration()
    .build()

    private val postDao = appDb.postDao()

    val repository:PostRepository = PostRepositoryImpl(
        postDao,
        apiService,)
}