package com.example.oledsafer

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.oledsafer.ui.theme.OLEDSaferTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OLEDSaferTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

data class AppInfo(val name: String, val packageName: String, val icon: android.graphics.Bitmap)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val prefs = remember { PreferencesManager(context) }
    
    var isActive by remember { mutableStateOf(prefs.isActive()) }
    var topHeight by remember { mutableFloatStateOf(prefs.getTopHeight().toFloat()) }
    var bottomHeight by remember { mutableFloatStateOf(prefs.getBottomHeight().toFloat()) }
    var opacity by remember { mutableFloatStateOf(prefs.getOpacity()) }
    var hideDuration by remember { mutableIntStateOf(prefs.getHideDuration()) }
    var targetApps by remember { mutableStateOf(prefs.getTargetApps()) }

    var installedApps by remember { mutableStateOf<List<AppInfo>>(emptyList()) }
    var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
            } else {
                pm.getInstalledApplications(0)
            }
            val appList = packages.filter { (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0 || it.packageName == "com.android.systemui"}
                .map {
                    AppInfo(
                        name = it.loadLabel(pm).toString(),
                        packageName = it.packageName,
                        icon = it.loadIcon(pm).toBitmap(100, 100)
                    )
                }.sortedBy { it.name }
            installedApps = appList
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!hasOverlayPermission) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(stringResource(R.string.permissions_required), style = MaterialTheme.typography.titleMedium)
                            Button(onClick = {
                                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
                                context.startActivity(intent)
                            }) {
                                Text(stringResource(R.string.grant_overlay))
                            }
                        }
                    }
                }
            }

            item {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                }) {
                    Text(stringResource(R.string.grant_accessibility))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.global_toggle), style = MaterialTheme.typography.titleLarge)
                    Switch(
                        checked = isActive,
                        onCheckedChange = {
                            isActive = it
                            prefs.setActive(it)
                            if (!it) OverlayService.stop(context)
                        }
                    )
                }
            }

            item { Divider() }

            item {
                Text("${stringResource(R.string.top_bar_height)}: ${topHeight.toInt()}")
                Slider(
                    value = topHeight,
                    onValueChange = { topHeight = it },
                    onValueChangeFinished = { 
                        prefs.setTopHeight(topHeight.toInt())
                        if (isActive) OverlayService.start(context)
                    },
                    valueRange = 0f..200f
                )
            }

            item {
                Text("${stringResource(R.string.bottom_bar_height)}: ${bottomHeight.toInt()}")
                Slider(
                    value = bottomHeight,
                    onValueChange = { bottomHeight = it },
                    onValueChangeFinished = { 
                        prefs.setBottomHeight(bottomHeight.toInt())
                        if (isActive) OverlayService.start(context)
                    },
                    valueRange = 0f..200f
                )
            }

            item {
                Text("${stringResource(R.string.opacity)}: ${(opacity * 100).toInt()}%")
                Slider(
                    value = opacity,
                    onValueChange = { opacity = it },
                    onValueChangeFinished = { 
                        prefs.setOpacity(opacity)
                        if (isActive) OverlayService.start(context)
                    },
                    valueRange = 0f..1f
                )
            }

            item {
                Text(stringResource(R.string.hide_duration), style = MaterialTheme.typography.titleMedium)
                val options = listOf(0, 3, 5, 10)
                val labels = listOf(R.string.duration_never, R.string.duration_3s, R.string.duration_5s, R.string.duration_10s)
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    options.forEachIndexed { index, duration ->
                        FilterChip(
                            selected = hideDuration == duration,
                            onClick = { 
                                hideDuration = duration
                                prefs.setHideDuration(duration)
                            },
                            label = { Text(stringResource(labels[index])) }
                        )
                    }
                }
            }

            item { Divider() }
            item { Text(stringResource(R.string.target_apps), style = MaterialTheme.typography.titleLarge) }

            items(installedApps) { app ->
                val isSelected = targetApps.contains(app.packageName)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = app.icon.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(app.name, modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = { checked ->
                            if (checked) prefs.addTargetApp(app.packageName) else prefs.removeTargetApp(app.packageName)
                            targetApps = prefs.getTargetApps()
                        }
                    )
                }
            }
        }
    }
}
