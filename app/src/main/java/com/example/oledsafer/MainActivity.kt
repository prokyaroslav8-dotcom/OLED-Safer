package com.example.oledsafer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

val AmoledBlack = Color(0xFF0B0E11)
val CardBackground = Color(0xFF161B22)
val AccentCyan = Color(0xFF4E9FBF)
val WarningRed = Color(0xFFBA1A1A)
val TextGray = Color(0xFF919EAB)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = AmoledBlack,
                    surface = CardBackground,
                    primary = AccentCyan
                )
            ) {
                MainScreenContainer()
            }
        }
    }
}

@Composable
fun MainScreenContainer() {
    var selectedTab by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1000) 
        isLoading = false
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = CardBackground, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
                    label = { Text("Главная") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = AccentCyan.copy(alpha = 0.3f))
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Настройки") },
                    label = { Text("Настройки") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = AccentCyan.copy(alpha = 0.3f))
                )
            }
        },
        containerColor = AmoledBlack
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = isLoading, animationSpec = tween(400), label = "loading") { loading ->
                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AccentCyan, strokeWidth = 3.dp)
                    }
                } else {
                    when (selectedTab) {
                        0 -> MainTab()
                        1 -> SettingsTab()
                    }
                }
            }
        }
    }
}

@Composable
fun MainTab() {
    var isOverlayEnabled by remember { mutableStateOf(false) }
    val hasOverlayPermission by remember { mutableStateOf(false) } 
    val hasAccessibilityPermission by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📱 OLED Saver",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        if (!hasOverlayPermission || !hasAccessibilityPermission) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = WarningRed.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⚠️ Требуются разрешения", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Для вывода полос под строкой состояния предоставьте допуски наложения поверх окон.",
                            color = TextGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "ℹ️", fontSize = 22.sp, modifier = Modifier.padding(end = 14.dp))
                    Text(
                        text = "OLED Saver снижает выгорание экрана с помощью настраиваемых черных полос, которые автоматически обходят системную строку состояния. Их можно временно скрыть одиночным нажатием.",
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Версия приложения", value = "1.4.120")
                    Divider(color = AmoledBlack, modifier = Modifier.padding(vertical = 12.dp))
                    InfoRow(label = "Разработчик", value = "iduchamp")
                    Divider(color = AmoledBlack, modifier = Modifier.padding(vertical = 12.dp))
                    
                    Text("Ссылки и ресурсы", color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SocialButton(text = "✈️ Личка", modifier = Modifier.weight(1f)) {}
                        SocialButton(text = "📢 Канал", modifier = Modifier.weight(1f)) {}
                        SocialButton(text = "📦 GitHub", modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }

        item {
            Button(
                onClick = { isOverlayEnabled = !isOverlayEnabled },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isOverlayEnabled) WarningRed else AccentCyan)
            ) {
                Text(text = if (isOverlayEnabled) "Убрать полосы" else "Включить OLED Saver", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingsTab() {
    var topBarWidth by remember { mutableStateOf(200f) }
    var bottomBarWidth by remember { mutableStateOf(97f) }
    var opacity by remember { mutableStateOf(100f) }
    var autoTikTok by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("⚙️ Настройки", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardBackground)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Параметры отображения", color = AccentCyan, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Ширина верхней полосы (dp): ${topBarWidth.toInt()}", color = Color.White)
                    Slider(value = topBarWidth, onValueChange = { topBarWidth = it }, valueRange = 0f..400f)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Ширина нижней полосы (dp): ${bottomBarWidth.toInt()}", color = Color.White)
                    Slider(value = bottomBarWidth, onValueChange = { bottomBarWidth = it }, valueRange = 0f..400f)

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Непрозрачность (%): ${opacity.toInt()}%", color = Color.White)
                    Slider(value = opacity, onValueChange = { opacity = it }, valueRange = 0f..100f)
                }
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardBackground)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Автоматическое включение в TikTok", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Автоматически разворачивать полосы защиты при обнаружении запуска TikTok", color = TextGray, fontSize = 12.sp)
                    }
                    Switch(checked = autoTikTok, onCheckedChange = { autoTikTok = it })
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = TextGray, fontSize = 14.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun SocialButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = AmoledBlack,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = text, color = AccentCyan, fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 10.dp))
    }
}
