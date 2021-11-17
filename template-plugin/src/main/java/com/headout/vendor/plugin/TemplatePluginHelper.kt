package com.headout.vendor.plugin

import com.headout.vendor.plugin.api.TemplateApi
import com.headout.vendor.plugin.utils.AbstractPluginHelper
import com.headout.vendor.plugin.utils.AuthorizationInterceptor
import com.headout.vendor.plugin.utils.ITemplateCredentials
import com.headout.vendor.plugin.utils.LoginTokenAuthenticator
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class TemplatePluginHelper(val credentials: ITemplateCredentials) : AbstractPluginHelper() {

    private val authInterceptor: AuthorizationInterceptor = AuthorizationInterceptor(credentials, ::getNewLoginToken)
    private val templateLoginApi: TemplateApi

    private fun getNewLoginToken() = runBlocking {
        "Implement token logic if there is any"
    }

    init {
        val client = getOkHttpClientBuilder(credentials.debug)
                .addInterceptor(authInterceptor)
                .build()
        val retrofit = getRetrofit(credentials.endpoint, client)
        templateLoginApi = retrofit.create(TemplateApi::class.java)
    }

    internal fun getTemplateApi(): TemplateApi {
        val client = getOkHttpClientBuilder(credentials.debug)
                .addInterceptor(authInterceptor)
                .authenticator(LoginTokenAuthenticator {
                    val token = getNewLoginToken()
                    authInterceptor.updateToken(token)
                    token
                })
                .build()
        val retrofit = getRetrofit(credentials.endpoint, client)
        return retrofit.create(TemplateApi::class.java)
    }

    override fun getRetrofit(baseUrl: String, client: OkHttpClient?): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))

        return client?.let { retrofitBuilder.client(client).build() } ?: retrofitBuilder.build()
    }
}