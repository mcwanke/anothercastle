package dev.anothercastle.app.model

import android.graphics.drawable.Drawable

data class BrowserInfo(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable
)