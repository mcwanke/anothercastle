package dev.anothercastle.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dev.anothercastle.app.ui.BrowserPickerScreen
import dev.anothercastle.app.ui.theme.AnotherCastleTheme
import dev.anothercastle.app.viewmodel.BrowserPickerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: BrowserPickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Grab the incoming URL if we were launched as a browser
        val incomingUrl = intent?.data?.toString()

        setContent {
            AnotherCastleTheme {
                BrowserPickerScreen(
                    url = incomingUrl,
                    viewModel = viewModel,
                    onBrowserSelected = { browser ->
                        launchInBrowser(incomingUrl, browser)
                    }
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
        finish() // Close AnotherCastle after forwarding
    }
}