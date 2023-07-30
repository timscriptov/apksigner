[![](https://jitpack.io/v/TimScriptov/apksigner.svg)](https://jitpack.io/#TimScriptov/apksigner)

# ApkSigner library Multiplatform
1. Sign with jks/bks, pk8 + x509.pem
2. Convert jks to bks, bks to jks, jks/bks to pk8 + x509.pem
3. Validate password
4. Create jks/bks


## Add it in your root build.gradle at the end of repositories:
```groovy
    allprojects {
        repositories {
            //...
            maven { url 'https://jitpack.io' }
        }
    }
```

## Add the dependency
```groovy
    dependencies {
        implementation 'com.github.TimScriptov:apksigner:Tag'
    }
```

## Sign apk with pk8 and x509.pem
```kotlin
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.pk8"), File("path/key.x509.pem"))
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.pk8"), File("path/key.x509.pem"), true/*v1SigningEnabled*/, true/*v2SigningEnabled*/, true/*v3SigningEnabled*/, false/*v4SigningEnabled*/);
```

```java
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.pk8"), new File("path/key.x509.pem"));
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.pk8"), new File("path/key.x509.pem"), true/*v1SigningEnabled*/, true/*v2SigningEnabled*/, true/*v3SigningEnabled*/, false/*v4SigningEnabled*/);
```

## Sign apk with jks/bks
```kotlin
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.jks"), "cert_pass", "cert_alias", "key_pass")
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.jks"), "cert_pass", "cert_alias", "key_pass", true/*v1SigningEnabled*/, true/*v2SigningEnabled*/, true/*v3SigningEnabled*/, false/*v4SigningEnabled*/)

    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.bks"), "cert_pass", "cert_alias", "key_pass")
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.bks"), "cert_pass", "cert_alias", "key_pass", true/*v1SigningEnabled*/, true/*v2SigningEnabled*/, true/*v3SigningEnabled*/, false/*v4SigningEnabled*/)
```

```java
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.jks"), "cert_pass", "cert_alias", "key_pass");
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.jks"), "cert_pass", "cert_alias", "key_pass", true/*v1SigningEnabled*/, true/*v2SigningEnabled*/, true/*v3SigningEnabled*/, false/*v4SigningEnabled*/);

    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.bks"), "cert_pass", "cert_alias", "key_pass");
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.bks"), "cert_pass", "cert_alias", "key_pass", true/*v1SigningEnabled*/, true/*v2SigningEnabled*/, true/*v3SigningEnabled*/, false/*v4SigningEnabled*/);
```

## Convert jks to bks
```kotlin
    CertConverter.convert(File("path/key.jks"), File("path/key.bks"), "password", "alias_password")
    CertConverter.convert(File("path/key.jks"), File("path/key.bks"), "password", "alias", "alias_password")
```

```java
    CertConverter.convert(new File("path/key.jks"), new File("path/key.bks"), "password", "alias_password");
    CertConverter.convert(new File("path/key.jks"), new File("path/key.bks"), "password", "alias", "alias_password");
```

## Convert bks to jks
```kotlin
    CertConverter.convert(File("path/key.bks"), File("path/key.jks"), "password", "alias_password")
    CertConverter.convert(File("path/key.bks"), File("path/key.jks"), "password", "alias", "alias_password")
```

```java
    CertConverter.convert(new File("path/key.jks"), new File("path/key.bks"), "password", "alias_password");
    CertConverter.convert(new File("path/key.jks"), new File("path/key.bks"), "password", "alias", "alias_password");
```

## Convert jks/bks to pk8 and x509.pem
```kotlin
    CertConverter.convert(File("path/key.jks"), "password", "alias_password", File("path/key.pk8"), File("path/key.x509.pem"))
    CertConverter.convert(File("path/key.jks"), "password", "alias", "alias_password", File("path/key.pk8"), File("path/key.x509.pem"))

    CertConverter.convert(File("path/key.bks"), "password", "alias_password", File("path/key.pk8"), File("path/key.x509.pem"))
    CertConverter.convert(File("path/key.bks"), "password", "alias", "alias_password", File("path/key.pk8"), File("path/key.x509.pem"))
```

```java
    CertConverter.convert(new File("path/key.jks"), "password", "alias_password", new File("path/key.pk8"), new File("path/key.x509.pem"));
    CertConverter.convert(new File("path/key.jks"), "password", "alias", "alias_password", new File("path/key.pk8"), new File("path/key.x509.pem"));

    CertConverter.convert(new File("path/key.bks"), "password", "alias_password", new File("path/key.pk8"), new File("path/key.x509.pem"));
    CertConverter.convert(new File("path/key.bks"), "password", "alias", "alias_password", new File("path/key.pk8"), new File("path/key.x509.pem"));
```

## Create jks/bks
```kotlin
    CertCreator.createKeystoreAndKey("path/key.jks", "password", "alias", DistinguishedNameValues())

    CertCreator.createKeystoreAndKey("path/key.bks", "password", "alias", DistinguishedNameValues())
```

```java
    CertCreator.createKeystoreAndKey("path/key.jks", "password", "alias", new DistinguishedNameValues());

    CertCreator.createKeystoreAndKey("path/key.bks", "password", "alias", new DistinguishedNameValues());
```

## Validate password jks/bks
```kotlin
    KeyStoreHelper.validateKeystorePassword("path/key.jks", "password")

    KeyStoreHelper.validateKeystorePassword("path/key.bks", "password")
```

```java
    KeyStoreHelper.validateKeystorePassword("path/key.jks", "password", "alias");

    KeyStoreHelper.validateKeystorePassword("path/key.bks", "password", "alias");
```
