package com.headout.vendor.plugins.template

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.headout.vendor.plugin.utils.AbstractPluginHelper
import com.headout.vendor.plugins.template.api.TemplateApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


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