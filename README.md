[![](https://jitpack.io/v/TimScriptov/apkparser.svg)](https://jitpack.io/#TimScriptov/apkparser)

## Read AndroidManifest.xml
```kotlin
    val manifestData = ReadManifest(File("path"))
    val name = manifestData.applicationName
```

```java
    final ReadManifest manifestData = new ReadManifest(new File("path"));
    final String name = manifestData.applicationName;
```

## Update AndroidManifest.xml
```kotlin
    val editor = EditManifest(File("path"))
    editor.setApplicationName("com.mypackage.MyApp")
```

```java
    final EditManifest editor = new EditManifest(new File("path"));
    editor.setApplicationName("com.mypackage.MyApp");
```
