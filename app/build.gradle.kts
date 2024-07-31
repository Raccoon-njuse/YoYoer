import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale


plugins {
    alias(libs.plugins.android.application)
}

android {
    defaultConfig {
        applicationId = "com.example.YoYoer"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("packJKS"){
            keyAlias = "YoYokey" // 别名
            keyPassword = "caoxin2002" // 密码
            storeFile = file("/Users/raccoon/apkpass") // 存储keystore或者是jks文件的路径
            storePassword = "caoxin2002" // 存储密码
        }
    }

    android.buildTypes.forEach {
            buildType ->
        // 拿到对应的任务类型名称，比如是release或debug,后面需要用它去拼接成对应的任务名称
        val typeName = buildType.name
        // 获取版本号versionName写到apk的民称中
        val versionName = android.defaultConfig.versionName
        // 往apk的名称中加入时间
        val date = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US).format(Date())
        // 按名称遍历productFlavors然后创建两个任务分别打release包和debug包
        android.productFlavors.map { it.name }
            .ifEmpty { listOf("") }
            .forEach {
                    flavorName->
                // 将获取到的名称首字母变为大写，比如：release变为Release
                val combineName = "${flavorName.capitalize()}${typeName.capitalize()}"
                // 为我们的任务命名：比如叫packRelease
                val taskName = "pack$combineName"
                // 找到打包的任务，比如release就是assembleRelease任务
                val originName = "assemble$combineName"
                // 创建一个任务专门做我们的自定义打包任务
                project.task(taskName){
                    // 为任务分组
                    group = "Pack apk"
                    // 执行我们的任务之前会先执行的任务，比如，打release包时会先执行assembleRelease任务
                    dependsOn(originName)
                    // 执行完任务后，我们将得到的APK 重命名并输出到根目录下的apks文件夹下
                    doLast{
                        copy{
                            from(File(project.buildDir,"outputs/apk/$typeName"))
                            into(File(rootDir,"apks"))
                            rename{"YoYoer-V$versionName-$date.apk"}
                            include("*.apk")
                        }
                    }
                }
            }
    }


    namespace = "com.example.YoYoer"
    compileSdk = 34


    buildTypes {
        // 通过前面配置的签名信息对应的标识符：packJKS拿到签名的配置信息
        // 保存在mySignConfig中，分别在debug和release中配置上就行了
        val mySignConfig = signingConfigs.getByName("packJKS")
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 配置release 的签名信息
            signingConfig = mySignConfig
        }

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 配置debug的签名信息
            signingConfig = mySignConfig
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("libs/poi-3.12-android-a.jar"))
    implementation(files("libs/poi-ooxml-schemas-3.12-20150511-a.jar"))
    implementation(files("libs/poi-ooxml-schemas-3.12-20150511-a.jar"))
    implementation(libs.monitor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}