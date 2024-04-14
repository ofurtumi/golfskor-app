package hugbo.golfskor.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
/**
 * Composable function that applies a theme to its child composables.
 *
 * This function sets up the color scheme based on system preferences for theme (dark or light) and,
 * if supported and enabled, uses dynamic coloring based on the current wallpaper (Android 12+).
 * It configures the MaterialTheme with these colors, along with default typography settings.
 *
 * Additionally, the function updates system UI settings such as the status bar color to match the
 * primary color of the chosen theme and sets the status bar text color to ensure good readability
 * depending on the darkness of the status bar color.
 *
 * @param darkTheme Optional boolean parameter that specifies whether to use a dark color scheme.
 *                  Defaults to the system preference.
 * @param dynamicColor Optional boolean parameter that, when true and supported by the device, enables
 *                     dynamic coloring based on the wallpaper colors. Defaults to true.
 * @param content A lambda representing the child composables that this theme will apply to. This allows
 *                all content enclosed within the theme to adhere to the defined color and typography settings.
 */
@Composable
fun GolfskorTheme(
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}