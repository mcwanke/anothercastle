package dev.anothercastle.app.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.anothercastle.app.model.BrowserInfo
import dev.anothercastle.app.viewmodel.BrowserPickerViewModel
import androidx.compose.foundation.Image

@Composable
fun BrowserPickerScreen(
    url: String?,
    onBrowserSelected: (BrowserInfo) -> Unit,
    viewModel: BrowserPickerViewModel = viewModel()
) {
    val browsers by viewModel.browsers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "🏰 AnotherCastle",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = url ?: "No URL provided",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Open with...",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Browser list
        if (browsers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No browsers found")
            }
        } else {
            LazyColumn {
                items(browsers) { browser ->
                    BrowserRow(
                        browser = browser,
                        onClick = { onBrowserSelected(browser) }
                    )
                }
            }
        }
    }
}

@Composable
fun BrowserRow(
    browser: BrowserInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BrowserIcon(drawable = browser.icon)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = browser.label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun BrowserIcon(drawable: Drawable) {
    val bitmap = remember(drawable) {
        drawable.toBitmap(width = 96, height = 96)
    }
    Image(
        painter = BitmapPainter(bitmap.asImageBitmap()),
        contentDescription = null,
        modifier = Modifier.size(40.dp)
    )
}