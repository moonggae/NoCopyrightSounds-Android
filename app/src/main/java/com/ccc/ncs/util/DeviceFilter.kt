package com.ccc.ncs.util

import android.os.Build
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

fun isDeviceNexus5X(): Boolean =
    "Nexus 5X".uppercase() == Build.MODEL.also { Firebase.crashlytics.log(it) }.uppercase()

fun isDeviceOnePlus(): Boolean =
    "OnePlus8Pro".uppercase() == Build.MODEL.also { Firebase.crashlytics.log(it) }.uppercase()