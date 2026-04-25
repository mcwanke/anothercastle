package dev.anothercastle.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dev.anothercastle.app.model.BrowserInfo
import dev.anothercastle.app.ui.BrowserPickerScreen
import dev.anothercastle.app.ui.theme.AnotherCastleTheme
import dev.anothercastle.app.viewmodel.BrowserPickerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BrowserPickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val incomingUrl = intent?.data?.toString()

        setContent {
            AnotherCastleTheme {
                BrowserPickerScreen(
                    url = incomingUrl,
                    onBrowserSelected = { browser ->
                        launchInBrowser(incomingUrl, browser)
                    },
                    viewModel = viewModel
                )
            }
        }
    }

    private fun launchInBrowser(url: String?, browser: BrowserInfo) {
        if (url == null) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setClassName(browser.packageName, browser.activityName)
        }
        startActivity(intent)
        finish()
    }
}