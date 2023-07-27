package com.mcal.apksigner

import com.mcal.apksigner.utils.Base64
import com.mcal.apksigner.utils.KeyStoreFileManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.PrivateKey
import java.security.cert.X509Certificate

object JksToPem {
    @JvmStatic
    fun convert(jksFile: File, jksPassword: String, pk8File: File, pemFile: File): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val keyStore = KeyStoreFileManager.loadKeyStore(jksFile.path, jksPassword.toCharArray())
            jksInputStream.close()

            val alias = keyStore.aliases().nextElement()

            FileOutputStream(pk8File).use { pk8OutputStream ->
                pk8OutputStream.write(
                    (keyStore.getKey(
                        alias,
                        jksPassword.toCharArray()
                    ) as PrivateKey).encoded
                )
            }

            FileOutputStream(pemFile).use { pemOutputStream ->
                pemOutputStream.write("-----BEGIN CERTIFICATE-----\n".toByteArray())
                pemOutputStream.write(
                    Base64.encode((keyStore.getCertificate(alias) as X509Certificate).encoded)
                        .replace("(.{64})".toRegex(), "$1\n").toByteArray()
                )
                pemOutputStream.write("\n-----END CERTIFICATE-----\n".toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
