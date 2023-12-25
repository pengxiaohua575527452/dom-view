import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import kotlinx.browser.window

import helper.Converting
import helper.compositionLocalCanvasId
import helper.compositionLocalParentId
import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element


//val VIRTUAL_DOM  = mutableMapOf<String, VirtualNode>()
//
//
//class VirtualNode(
//    val id: String,
//    val el: Element,
//    val parentElement: Element
//    // 作为一个节点映射
////    val substitute: EventTarget
//)

val doms = mutableMapOf<String, Element>()


@Composable
fun DOMView(
    show: Boolean,
    id: String,
    modifier: Modifier,
    factor: DOMViewScope.() -> Element,
    onUpdate: DOMViewScope.() -> Unit,
    onDestroy: DOMViewScope.() -> Unit,
    content: DOMViewScope.() -> Unit
){
    console.log("isShow: ", show, doms[id])
    if(show && doms[id] == null){
        val parentElement = (compositionLocalParentId.current?:throw(Throwable("""
            compositionLocalParentId.current? == null
            at DOMView
        """.trimIndent()))).let {
                (window.document.querySelector("#$it")?:throw(Throwable("""
                window.document.querySelector("#$it") == null
                at DOMView
            """.trimIndent())))
        }

        val canvasId = compositionLocalCanvasId.current?:throw(Throwable("""
        compositionLocalCanvasId.current == null
        at DOMView
    """.trimIndent()))

        val scope: DOMViewScope = DOMViewScope(
            rect = DOMRectReadOnly(x = 0.0, y = 0.0, width = 0.0, height = 0.0),
            parentElement = parentElement,
            onUpdate = onUpdate
        )

        factor(scope).let {
            scope.el = it
            doms[id] = it
            content(DOMViewScope(
                rect = it.getBoundingClientRect(),
                parentElement = it,
            ))
        }

        Box(modifier = modifier.onGloballyPositioned{
            // DOMView 的实际尺寸
            val rect = it.getBoundingClientRect(canvasId)
            scope.rect = rect
        })
    }

    if(!show && doms[id] != null){
        val parentElement = (compositionLocalParentId.current?:throw(Throwable("""
            compositionLocalParentId.current? == null
            at DOMView
        """.trimIndent()))).let {
            (window.document.querySelector("#$it")?:throw(Throwable("""
                window.document.querySelector("#$it") == null
                at DOMView
            """.trimIndent())))
        }
        onDestroy(DOMViewScope(
            rect = DOMRectReadOnly(x = 0.0, y = 0.0, width = 0.0, height = 0.0),
            el = doms[id],
            parentElement = parentElement,
            onUpdate = onUpdate
        ))
        doms.remove(id)
    }
}

class DOMViewScope(
    rect: DOMRectReadOnly,
    var el: Element? = null,
    val parentElement: Element,
    private val onUpdate : (DOMViewScope.() -> Unit)? = null
){
    var rect: DOMRectReadOnly = rect
        get() = field
        set(value) {
            field = value
            onUpdate?.let{
                it()
            }
        }
}


/**
 * 扩展 LayoutCoordinates 的方法
 * 目标
 *  - 根据元素所在的canvas,计算元素的位置信息
 *  - 类似WebApi里面的element.getBoundingClientRect()方法
 *  - 获取到元素现对于视口的位置
 */
fun LayoutCoordinates.getBoundingClientRect(
    canvasElementId: String
): DOMRectReadOnly {
    val x: Double
    val y: Double
    val offset = run {
        val offsetX = Converting.composeElementSizeToWebSize(positionInWindow().x)
        val offsetY = Converting.composeElementSizeToWebSize(positionInWindow().y)
        object {
            val x = offsetX;
            val y = offsetY
        }
    }

    try {
        // 获取到了 ComposeCanvas 元素相对于视口的位置
        val canvasOffset = window.document.querySelector("#${canvasElementId}")!!.run {
            val rect = getBoundingClientRect()
            console.log("canvasRect: ", rect)
            console.log("offset.y", offset.y)
            object {
                val x = rect.x;
                val y = rect.y
            }
        }

        x = canvasOffset.x + offset.x
        y = canvasOffset.y + offset.y
    } catch (e: NullPointerException) {
        throw (Throwable(
            """
                window.document.querySelector(canvasElementId) == null
                canvasElementId = $canvasElementId
                at fun LayoutCoordinates.getBoundingClientRect
                at DomView.kt
            """.trimIndent()
        ))
    }
    return DOMRectReadOnly(
        x = x,
        y = y,
        width = size.width.toDouble(),
        height = size.height.toDouble()
    )
}

