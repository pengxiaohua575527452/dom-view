package org.dweb_browser.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.CanvasBasedWindow
import helper.DATA_PRE_ELEMENT_ID
import helper.compositionLocalCanvasId
import helper.compositionLocalParentId
import kotlinx.browser.window


import org.w3c.dom.*
import org.w3c.fetch.Headers
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import kotlin.js.JSON
import kotlin.js.Promise
import external.ResizeObserver
import kotlinx.coroutines.*


const val HTML_CANVAS_ELEMENT_ID = "rendering-prepare-canvas"
const val HTML_BODY_ELEMENT_ID = "rendering-prepare-parent"

/**
 * 最基础的准备工作
 * 基于body
 */
@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionName")
fun RenderingPrepare(title: String, content: @Composable () -> Unit) {
    (window.document.body ?: throw (Throwable(
        """
            window.document.body == null
            at RendingPrepare
            at renderingPrepare.kt
        """.trimIndent()
    ))).apply {
        setAttribute("id", HTML_BODY_ELEMENT_ID)
        // 创建和插入HTMLCanvasElement
        window.document.createElement("canvas").let {
            it.setAttribute("id", HTML_CANVAS_ELEMENT_ID)
            appendChild(it)
        }
//        var debounceJog: Job? = null
//        ResizeObserver{ entries, observer ->
//            debounceJog?.cancel()
//            debounceJog = CoroutineScope(Dispatchers.Main).launch {
//                delay(30)
//                val intSize = IntSize(entries[0].contentRect.width.toInt(), entries[0].contentRect.height.toInt())
//                if(canvasSize!!.complete(intSize)){
//                    canvasSize = CompletableDeferred<IntSize>()
//                    canvasSize?.complete(intSize)
//                    console.log("尺寸发生了改变")
//                }
//            }
//        }.observe(this)
        CanvasBasedWindow(title = title, canvasElementId = HTML_CANVAS_ELEMENT_ID
        ) {
            CompositionLocalProvider(compositionLocalParentId provides HTML_BODY_ELEMENT_ID) {
                CompositionLocalProvider(compositionLocalCanvasId provides HTML_CANVAS_ELEMENT_ID) {
                    content()
                }
            }
        }
    }
}

/**
 * 一个可以序列化的类
 *
 * 用来接受从js环境发送过来的parentElement的DOMRect数据
 */

@Suppress("")
class DOMResizeRect(
    val x: Double,
    val y: Double,
    val top: Double,
    val right: Double,
    val bottom: Double,
    val left: Double,
    val width: Double,
    val height: Double,
    val id: String
)


