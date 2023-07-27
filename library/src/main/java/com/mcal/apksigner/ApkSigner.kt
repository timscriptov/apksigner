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
        args.add("--v1-signing-enabled")
        args.add(v1SigningEnabled.toString())
        args.add("--v2-signing-enabled")
        args.add(v2SigningEnabled.toString())
        args.add("--v3-signing-enabled")
        args.add(v3SigningEnabled.toString())
        args.add("--v4-signing-enabled")
        args.add(v4SigningEnabled.toString())
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
        password: String,
        alias: String,
        aliasPassword: String,
    ): Boolean {
        return try {
            val keystore = KeyStoreFileManager.loadKeyStore(keyFile.path, password.toCharArray())
            ApkSigner.Builder(
                listOf(
                    ApkSigner.SignerConfig.Builder(
                        "CERT",
                        keystore.getKey(alias, aliasPassword.toCharArray()) as PrivateKey,
                        listOf(keystore.getCertificate(alias) as X509Certificate)
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
        password: String,
        alias: String,
        aliasPassword: String,
        v1SigningEnabled: Boolean,
        v2SigningEnabled: Boolean,
        v3SigningEnabled: Boolean,
        v4SigningEnabled: Boolean,
    ): Boolean {
        return try {
            val keystore = KeyStoreFileManager.loadKeyStore(keyFile.path, password.toCharArray())
            ApkSigner.Builder(
                listOf(
                    ApkSigner.SignerConfig.Builder(
                        "CERT",
                        keystore.getKey(alias, aliasPassword.toCharArray()) as PrivateKey,
                        listOf(keystore.getCertificate(alias) as X509Certificate)
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
