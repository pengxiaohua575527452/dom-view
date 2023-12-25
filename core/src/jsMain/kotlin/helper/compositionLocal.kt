package helper

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import kotlinx.browser.window
import org.w3c.dom.HTMLElement

val compositionLocalParentId: ProvidableCompositionLocal<String?> = compositionLocalOf {""}
val compositionLocalCanvasId: ProvidableCompositionLocal<String?> = compositionLocalOf {""}
