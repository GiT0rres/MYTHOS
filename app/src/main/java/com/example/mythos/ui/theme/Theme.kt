package com.example.mythos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MythosColorScheme = darkColorScheme(
    primary = MythosGold,
    onPrimary = MythosBackground,
    secondary = MythosGoldLight,
    onSecondary = MythosBackground,
    background = MythosBackground,
    onBackground = MythosIvory,
    surface = MythosSurface,
    onSurface = MythosIvory,
    surfaceVariant = MythosSurfaceElevated,
    onSurfaceVariant = MythosMuted,
    outline = MythosGold
)

@Composable
fun MythosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // O app usa uma identidade visual fixa (museu noturno), independente do tema do sistema.
    MaterialTheme(
        colorScheme = MythosColorScheme,
        typography = MythosTypography,
        content = content
    )
}
