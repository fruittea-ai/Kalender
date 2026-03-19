package com.proyek.kalender.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BackgroundSurface = Color(0xFFFCF8FF) // Latar belakang utama
val SurfaceContainerLow = Color(0xFFF6F2FF)
val PrimaryIndigo = Color(0xFF4956B4)
val OnSurfaceText = Color(0xFF302E56) // Pengganti warna hitam
val WorkMint = Color(0xFF8EF4E9) // Warna acara kerja
val PersonalOrange = Color(0xFFFFA184) // Warna acara personal
val DeepWorkPurple = Color(0xFFE9E5FF)

val KalenderTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif, // Ganti dengan Manrope di project aslimu
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = OnSurfaceText
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Ganti dengan Manrope
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = OnSurfaceText
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif, // Ganti dengan Inter
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = OnSurfaceText
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun KalenderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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