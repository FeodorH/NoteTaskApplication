package com.example.note_task_app.data.utils

import android.content.Context
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

internal fun loadCertificateFromRaw(context: Context, rawResId: Int): X509Certificate {
    context.resources.openRawResource(rawResId).use { inputStream ->
        val certificateFactory = CertificateFactory.getInstance("X.509")
        return certificateFactory.generateCertificate(inputStream) as X509Certificate
    }
}