package com.mcal.apksigner

import com.android.apksig.ApkSigner
import com.android.apksigner.ApkSignerTool
import com.mcal.apksigner.utils.KeyStoreFileManager
import java.io.File
import java.security.PrivateKey
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
        pk8File: File,
        x509File: File,
        v1SigningEnabled: Boolean,
        v2SigningEnabled: Boolean,
        v3SigningEnabled: Boolean,
        v4SigningEnabled: Boolean,
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
            x509File.path,
        )
        if (v1SigningEnabled) {
            args.add("--v1-signing-enabled")
        }
        if (v2SigningEnabled) {
            args.add("--v2-signing-enabled")
        }
        if (v3SigningEnabled) {
            args.add("--v3-signing-enabled")
        }
        if (v4SigningEnabled) {
            args.add("--v4-signing-enabled")
        }
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
            val keystore = KeyStoreFileManager.loadKeyStore(keyFile.path, certPass.toCharArray())
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
            val keystore = KeyStoreFileManager.loadKeyStore(keyFile.path, certPass.toCharArray())
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
}
