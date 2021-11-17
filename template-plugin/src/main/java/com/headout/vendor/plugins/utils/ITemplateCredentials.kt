package com.headout.vendor.plugins.utils

interface ITemplateCredentials {
    val endpoint: String
    val username: String?
    val password: String?
    val apiKey: String?
    val debug: Boolean
}