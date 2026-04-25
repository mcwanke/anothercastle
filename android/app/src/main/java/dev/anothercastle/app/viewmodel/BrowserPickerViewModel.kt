package dev.anothercastle.app.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import dev.anothercastle.app.model.BrowserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BrowserPickerViewModel(application: Application) : AndroidViewModel(application) {

    private val _browsers = MutableStateFlow<List<BrowserInfo>>(emptyList())
    val browsers: StateFlow<List<BrowserInfo>> = _browsers

    init {
        loadBrowsers()
    }

    private fun loadBrowsers() {
        val context = getApplication<Application>()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://"))
        val activities = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_ALL
        )

        _browsers.value = activities
            .filter { it.activityInfo.packageName != context.packageName }
            .map {
                BrowserInfo(
                    packageName = it.activityInfo.packageName,
                    activityName = it.activityInfo.name,
                    label = it.loadLabel(context.packageManager).toString(),
                    icon = it.loadIcon(context.packageManager)
                )
            }
            .sortedBy { it.label }
    }
}