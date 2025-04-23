plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
}
buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.9.2")
        classpath ("com.google.gms:google-services:4.4.0")
    }
}