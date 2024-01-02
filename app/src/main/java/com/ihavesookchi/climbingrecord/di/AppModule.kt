package com.ihavesookchi.climbingrecord.di

import android.content.Context
import androidx.room.Room
import com.ihavesookchi.climbingrecord.data.Const
import com.ihavesookchi.climbingrecord.data.Const.KAKAO_URL
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.dao.ClimbingCenterDao
import com.ihavesookchi.climbingrecord.data.db.AppDatabase
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.repositoryImpl.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideClimbingCenterDao(database: AppDatabase): ClimbingCenterDao {
        return database.climbingCenterDao()
    }

    @Provides
    @Singleton
    fun kakaoRetrofit(): KakaoApi {
        return Retrofit.Builder()
            .baseUrl(KAKAO_URL)
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(Interceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", Const.KAKAO_API_KEY)
                            .build()
                        chain.proceed(newRequest)
                    })
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(kakaoRetrofit: KakaoApi, climbingCenterDao: ClimbingCenterDao): SearchRepository = SearchRepositoryImpl(kakaoRetrofit, climbingCenterDao)
}