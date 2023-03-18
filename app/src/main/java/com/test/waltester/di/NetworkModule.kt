package com.test.waltester.di

import com.test.waltester.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient? {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory? =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient?,
        gsonConverterFactory: GsonConverterFactory?
    ): Retrofit? {
        return if (okHttpClient != null && gsonConverterFactory != null) {
            Retrofit.Builder()
                .baseUrl("https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/")
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build()
        } else null
    }

    @Singleton
    @Provides
    fun provideWTNetworker(retrofit: Retrofit?): WTNetworker? =
        retrofit?.create(WTNetworker::class.java)
}
