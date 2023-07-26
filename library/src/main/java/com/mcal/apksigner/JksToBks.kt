package com.mcal.apksigner

import com.mcal.apksigner.utils.KeyStoreFileManager
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore

object JksToBks {
    @JvmStatic
    fun convert(jksFile: File, bksFile: File, jksPassword: String, bksPassword: String): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val jksKeyStore =
                KeyStoreFileManager.loadKeyStore(jksFile.path, jksPassword.toCharArray())
            jksInputStream.close()

            val bksKeyStore = loadBks(bksPassword.toCharArray())
            val aliases = jksKeyStore.aliases()
            while (aliases.hasMoreElements()) {
                val alias = aliases.nextElement()
                val key = jksKeyStore.getKey(alias, jksPassword.toCharArray())
                val chain = jksKeyStore.getCertificateChain(alias)
                bksKeyStore.setKeyEntry(alias, key, bksPassword.toCharArray(), chain)
            }

            val bksOutputStream = FileOutputStream(bksFile)
            bksKeyStore.store(bksOutputStream, bksPassword.toCharArray())
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
