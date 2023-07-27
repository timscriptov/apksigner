package com.mcal.apksigner

import com.mcal.apksigner.utils.Base64
import com.mcal.apksigner.utils.KeyStoreFileManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyPair
import java.security.PrivateKey


object JksToPem {
    @JvmStatic
    fun convert(
        jksFile: File,
        password: String,
        alias: String,
        aliasPassword: String,
        pk8File: File,
        pemFile: File
    ): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val keyStore = KeyStoreFileManager.loadKeyStore(jksFile.path, password.toCharArray())
            jksInputStream.close()

            val key = keyStore.getKey(alias, aliasPassword.toCharArray())
            val publicKey = keyStore.getCertificate(alias)
            val keyPair = KeyPair(publicKey.publicKey, key as PrivateKey)

            FileOutputStream(pk8File).use { pk8OutputStream ->
                pk8OutputStream.write(keyPair.private.encoded)
            }

            FileOutputStream(pemFile).use { pemOutputStream ->
                pemOutputStream.write("-----BEGIN CERTIFICATE-----\n".toByteArray())
                pemOutputStream.write(formatCertificate(publicKey.encoded))
                pemOutputStream.write("\n-----END CERTIFICATE-----\n".toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    fun convert(
        jksFile: File,
        password: String,
        aliasPassword: String,
        pk8File: File,
        pemFile: File
    ): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val keyStore = KeyStoreFileManager.loadKeyStore(jksFile.path, password.toCharArray())
            jksInputStream.close()

            val alias = keyStore.aliases().nextElement()
            val key = keyStore.getKey(alias, aliasPassword.toCharArray())
            val publicKey = keyStore.getCertificate(alias)
            val keyPair = KeyPair(publicKey.publicKey, key as PrivateKey)

            FileOutputStream(pk8File).use { pk8OutputStream ->
                pk8OutputStream.write(keyPair.private.encoded)
            }

            FileOutputStream(pemFile).use { pemOutputStream ->
                pemOutputStream.write("-----BEGIN CERTIFICATE-----\n".toByteArray())
                pemOutputStream.write(formatCertificate(publicKey.encoded))
                pemOutputStream.write("\n-----END CERTIFICATE-----\n".toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun formatCertificate(byteArray: ByteArray): ByteArray {
        return Base64.encode(byteArray).replace("(.{64})".toRegex(), "$1\n").toByteArray()
    }
}
