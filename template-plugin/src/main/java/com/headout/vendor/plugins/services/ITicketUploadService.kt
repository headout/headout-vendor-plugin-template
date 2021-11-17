package com.headout.vendor.plugins.services

import java.io.InputStream

interface ITicketUploadService {
    fun uploadFile(reference: String, directory: String?, stream: InputStream): String
}

