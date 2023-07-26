package com.mcal.apksigner

import com.mcal.apksigner.utils.KeyStoreHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object JksToBks {
    @JvmStatic
    fun convert(jksFile: File, bksFile: File, jksPassword: String, bksPassword: String): Boolean {
        return try {
            val jksInputStream = FileInputStream(jksFile)
            val jksKeyStore = KeyStoreHelper.loadJks(jksInputStream, jksPassword.toCharArray())
            jksInputStream.close()

            val bksKeyStore = KeyStoreHelper.loadBks(null, bksPassword.toCharArray(), "BC")
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
}
