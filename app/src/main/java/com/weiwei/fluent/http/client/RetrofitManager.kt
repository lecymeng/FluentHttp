package com.weiwei.fluent.http.client

import androidx.viewbinding.BuildConfig
import com.weiwei.fluent.http.cookie.PersistentCookieJar
import com.weiwei.fluent.http.cookie.cache.SetCookieCache
import com.weiwei.fluent.http.cookie.persistence.SharedPrefsCookiePersistDelegate
import com.weiwei.fluent.http.global.mainContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author weiwei
 * @date 2022.05.30
 */
object RetrofitManager {

  private const val BASE_URL = ""

  // 请求超时时间
  private const val TIME_OUT_SECONDS = 10

  private val cookieJar: PersistentCookieJar by lazy {
    PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistDelegate(mainContext))
  }

  private val logInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
  }

  // OkHttpClient相关配置
  private val client: OkHttpClient
    get() = OkHttpClient.Builder()
      .addInterceptor(logInterceptor)
      // .addInterceptor(CacheInterceptor(30)) // 缓存拦截器 可传入缓存天数
      // 缓存配置,缓存最大 10 MB, 请求的数据缓存到 data/data/pkg/cache/net_cache 目录中
      // .cache(Cache(File(mainContext.cacheDir, "net_cache"), 10 * 1024 * 1024))
      .connectTimeout(TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
      // .cookieJar(cookieJar)
      .build()

  /**
   * Retrofit相关配置
   */
  fun <T> getService(serviceClass: Class<T>, baseUrl: String? = null): T {
    return Retrofit.Builder()
      .client(client)
      .baseUrl(baseUrl ?: BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(serviceClass)
  }
}