package com.proyek.kalender.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.proyek.kalender.R // Pastikan ini mengarah ke package aplikasimu

// 1. Definisi Warna Kustom (Pertahankan milikmu)
val BackgroundSurface = Color(0xFFFCF8FF)
val SurfaceContainerLow = Color(0xFFF6F2FF)
val PrimaryIndigo = Color(0xFF4956B4)
val OnSurfaceText = Color(0xFF302E56)
val WorkMint = Color(0xFF8EF4E9)
val PersonalOrange = Color(0xFFFFA184)
val DeepWorkPurple = Color(0xFFE9E5FF)

// 2. Mendaftarkan Font Manrope (Pastikan file ada di res/font/)
val ManropeFontFamily = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold)
)

// 3. Tipografi dengan Font Manrope (Memperbaiki bug sebelumnya)
val KalenderTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = OnSurfaceText
    ),
    titleMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        color = OnSurfaceText
    ),
    bodyMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = OnSurfaceText
    )
)

// 4. Memasukkan warnamu ke dalam skema bawaan Material 3
private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    background = BackgroundSurface,
    surface = BackgroundSurface,
    onPrimary = Color.White,
    onBackground = OnSurfaceText,
    onSurface = OnSurfaceText
)

@Composable
fun KalenderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Kita paksa menggunakan LightColorScheme agar desain "Editorial" selalu konsisten
    // (Warna dinamis Android 12+ dimatikan agar desain tidak ditimpa warna wallpaper HP)
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 5. Konfigurasi Edge-to-Edge (Status Bar Transparan)
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            // Membuat ikon status bar (baterai/jam/sinyal) menjadi warna gelap
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KalenderTypography, // DILURUSKAN: Menggunakan KalenderTypography milikmu
        content = content
    )
}