package com.weiwei.fluent.http.interceptor

import com.weiwei.fluent.http.global.mainContext
import com.weiwei.fluent.http.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author weiwei
 * @date 2022.05.30
 *
 * 缓存拦截器: 用于无网情况下传递 header 直接拉取之前缓存的数据
 */
class CacheInterceptor(private var day: Int = 7) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()
    if (!NetworkUtils.isNetworkAvailable(mainContext)) {
      request = request.newBuilder()
        .cacheControl(CacheControl.FORCE_CACHE)
        .build()
    }
    val response = chain.proceed(request)
    if (!NetworkUtils.isNetworkAvailable(mainContext)) {
      val maxAge = 60 * 60
      response.newBuilder()
        .removeHeader("Pragma")
        .header("Cache-Control", "public, max-age=$maxAge")
        .build()
    } else {
      val maxStale = 60 * 60 * 24 * day
      response.newBuilder()
        .removeHeader("Pragma")
        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
        .build()
    }
    return response
  }
}