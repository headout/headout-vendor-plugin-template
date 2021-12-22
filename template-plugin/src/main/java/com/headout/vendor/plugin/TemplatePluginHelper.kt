package com.headout.vendor.plugin

import com.headout.vendor.plugin.utils.AbstractPluginHelper
import com.headout.vendor.plugins.template.api.TemplateApi
import okhttp3.OkHttpClient

/*
* Helper to intercept the requests and inject whatever data the API needs.
* This includes setting various headers, signing the API request, etc.
* */
class TemplatePluginHelper(
    private val credentials: TemplateCredentials

) : AbstractPluginHelper() {
    private val templateClient = getRetrofit(credentials.baseUrl, getOkHttpClient())
    val templateApi: TemplateApi = templateClient.create(TemplateApi::class.java)


    private fun getOkHttpClient(): OkHttpClient {
        return getOkHttpClientBuilder(credentials.debug)
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .addHeader("X-API-Key", credentials.apiKey)
                    .addHeader("X-API-Secret", credentials.apiSecret)
                    .build()
                chain.proceed(request)
            }
            .build()
    }
}