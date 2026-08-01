package yosel.dev.facturascan.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val draftContainer: Color,
    val onDraftContainer: Color,
    val savedContainer: Color,
    val onSavedContainer: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        draftContainer = Color.Unspecified,
        onDraftContainer = Color.Unspecified,
        savedContainer = Color.Unspecified,
        onSavedContainer = Color.Unspecified
    )
}

val LightExtendedColors = ExtendedColors(
    draftContainer = draftContainerLight,
    onDraftContainer = onDraftContainerLight,
    savedContainer = savedContainerLight,
    onSavedContainer = onSavedContainerLight
)

val DarkExtendedColors = ExtendedColors(
    draftContainer = draftContainerDark,
    onDraftContainer = onDraftContainerDark,
    savedContainer = savedContainerDark,
    onSavedContainer = onSavedContainerDark
)