package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DeepCyan,
    secondary = BrightNeonBlue,
    tertiary = NeonCyan,
    background = DeepSlateBlue,
    surface = DarkSlate,
    onPrimary = White,
    onSecondary = White,
    onTertiary = DarkSlate,
    onBackground = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = DeepCyan,
    secondary = BrightNeonBlue,
    tertiary = NeonCyan,
    background = White,
    surface = OffWhite,
    onPrimary = White,
    onSecondary = White,
    onTertiary = DarkSlate,
    onBackground = DeepSlateBlue,
    onSurface = DeepSlateBlue
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
