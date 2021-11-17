package com.headout.vendor.plugin.utils

import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.pkcs.RSAPublicKey
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.crypto.InvalidCipherTextException
import org.bouncycastle.crypto.encodings.PKCS1Encoding
import org.bouncycastle.crypto.engines.RSAEngine
import org.bouncycastle.crypto.params.RSAKeyParameters
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMParser
import java.io.IOException
import java.io.StringReader
import java.math.BigInteger
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


@Throws(IOException::class,
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidKeyException::class,
        InvalidCipherTextException::class)
fun encryptRSA(data: String, publicKey: String): String {
    Security.addProvider(BouncyCastleProvider())
    val bytesToEncrypt = data.toByteArray()
    val cryptoEngine = PKCS1Encoding(RSAEngine())
    val readerObject = PEMParser(StringReader(publicKey)).readObject()
    val pubKeyInfo = readerObject as SubjectPublicKeyInfo

    val pubKey = RSAPublicKey.getInstance(pubKeyInfo.parsePublicKey() as ASN1Sequence)

    val rsaKeyParameters = RSAKeyParameters(false, pubKey.modulus, pubKey.publicExponent)

    cryptoEngine.init(true, rsaKeyParameters)
    val encrypted = cryptoEngine.processBlock(bytesToEncrypt, 0, bytesToEncrypt.size)

    return Base64.getEncoder().encodeToString(encrypted)
}

@Throws(NoSuchAlgorithmException::class)
fun getSHA256(input: String): String {

    // Static getInstance method is called with hashing SHA
    val md = MessageDigest.getInstance("SHA-256")

    // digest() method called
    // to calculate message digest of an input
    // and return array of byte
    val messageDigest = md.digest(input.toByteArray())

    // Convert byte array into signum representation
    val no = BigInteger(1, messageDigest)

    // Convert message digest into hex value
    var hashtext = no.toString(16)

    while (hashtext.length < 32) {
        hashtext = "0$hashtext"
    }

    return hashtext
}