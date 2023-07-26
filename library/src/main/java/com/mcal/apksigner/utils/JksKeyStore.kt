package com.mcal.apksigner.utils

import java.security.KeyStore

class JksKeyStore : KeyStore(JKS(), KeyStoreFileManager.getProvider(), "JKS")
