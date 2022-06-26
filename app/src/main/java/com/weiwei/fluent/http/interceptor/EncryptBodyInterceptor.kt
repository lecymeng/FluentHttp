package com.weiwei.fluent.http.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

abstract class EncryptBodyInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originRequest = chain.request()

    if (enableEncrypt()) {
      val handleRequest = encryptRequest(originRequest)
      return decryptResponse(chain.proceed(handleRequest))
    }
    return chain.proceed(originRequest)
  }

  private fun encryptRequest(request: Request): Request {
    val requestBody = request.body ?: return request

    val buffer = Buffer()
    requestBody.writeTo(buffer)
    val bodyString = buffer.readUtf8()
    if (TextUtils.isEmpty(bodyString)) {
      return request
    }

    val encryptBodyString = encryptBody(bodyString) ?: ""
    val encryptRequestBody = encryptBodyString.toRequestBody(requestBody.contentType())

    val requestBuilder = request.newBuilder()
    requestBuilder.method(request.method, encryptRequestBody)

    return requestBuilder.build()
  }

  private fun decryptResponse(response: Response): Response {
    if (!response.isSuccessful) {
      return response
    }

    val responseBody = response.body ?: return response

    val bodyString = responseBody.string()
    if (TextUtils.isEmpty(bodyString)) {
      return response
    }

    val decryptBodyString = decryptBody(bodyString) ?: ""
    val decryptResponseBody = decryptBodyString.toResponseBody(responseBody.contentType())

    val responseBuilder = response.newBuilder()
    responseBuilder.body(decryptResponseBody)

    return responseBuilder.build()
  }

  abstract fun enableEncrypt(): Boolean

  abstract fun encryptBody(bodyString: String): String?

  abstract fun decryptBody(bodyString: String): String?
}