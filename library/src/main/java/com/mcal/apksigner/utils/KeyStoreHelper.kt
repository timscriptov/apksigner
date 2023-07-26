package com.mcal.apksigner.utils

import java.io.File
import java.io.FileInputStream
import java.security.KeyStore

object KeyStoreHelper {
    /**
     * TODO: JKS removed in Android 12+
     */
    @JvmStatic
    @Throws(Exception::class)
    fun loadJks(keyFile: File, password: CharArray): KeyStore {
        return loadJks(FileInputStream(keyFile), password)
    }

    /**
     * TODO: JKS removed in Android 12+
     */
    @JvmStatic
    @Throws(Exception::class)
    fun loadJks(inputStream: FileInputStream, password: CharArray): KeyStore {
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("JKS")
            keyStore.load(inputStream, password)
        } catch (e: Exception) {
            throw RuntimeException("Failed to load keystore: " + e.message)
        } finally {
            inputStream.close()
        }
        return keyStore
    }

    @JvmStatic
    @Throws(Exception::class)
    fun loadBks(keyFile: File, password: CharArray): KeyStore {
        val inputStream = FileInputStream(keyFile)
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("BKS")
            keyStore.load(inputStream, password)
        } catch (e: Exception) {
            throw RuntimeException("Failed to load keystore: " + e.message)
        } finally {
            inputStream.close()
        }
        return keyStore
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