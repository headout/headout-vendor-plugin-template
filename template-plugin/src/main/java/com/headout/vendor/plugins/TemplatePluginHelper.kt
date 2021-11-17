package com.headout.vendor.plugins

import com.headout.vendor.plugins.utils.AbstractPluginHelper
import com.headout.vendor.plugins.ho.api.hoApi
import com.headout.vendor.plugins.ho.api.models.LoginRequest
import com.headout.vendor.plugins.utils.AuthorizationInterceptor
import com.headout.vendor.plugins.utils.ITemplateCredentials
import com.headout.vendor.plugins.utils.LoginTokenAuthenticator
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class TemplatePluginHelper(val credentials: ITemplateCredentials) : AbstractPluginHelper() {

    private val authInterceptor: AuthorizationInterceptor = AuthorizationInterceptor(credentials, ::getNewLoginToken)
    private val hoLoginAPI: hoApi

    private fun getNewLoginToken() = runBlocking {
        val res = hoLoginAPI.login(LoginRequest(
                username = credentials.username,
                password = credentials.password
        ))
        res.token!!
    }

    init {
        val client = getOkHttpClientBuilder(credentials.debug)
                .addInterceptor(authInterceptor)
                .build()
        val retrofit = getRetrofit(credentials.endpoint, client)
        hoLoginAPI = retrofit.create(hoApi::class.java)
    }

    internal fun getTemplateApi(): hoApi {
        val client = getOkHttpClientBuilder(credentials.debug)
                .addInterceptor(authInterceptor)
                .authenticator(LoginTokenAuthenticator {
                    val token = getNewLoginToken()
                    authInterceptor.updateToken(token)
                    token
                })
                .build()
        val retrofit = getRetrofit(credentials.endpoint, client)
        return retrofit.create(hoApi::class.java)
    }

    override fun getRetrofit(baseUrl: String, client: OkHttpClient?): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))

        return client?.let { retrofitBuilder.client(client).build() } ?: retrofitBuilder.build()
    }
}