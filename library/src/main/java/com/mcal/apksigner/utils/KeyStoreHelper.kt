package com.mcal.apksigner.utils

import org.spongycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.Security

object KeyStoreHelper {
    @JvmStatic
    @Throws(Exception::class)
    fun loadJks(keyFile: File, password: CharArray): KeyStore {
        return loadJks(FileInputStream(keyFile), password)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun loadJks(inputStream: FileInputStream, password: CharArray): KeyStore {
        var keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("JKS")
            keyStore.load(inputStream, password)
        } catch (e: Exception) {
            val provider = BouncyCastleProvider()
            Security.addProvider(provider)
            try {
                keyStore = JksKeyStore(provider)
                keyStore.load(inputStream, password)
            } catch (e: Exception) {
                try {
                    keyStore = KeyStore.getInstance("BKS", provider)
                    keyStore.load(inputStream, password)
                } catch (e: Exception) {
                    throw RuntimeException("Failed to load keystore: " + e.message)
                }
            }
        } finally {
            inputStream.close()
        }
        return keyStore
    }

    @JvmStatic
    @Throws(Exception::class)
    fun loadBks(keyFile: File, password: CharArray): KeyStore {
        return loadBks(FileInputStream(keyFile), password)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun loadBks(inputStream: FileInputStream?, password: CharArray): KeyStore {
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("BKS")
            keyStore.load(inputStream, password)
        } catch (e: Exception) {
            throw RuntimeException("Failed to load keystore: " + e.message)
        } finally {
            inputStream?.close()
        }
        return keyStore
    }

    @JvmStatic
    @Throws(Exception::class)
    fun loadBks(inputStream: FileInputStream?, password: CharArray, provider: String): KeyStore {
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("BKS", provider)
            keyStore.load(inputStream, password)
        } catch (e: Exception) {
            throw RuntimeException("Failed to load keystore: " + e.message)
        } finally {
            inputStream?.close()
        }
        return keyStore
    }
}