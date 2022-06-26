package com.weiwei.fluent.http.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject

abstract class CommonParamInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originRequest = chain.request()

    val headerParams = getHeaderParams(originRequest)
    val requestBodyParams = getRequestBodyParams(originRequest)
    if (headerParams == null && requestBodyParams == null) {
      return chain.proceed(originRequest)
    }

    val requestBuilder = originRequest.newBuilder()

    headerParams?.forEach { entry ->
      requestBuilder.addHeader(entry.key, entry.value)
    }

    if (requestBodyParams != null) {
      val requestBody = originRequest.body

      val buffer = Buffer()
      originRequest.body?.writeTo(buffer)
      val bodyString = buffer.readUtf8()

      // FIXME: use <String, Any> ?, check content type
      val bodyJson: JSONObject = string2Json(bodyString)
      requestBodyParams.forEach { entry ->
        bodyJson.put(entry.key, entry.value)
      }

      val bodyJsonString = bodyJson.toString()
      val contentType = requestBody?.contentType()
      // val contentType = "application/json; charset=UTF-8".toMediaTypeOrNull()
      requestBuilder.method(originRequest.method, bodyJsonString.toRequestBody(contentType))
    }

    return chain.proceed(requestBuilder.build())
  }

  private fun string2Json(bodyString: String): JSONObject {
    if (TextUtils.isEmpty(bodyString)) {
      return JSONObject()
    }
    try {
      return JSONObject(bodyString)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return JSONObject()
  }

  abstract fun getHeaderParams(originRequest: Request): Map<String, String>?

  abstract fun getRequestBodyParams(originRequest: Request): Map<String, Any>?
}