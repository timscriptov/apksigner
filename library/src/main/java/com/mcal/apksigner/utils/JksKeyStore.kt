package com.mcal.apksigner.utils

import java.security.KeyStore
import java.security.Provider

class JksKeyStore(provider: Provider) : KeyStore(JKS(), provider, "JKS")

