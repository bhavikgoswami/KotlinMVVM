package com.witnovus.book.di

import com.witnovus.book.network.BookAPIService
import com.witnovus.book.utils.Constants
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory

/**
 * Provider of singleton objects of Retrofit class for API operation
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Provider of Retrofit API Class
     * @return the Retrofit class
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    /**
     * Provider of Book Api Service class
     * @return bookApiService
     */
    @Provides
    @Singleton
    fun provideBookAPIService(): BookAPIService {
        return provideRetrofit().create(BookAPIService::class.java)
    }
}