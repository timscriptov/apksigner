[![](https://jitpack.io/v/TimScriptov/apksigner.svg)](https://jitpack.io/#TimScriptov/apksigner)

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

## TODO
Android 12+ not support JKS by Oracle. Google recommended use BKS

## Sign apk with pk8 and x509.pem
```kotlin
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.pk8"), File("path/key.x509.pem"))
```

```java
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.pk8"), new File("path/key.x509.pem"));
```

## Sign apk with pk8 and x509.pem + set signature version
```kotlin
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.pk8"), File("path/key.x509.pem"), v1SigningEnabled, v2SigningEnabled, v3SigningEnabled, v4SigningEnabled);
```

```java
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.pk8"), new File("path/key.x509.pem"), v1SigningEnabled, v2SigningEnabled, v3SigningEnabled, v4SigningEnabled);
```

## Sign apk with jks
```kotlin
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.jks"), "cert_pass", "cert_alias", "key_pass")
```

```java
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.jks"), "cert_pass", "cert_alias", "key_pass");
```

## Sign apk with jks + set signature version
```kotlin
    ApkSigner.sign(File("path/unsigned_apk.apk"), File("path/signed_apk.apk"), File("path/key.jks"), "cert_pass", "cert_alias", "key_pass", v1SigningEnabled, v2SigningEnabled, v3SigningEnabled, v4SigningEnabled)
```

```java
    ApkSigner.sign(new File("path/unsigned_apk.apk"), new File("path/signed_apk.apk"), new File("path/key.jks"), "cert_pass", "cert_alias", "key_pass", v1SigningEnabled, v2SigningEnabled, v3SigningEnabled, v4SigningEnabled);
```

## Convert jks to bks
```kotlin
    JksToBks.convert(File("path/key.jks"), File("path/key.bks"), "jks_password", "bks_password")
```

```java
    JksToBks.convert(new File("path/key.jks"), new File("path/key.bks"), "jks_password", "bks_password");
```
