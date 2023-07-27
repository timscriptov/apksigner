package com.mcal.apksigner

import com.mcal.apksigner.utils.KeyStoreFileManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore

object JksToBks {
    @JvmStatic
    fun convert(
        jksFile: File,
        bksFile: File,
        password: String,
        alias: String,
        aliasPassword: String
    ): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val jksKeyStore =
                KeyStoreFileManager.loadKeyStore(jksFile.path, password.toCharArray())
            jksInputStream.close()

            val bksKeyStore = loadBks(/* new password */password.toCharArray())
            val key = jksKeyStore.getKey(alias, aliasPassword.toCharArray())
            val chain = jksKeyStore.getCertificateChain(alias)
            bksKeyStore.setKeyEntry(alias, key, /* new password */password.toCharArray(), chain)

            val bksOutputStream = FileOutputStream(bksFile)
            bksKeyStore.store(bksOutputStream, /* new password */password.toCharArray())
            bksOutputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    fun convert(
        jksFile: File,
        bksFile: File,
        password: String,
        aliasPassword: String
    ): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val jksKeyStore =
                KeyStoreFileManager.loadKeyStore(jksFile.path, password.toCharArray())
            jksInputStream.close()

            val bksKeyStore = loadBks(/* new password */password.toCharArray())
            val alias = jksKeyStore.aliases().nextElement()
            val key = jksKeyStore.getKey(alias, aliasPassword.toCharArray())
            val chain = jksKeyStore.getCertificateChain(alias)
            bksKeyStore.setKeyEntry(alias, key, /* new password */password.toCharArray(), chain)

            val bksOutputStream = FileOutputStream(bksFile)
            bksKeyStore.store(bksOutputStream, /* new password */password.toCharArray())
            bksOutputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Throws(Exception::class)
    private fun loadBks(password: CharArray): KeyStore {
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("BKS", "BC")
            keyStore.load(null, password)
        } catch (e: Exception) {
            throw RuntimeException("Failed to load keystore: " + e.message)
        }
        return keyStore
    }
}
