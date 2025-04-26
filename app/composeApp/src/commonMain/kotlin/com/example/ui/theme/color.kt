package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Common colors
val backgroundColor = Color(0xFF1E1E2E)   // Dark background
val primaryPastel = Color(0xFFFBE7A1)      // Primary soft pastel
val accentPastel = Color(0xFFBCAEFF)       // Accent soft pastel
val cardColor = Color(0xFF2B2B3C)          // Dark card color
val lightCardColor = Color(0xFF3A3A4F)     // Slightly lighter dark card (for contrast)
val successColor = Color(0xFF9AEBA3)       // Soft green
val errorColor = Color(0xFFFF8A8A)         // Soft red
val softWhite = Color(0xFFECECF1)          // Text color on dark
val softTextColor = Color(0xFFB0B0C0)      // Secondary text color

// Extra colors for light theme
val lightBackground = Color(0xFFFFFFFF)    // White background
val lightPrimary = Color(0xFF5B4B00)        // Darker yellow-ish (for primary on white)
val lightAccent = Color(0xFF7C4DFF)         // Purple accent
val lightSuccess = Color(0xFF388E3C)        // Darker green
val lightError = Color(0xFFD32F2F)          // Darker red
val lightOnBackground = Color(0xFF212121)   // Blackish text
val lightSecondary = Color(0xFF757575)      // Grey secondary
val lightSurface = Color(0xFFF5F5F5)         // Light card surface
val lightDivider = Color(0xFFEEEEEE)         // Divider color
val lightDisabled = Color(0xFFBDBDBD)        // Disabled color
val lightBorder = Color(0xFFE0E0E0)          // Light border
val lightIcon = Color(0xFF616161)            // Dark grey icons

// ThemeColors Data Class
data class ThemeColors(
    val background: Color,
    val onBackground: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val accent: Color,
    val onAccent: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color,
    val success: Color,
    val onSuccess: Color,
    val border: Color,
    val divider: Color,
    val disabled: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val iconColor: Color,
)

// --- DARK THEME ---
val darkTheme = ThemeColors(
    background = backgroundColor,
    onBackground = softWhite,
    primary = primaryPastel,
    onPrimary = backgroundColor,
    secondary = accentPastel,
    onSecondary = backgroundColor,
    accent = accentPastel,
    onAccent = backgroundColor,
    surface = cardColor,
    onSurface = softWhite,
    error = errorColor,
    onError = backgroundColor,
    success = successColor,
    onSuccess = backgroundColor,
    border = Color(0xFF444444),
    divider = Color(0xFF333333),
    disabled = Color(0xFF6B6B6B),
    textPrimary = softWhite,
    textSecondary = softTextColor,
    iconColor = softWhite,
)

// --- LIGHT THEME ---
val lightTheme = ThemeColors(
    background = lightBackground,
    onBackground = lightOnBackground,
    primary = lightPrimary,
    onPrimary = Color.White,
    secondary = lightSecondary,
    onSecondary = Color.White,
    accent = lightAccent,
    onAccent = Color.White,
    surface = lightSurface,
    onSurface = lightOnBackground,
    error = lightError,
    onError = Color.White,
    success = lightSuccess,
    onSuccess = Color.White,
    border = lightBorder,
    divider = lightDivider,
    disabled = lightDisabled,
    textPrimary = lightOnBackground,
    textSecondary = lightSecondary,
    iconColor = lightIcon,
)

