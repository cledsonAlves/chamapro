package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = PureWhite,
    primaryContainer = DarkCharcoal,
    onPrimaryContainer = PureWhite,
    secondary = TextSecondary,
    onSecondary = PureWhite,
    secondaryContainer = SurfaceContainer,
    onSecondaryContainer = TextPrimary,
    tertiary = Black,
    onTertiary = PureWhite,
    background = SurfaceCanvas,
    onBackground = TextPrimary,
    surface = PureWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderHairline,
    error = AccentError,
    onError = PureWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = PureWhite,
    onPrimary = Black,
    primaryContainer = SurfaceContainer,
    onPrimaryContainer = PureWhite,
    secondary = SurfaceVariant,
    onSecondary = Black,
    secondaryContainer = DarkCharcoal,
    onSecondaryContainer = PureWhite,
    tertiary = PureWhite,
    onTertiary = Black,
    background = DarkCharcoal,
    onBackground = PureWhite,
    surface = Color(0xFF1E1E1E),
    onSurface = PureWhite,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFCCCCCC),
    outline = Color(0xFF404040),
    outlineVariant = Color(0xFF333333),
    error = AccentError,
    onError = PureWhite
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
