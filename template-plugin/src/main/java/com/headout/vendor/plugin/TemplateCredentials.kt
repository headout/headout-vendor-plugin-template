package com.headout.vendor.plugin

/*
* Credentials necessary for the plugin,
* different environments have different credentials and are populated accordingly
* to be used when authenticating in the vendor APIs
* */
interface TemplateCredentials {
    val apiKey: String
    val apiSecret: String
    val baseUrl: String
    val debug: Boolean
}
