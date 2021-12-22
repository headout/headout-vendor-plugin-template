package com.headout.vendor.plugins.template

interface TemplateCredentials {
    val apiKey: String
    val apiSecret: String
    val baseUrl: String
    val sandboxEnvironment: Boolean
    val endpoint: String
    val debug: Boolean
}
