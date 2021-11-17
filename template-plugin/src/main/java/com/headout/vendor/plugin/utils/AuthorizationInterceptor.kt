package com.headout.vendor.plugin.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val credentials: ITemplateCredentials,
                               private val getNewToken: () -> String) : Interceptor {

    private var token: String? = null

    fun updateToken(token: String) {
        this.token = token
    }

    private fun isLoginAPI(request: Request) = request.url().encodedPath().contains("login")

    override fun intercept(chain: Interceptor.Chain): Response {
        // Add your interceptor logic here
        return chain.proceed(chain.request())
    }

}