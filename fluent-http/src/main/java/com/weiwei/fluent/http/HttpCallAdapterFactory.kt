package com.weiwei.fluent.http

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class HttpCallAdapterFactory : CallAdapter.Factory() {

    class HttpCallAdapter constructor(private val resultType: Type) : CallAdapter<Type, Call<HttpResult<Type>>> {

        override fun responseType() = resultType

        override fun adapt(call: Call<Type>): Call<HttpResult<Type>> = HttpResponseCall(call)
    }

    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                // 只有返回值是 HttpResult 的才会使用 [HttpCallAdapter]
                when (getRawType(callType)) {
                    HttpResult::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        HttpCallAdapter(resultType)
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

}