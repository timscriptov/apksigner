package com.mcal.apksigner.app

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mcal.apksigner.ApkSigner
import com.mcal.apksigner.CertConverter
import com.mcal.apksigner.CertCreator
import com.mcal.apksigner.app.databinding.ActivityMainBinding
import com.mcal.apksigner.app.filepicker.FilePickHelper
import com.mcal.apksigner.utils.DistinguishedNameValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private var apkFile: File? = null
    private var pk8File: File? = null
    private var x509File: File? = null
    private var jksFile: File? = null

    private var pickApkFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    contentResolver.openInputStream(uri)?.let { inputStream ->
                        val name = FilePickHelper.getFileName(this, uri)
                        File(filesDir, name).also {
                            it.writeBytes(inputStream.readBytes())
                            apkFile = it
                        }
                        inputStream.close()
                        setEnabled(binding.signApkWithPem, true)
                        setEnabled(binding.signApkWithJks, true)
                        binding.selectApk.text = name
                    }
                }
            }
        }

    private var pickPk8File =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    contentResolver.openInputStream(uri)?.let { inputStream ->
                        val name = FilePickHelper.getFileName(this, uri)
                        File(filesDir, name).also {
                            it.writeBytes(inputStream.readBytes())
                            pk8File = it
                        }
                        inputStream.close()
                        binding.selectPk8.text = name
                    }
                }
            }
        }

    private var pickX509File =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    val name = FilePickHelper.getFileName(this, uri)
                    contentResolver.openInputStream(uri)?.let { inputStream ->
                        File(filesDir, name).also {
                            it.writeBytes(inputStream.readBytes())
                            x509File = it
                        }
                        inputStream.close()
                        binding.selectX509.text = name
                    }
                }
            }
        }

    private var pickJks =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    contentResolver.openInputStream(uri)?.let { inputStream ->
                        val name = FilePickHelper.getFileName(this, uri)
                        File(filesDir, name).also {
                            it.writeBytes(inputStream.readBytes())
                            jksFile = it
                        }
                        inputStream.close()
                        binding.selectJksKey.text = name
                        setEnabled(binding.jksToBks, true)
                        setEnabled(binding.jksToPk8AndX509, true)
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.selectApk.setOnClickListener {
            pickApkFile.launch(FilePickHelper.pickFile(true))
        }
        binding.selectPk8.setOnClickListener {
            pickPk8File.launch(FilePickHelper.pickFile(false))
        }
        binding.selectX509.setOnClickListener {
            pickX509File.launch(FilePickHelper.pickFile(false))
        }
        binding.selectJksKey.setOnClickListener {
            pickJks.launch(FilePickHelper.pickFile(false))
        }
        jks2bks()
        jks2pem()
        signApkWithJks()
        signApkWithPem()
        createKey()
    }

    private fun createKey() {
        binding.createKey.setOnClickListener {
            val password = binding.newPassword.text.toString().trim()
            if (password.isNotEmpty()) {
                val alias = binding.newAlias.text.toString().trim()
                if (alias.isNotEmpty()) {
                    val country = binding.country.text.toString().trim()
                    if (country.isNotEmpty()) {
                        val state = binding.state.text.toString().trim()
                        if (state.isNotEmpty()) {
                            val locality = binding.locality.text.toString().trim()
                            if (locality.isNotEmpty()) {
                                val street = binding.street.text.toString().trim()
                                if (street.isNotEmpty()) {
                                    val organization = binding.organization.text.toString().trim()
                                    if (organization.isNotEmpty()) {
                                        val organizationalUnit =
                                            binding.organizationalUnit.text.toString().trim()
                                        if (organizationalUnit.isNotEmpty()) {
                                            val commonName =
                                                binding.commonName.text.toString().trim()
                                            if (commonName.isNotEmpty()) {
                                                CertCreator.createKeystoreAndKey(
                                                    File(
                                                        getExternalFilesDir(null),
                                                        "$alias.jks"
                                                    ),
                                                    password.toCharArray(),
                                                    alias,
                                                    DistinguishedNameValues().apply {
                                                        setCountry(country)
                                                        setState(state)
                                                        setLocality(locality)
                                                        setStreet(street)
                                                        setOrganization(organization)
                                                        setOrganizationalUnit(organizationalUnit)
                                                        setCommonName(commonName)
                                                    })
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Enter Name!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Enter Department/Unit!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Enter Company/Organization!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Enter Street Address!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(this, "Enter City/Locality!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(this, "Enter State/Province!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Enter Country code!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Enter Alias!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signApkWithJks() {
        binding.signApkWithJks.setOnClickListener { view ->
            setEnabled(view, false)
            val apk = apkFile
            if (apk != null && apk.exists()) {
                val jks = jksFile
                if (jks != null && jks.exists()) {
                    val password = binding.password.text.toString().trim()
                    if (password.isNotEmpty()) {
                        val alias = binding.alias.text.toString().trim()
                        if (alias.isNotEmpty()) {
                            val aliasPassword = binding.aliasPassword.text.toString().trim()
                            if (aliasPassword.isNotEmpty()) {
                                val dialog = dialog().apply {
                                    show()
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    ApkSigner.sign(
                                        apk,
                                        File(getExternalFilesDir(null), "app_signed.apk"),
                                        jks,
                                        password,
                                        alias,
                                        aliasPassword,
                                        binding.v1SigningEnabled.isChecked,
                                        binding.v2SigningEnabled.isChecked,
                                        binding.v3SigningEnabled.isChecked,
                                        binding.v4SigningEnabled.isChecked,
                                    )
                                    withContext(Dispatchers.Main) {
                                        dialog.dismiss()
                                        setEnabled(view, true)
                                    }
                                }
                            } else {
                                Toast.makeText(this, "Enter Alias Password!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(this, "Enter Alias!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please select JKS Keystore!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select APK File!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun jks2bks() {
        binding.jksToBks.setOnClickListener {
            val jks = jksFile
            if (jks != null && jks.exists()) {
                val aliasPassword = binding.aliasPassword.text.toString().trim()
                if (aliasPassword.isNotEmpty()) {
                    CertConverter.convert(
                        jks,
                        File(getExternalFilesDir(null), jks.name + ".bks"),
                        aliasPassword,
                        aliasPassword
                    )
                } else {
                    Toast.makeText(this, "Enter Alias Password!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Please select JKS Keystore!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signApkWithPem() {
        binding.signApkWithPem.setOnClickListener { view ->
            setEnabled(view, false)
            val apk = apkFile
            if (apk != null && apk.exists()) {
                val pk8 = pk8File
                if (pk8 != null && pk8.exists()) {
                    val x509 = x509File
                    if (x509 != null && x509.exists()) {
                        val dialog = dialog().apply {
                            show()
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            ApkSigner.sign(
                                apk,
                                File(getExternalFilesDir(null), "app_signed.apk"),
                                pk8,
                                x509,
                                binding.v1SigningEnabled.isChecked,
                                binding.v2SigningEnabled.isChecked,
                                binding.v3SigningEnabled.isChecked,
                                binding.v4SigningEnabled.isChecked,
                            )
                            withContext(Dispatchers.Main) {
                                dialog.dismiss()
                                setEnabled(view, true)
                            }
                        }
                    } else {
                        Toast.makeText(this, "Please select x509.pem Keystore!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Please select pk8 Keystore!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select APK File!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun jks2pem() {
        binding.jksToPk8AndX509.setOnClickListener {
            val jks = jksFile
            if (jks != null && jks.exists()) {
                val password = binding.password.text.toString().trim()
                if (password.isNotEmpty()) {
                    val aliasPassword = binding.aliasPassword.text.toString().trim()
                    if (aliasPassword.isNotEmpty()) {
                        CertConverter.convert(
                            jks,
                            password,
                            aliasPassword,
                            File(getExternalFilesDir(null), jks.name + ".pk8"),
                            File(getExternalFilesDir(null), jks.name + ".x509.pem"),
                        )
                    } else {
                        Toast.makeText(this, "Enter Alias Password!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Please select JKS Keystore!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialog(): AlertDialog {
        return MaterialAlertDialogBuilder(this).apply {
            setView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(48, 48, 48, 48)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                addView(ProgressBar(context).apply {
                    setPadding(0, 48, 0, 48)
                })
                addView(AppCompatTextView(context).apply {
                    text = context.getString(R.string.signing)
                    setPadding(0, 0, 0, 48)
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            })
        }.create()
    }

    private fun setVisibility(view: View, mode: Int) {
        if (view.visibility != mode) {
            view.visibility = mode
        }
    }

    private fun setEnabled(view: View, mode: Boolean) {
        if (view.isEnabled != mode) {
            view.isEnabled = mode
        }
    }
}
