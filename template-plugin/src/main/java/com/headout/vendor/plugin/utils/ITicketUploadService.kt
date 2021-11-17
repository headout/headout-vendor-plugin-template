package com.headout.vendor.plugin.utils

import java.io.InputStream

interface ITicketUploadService {
    fun uploadFile(reference: String, directory: String?, stream: InputStream): String
}

