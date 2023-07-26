package com.mcal.apksigner

import org.spongycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.Security

object JksToBks {
    fun convert(jksFile: File, bksFile: File, jksPassword: String, bksPassword: String) {
        Security.addProvider(BouncyCastleProvider())

        val jksInputStream = FileInputStream(jksFile)
        val jksKeyStore = KeyStore.getInstance("JKS")
        jksKeyStore.load(jksInputStream, jksPassword.toCharArray())
        jksInputStream.close()

        val bksOutputStream = FileOutputStream(bksFile)
        val bksKeyStore = KeyStore.getInstance("BKS", "BC")
        bksKeyStore.load(null, bksPassword.toCharArray())

        val aliases = jksKeyStore.aliases()
        while (aliases.hasMoreElements()) {
            val alias = aliases.nextElement()
            val key = jksKeyStore.getKey(alias, jksPassword.toCharArray())
            val chain = jksKeyStore.getCertificateChain(alias)

            bksKeyStore.setKeyEntry(alias, key, bksPassword.toCharArray(), chain)
        }

        bksKeyStore.store(bksOutputStream, bksPassword.toCharArray())
        bksOutputStream.close()
    }
}