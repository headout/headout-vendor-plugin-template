package com.headout.vendor.plugins.utils

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class LoginTokenAuthenticator(val getNewToken: () -> String): Authenticator {

    private fun isLoginAPI(request: Request) = request.header("token") == null
    private var token: String? = null

    fun updateToken(token: String) {
        this.token = token
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val isLoginAPI = isLoginAPI(response.request())
        if(!isLoginAPI) {
            val token = getNewToken()
            return response.request()
                    .newBuilder()
                    .header("token", token)
                    .build()

        } else {
            throw Exception("Panic: This is a critical failure. 401 on Login Request")
        }
    }

}