package com.headout.vendor.plugins.template

import org.apache.commons.codec.binary.Hex
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

const val ALGORITHM = "HmacSHA256"

// based on https://globalreseller.nl/documentation/connection/hmac-calculation
fun createSignature(data: String, key: String): String {
    val sha256Hmac = Mac.getInstance(ALGORITHM)
    val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), ALGORITHM)
    sha256Hmac.init(secretKey)
    // yes, they want us to encode to hex first and then
    val hex = Hex.encodeHexString(sha256Hmac.doFinal(data.toByteArray()))
    // base64 encode the result
    return Base64.getEncoder().encodeToString(hex.toByteArray())
}
