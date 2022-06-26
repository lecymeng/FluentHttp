package com.weiwei.fluent.http

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 对原来的 Call 进行适配，对方法的返回值进行扩展
 *
 * /post/6844903778303344647
 * /post/7106832453578260494
 */
class HttpResponseCall<T>(private val proxyCall: Call<T>) : Call<HttpResult<T>> {

    override fun enqueue(callback: Callback<HttpResult<T>>) {
        proxyCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                // 将被代理的 Call 的响应包装成 HttpResult 返回给代理者
                val httpResult: HttpResult<T> =
                    if (response.isSuccessful) {
                        // http response success, code=[200-300]
                        HttpResult.Success(response)
                    } else  {
                        // http response failure
                        HttpResult.Failure.ServerError(response)
                    }

                // 将 Retrofit 中的 Response 转成 HttpResult
                callback.onResponse(this@HttpResponseCall, Response.success(httpResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                // 将被代理的 Call 的异常包装成 HttpResult 返回给代理者
                val httpResult: HttpResult<T> = HttpResult.Failure.Exception(t)
                callback.onResponse(this@HttpResponseCall, Response.success(httpResult))
            }
        })
    }

    override fun clone(): Call<HttpResult<T>> = HttpResponseCall(proxyCall.clone())

    override fun execute(): Response<HttpResult<T>> = throw NotImplementedError()

    override fun isExecuted(): Boolean = proxyCall.isExecuted

    override fun isCanceled(): Boolean = proxyCall.isCanceled

    override fun cancel() = proxyCall.cancel()

    override fun request(): Request = proxyCall.request()

    override fun timeout(): Timeout = proxyCall.timeout()
}