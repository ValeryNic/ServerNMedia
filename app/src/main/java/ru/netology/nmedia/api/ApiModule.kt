package ru.netology.nmedia.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)//- на уровне всего приложения
@Module
class ApiModule {
    companion object {
        private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"
        //"http://10.0.2.2:9999/api/slow/"
    }
    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {

        level = HttpLoggingInterceptor.Level.BODY

    }
    @Singleton
    @Provides
    fun provideOkHttp(
        logging:HttpLoggingInterceptor,
        appAuth: AppAuth,
    ):OkHttpClient = OkHttpClient.Builder()
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

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ):Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): PostsApiService = retrofit.create()
}