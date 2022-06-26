package com.weiwei.fluent.http.client

import androidx.viewbinding.BuildConfig
import com.weiwei.fluent.http.HttpCallAdapterFactory
import com.weiwei.fluent.http.api.WanApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WanClient {
  private val baseUrl = "https://www.wanandroid.com"

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor.Level.BODY
    } else {
      HttpLoggingInterceptor.Level.BASIC
    }
  }

  private val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(10L, TimeUnit.SECONDS)
    .build()

  val jsonMediaType: MediaType? get() = "application/json; charset=UTF-8".toMediaTypeOrNull()

  fun getService(): WanApi {
    return Retrofit.Builder()
      .client(client)
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(HttpCallAdapterFactory())
      .build()
      .create(WanApi::class.java)
  }
}