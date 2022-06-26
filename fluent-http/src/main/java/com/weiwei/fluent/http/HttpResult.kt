package com.weiwei.fluent.http

import android.os.Handler
import android.os.Looper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author weiwei
 * @date 2022.05.31
 */

sealed class HttpResult<out T> {
    /**
     * Http request success
     */
    data class Success<T>(val response: Response<T>) : HttpResult<T>() {
        val bodyData: T = response.body()!!
    }

    /**
     * Http request failed
     */
    sealed class Failure<T> : HttpResult<T>() {
        /**
         * Internal server error
         * Http协议报文中的状态码不在200-300区间内
         */
        data class ServerError<T>(val response: Response<T>) : Failure<T>() {
            val errorBody: String = response.errorBody()?.string() ?: ""
            val code: Int = response.code()
        }

        /**
         * An exception occurred in the http request
         * 网络请求过程中发生了异常，例如超时异常，网络断开异常，实体类解析异常等
         */
        data class Exception<T>(val t: Throwable) : Failure<T>() {
            val errorMsg: String get() = t.message ?: ""
        }
    }
}

inline fun <reified T> HttpResult<T>.doHttpResponse(block: (Response<T>) -> Unit) {
    if (this is HttpResult.Success) {
        block(this.response)
    } else if (this is HttpResult.Failure.ServerError) {
        block(this.response)
    }
}

inline fun <reified T> HttpResult<T>.doHttpFailure(block: (errorMsg: String) -> Unit) {
    if (this is HttpResult.Failure.ServerError) {
        block(this.errorBody)
    } else if (this is HttpResult.Failure.Exception) {
        block(this.t.message ?: "")
    }
}

inline fun <reified T> HttpResult<T>.doHttpSuccess(block: (HttpResult.Success<T>) -> Unit) {
    if (this is HttpResult.Success) {
        block(this)
    }
}

inline fun <reified T> HttpResult<T>.doHttpServerError(block: (HttpResult.Failure.ServerError<T>) -> Unit) {
    if (this is HttpResult.Failure.ServerError) {
        block(this)
    }
}

inline fun <reified T> HttpResult<T>.doHttpException(block: (t: Throwable) -> Unit) {
    if (this is HttpResult.Failure.Exception) {
        block(this.t)
    }
}

fun <T> Call<HttpResult<T>>.doEnqueue(
    httpCallback: ((httpResult: HttpResult<T>) -> Unit)
) {
    this.enqueue(object : Callback<HttpResult<T>> {
        override fun onResponse(call: Call<HttpResult<T>>, response: Response<HttpResult<T>>) {
            val httpResult = response.body() ?: return

            Handler(Looper.getMainLooper()).post {
                httpCallback(httpResult)
            }
        }

        override fun onFailure(call: Call<HttpResult<T>>, t: Throwable) {
        }
    })
}