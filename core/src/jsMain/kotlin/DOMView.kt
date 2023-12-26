import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import kotlinx.browser.window

import helper.Converting
import helper.compositionLocalCanvasId
import helper.compositionLocalParentId
import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement





@Composable
fun DOMView(
    modifier: Modifier,
    factor: DOMViewInitScope.() -> Element,
    // ？？onUpdate 的是否有使用的场景，跟新的实际是什么？？
    // ？？如果他 factory 返回的一个相同的 Element 会是什么情况了
    // onUpdate: DOMViewUpdateScope.() -> Unit,
    onResize: DOMViewResizeScope.() -> Unit,
    onDestroy: DOMViewDestroyScope.() -> Unit,
) {

    val parentElement = getParentElement<HTMLElement>(compositionLocalParentId.current)
    val canvasId = getCanvasId(compositionLocalCanvasId.current)
    var rect by remember { mutableStateOf(DOMRectReadOnly(x = 0.0, y = 0.0, width = 0.0, height = 0.0)) }
    val currentDOMViewInitScope = DOMViewInitScope(parentElement = parentElement)
    val element by remember{ mutableStateOf(factor(currentDOMViewInitScope)) }

    LaunchedEffect(element) {
        // 挂载到新的的节点上
        parentElement.appendChild(element)
    }

    LaunchedEffect(rect) {
        onResize(DOMViewResizeScope(element, rect))
    }

    DisposableEffect(Unit) {
        onDispose {
            onDestroy(DOMViewDestroyScope(element))
        }
    }

    Box(modifier = modifier.onGloballyPositioned {
        // DOMView 的实际尺寸
        rect = it.getBoundingClientRect(canvasId)
    })
}

class  DOMViewInitScope(
    val parentElement: Element
)

class DOMViewUpdateScope(val selfElement: Element)

class DOMViewDestroyScope(
    val selfElement: Element
)


class DOMViewResizeScope(
    val selfElement: Element,
    val rect: DOMRectReadOnly,
)


inline fun <reified T: Element> getParentElement(id: String? = null): T {
    return (id ?: throw (Throwable(
        """
            compositionLocalParentId.current? == null
            at DOMView
        """.trimIndent()
    ))).let {
        val el = (window.document.querySelector("#$it")?: throw (Throwable(
            """
                window.document.querySelector("#$it") == null
                at DOMView
            """.trimIndent()
        )))
        if(el is T) el else throw(Throwable("""
            el 不是匹配的类型
            at getParentElement
        """.trimIndent()))
    }
}

/**
 * 获取canvasId
 */
fun getCanvasId(id: String? = null): String {
    return id ?: throw (Throwable(
        """
        compositionLocalCanvasId.current == null
        at DOMView
    """.trimIndent()
    ))
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
        width = Converting.composeElementSizeToWebSize(size.width.toDouble()),
        height = Converting.composeElementSizeToWebSize(size.height.toDouble())
    )
}

