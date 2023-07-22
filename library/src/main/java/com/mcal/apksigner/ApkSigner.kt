package com.mcal.apksigner

import com.android.apksig.ApkSigner
import com.android.apksigner.ApkSignerTool
import com.mcal.apksigner.utils.JksKeyStore
import org.spongycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Security
import java.security.cert.X509Certificate

object ApkSigner {
    @JvmStatic
    fun sign(
        unsignedApkFile: File,
        signedApkFile: File,
        pk8File: File,
        x509File: File
    ): Boolean {
        val args = mutableListOf(
            "sign",
            "--in",
            unsignedApkFile.path,
            "--out",
            signedApkFile.path,
            "--key",
            pk8File.path,
            "--cert",
            x509File.path
        )
        return try {
            ApkSignerTool.main(args.toTypedArray())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    fun sign(
        unsignedApkFile: File,
        signedApkFile: File,
        keyFile: File,
        certPass: String,
        certAlias: String,
        keyPass: String,
    ): Boolean {
        return try {
            val keystore = loadKeyStore(FileInputStream(keyFile), certPass.toCharArray())
            ApkSigner.Builder(
                listOf(
                    ApkSigner.SignerConfig.Builder(
                        "CERT",
                        keystore.getKey(certAlias, keyPass.toCharArray()) as PrivateKey,
                        listOf(keystore.getCertificate(certAlias) as X509Certificate)
                    ).build()
                )
            ).apply {
                setInputApk(unsignedApkFile)
                setOutputApk(signedApkFile)
            }.build().sign()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    fun sign(
        unsignedApkFile: File,
        signedApkFile: File,
        keyFile: File,
        certPass: String,
        certAlias: String,
        keyPass: String,
        v1SigningEnabled: Boolean,
        v2SigningEnabled: Boolean,
        v3SigningEnabled: Boolean,
        v4SigningEnabled: Boolean,
    ): Boolean {
        return try {
            val keystore = loadKeyStore(FileInputStream(keyFile), certPass.toCharArray())
            ApkSigner.Builder(
                listOf(
                    ApkSigner.SignerConfig.Builder(
                        "CERT",
                        keystore.getKey(certAlias, keyPass.toCharArray()) as PrivateKey,
                        listOf(keystore.getCertificate(certAlias) as X509Certificate)
                    ).build()
                )
            ).apply {
                setInputApk(unsignedApkFile)
                setOutputApk(signedApkFile)
                setV1SigningEnabled(v1SigningEnabled)
                setV2SigningEnabled(v2SigningEnabled)
                setV3SigningEnabled(v3SigningEnabled)
                setV4SigningEnabled(v4SigningEnabled)
            }.build().sign()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @Throws(Exception::class)
    private fun loadKeyStore(keystorePath: FileInputStream, password: CharArray): KeyStore {
        var keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance("jks")
            keyStore.load(keystorePath, password)
        } catch (e: Exception) {
            val provider = BouncyCastleProvider()
            Security.addProvider(provider)
            try {
                keyStore = JksKeyStore(provider)
                keyStore.load(keystorePath, password)
            } catch (e: Exception) {
                try {
                    keyStore = KeyStore.getInstance("bks", provider)
                    keyStore.load(keystorePath, password)
                } catch (e: Exception) {
                    throw RuntimeException("Failed to load keystore: " + e.message)
                }
            }
        } finally {
            keystorePath.close()
        }
        return keyStore
    }
}