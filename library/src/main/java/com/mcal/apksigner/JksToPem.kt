package com.mcal.apksigner

import com.mcal.apksigner.utils.KeyStoreHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.PrivateKey

object JksToPem {
    @JvmStatic
    fun convert(jksFile: File, jksPassword: String, pk8File: File, pemFile: File): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val keyStore = KeyStoreHelper.loadJks(jksInputStream, jksPassword.toCharArray())
            jksInputStream.close()

            val alias = keyStore.aliases().nextElement()

            FileOutputStream(pk8File).apply {
                write((keyStore.getKey(alias, jksPassword.toCharArray()) as PrivateKey).encoded)
            }.close()

            FileOutputStream(pemFile).apply {
                write("-----BEGIN CERTIFICATE-----\n".toByteArray())
                write(keyStore.getCertificate(alias).encoded)
                write("\n-----END CERTIFICATE-----\n".toByteArray())
            }.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
