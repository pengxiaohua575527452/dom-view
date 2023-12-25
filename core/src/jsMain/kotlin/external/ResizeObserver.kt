package external

import org.w3c.dom.DOMRect
import org.w3c.dom.DOMRectReadOnly
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

external class ResizeObserver(callback: (entries: Array<ResizeObserverEntry>, observer: ResizeObserver) -> Unit){
    fun disconnect(): Unit
    fun observe(el: Element, options: Options)
    fun observe(el: Element)
    fun unobserve(): Unit

    interface Options{
        val box: String
    }

    interface ResizeObserverEntry{
        val target: HTMLElement
        val borderBoxSize: Array<ResizeObserverSize>
        val contentBoxSize: Array<ResizeObserverSize>
        val devicePixelContentBoxSize: Array<ResizeObserverSize>
        val contentRect: DOMRectReadOnly
    }

    interface ResizeObserverSize{
        val inlineSize: Float
        val blockSize: Float
    }

}





//external interface ResizeObserverEntry{
//    val borderBoxSize:
//    一个对象，当运行回调时，该对象包含着正在观察元素的新边框盒的大小。
//
//    ResizeObserverEntry.contentBoxSize 只读
//    一个对象，当运行回调时，该对象包含着正在观察元素的新内容盒的大小。
//
//    ResizeObserverEntry.devicePixelContentBoxSize 只读
//    一个对象，当运行回调时，该对象包含着正在观察元素的新内容盒的大小（以设备像素为单位）。
//
//    ResizeObserverEntry.contentRect 只读
//    一个对象，当运行回调时，该对象包含着正在观察元素新大小的 DOMRectReadOnly 对象。请注意，这比以上两个属性有着更好的支持，但是它是 Resize Observer API 早期实现遗留下来的，出于对浏览器的兼容性原因，仍然被保留在规范中，并且在未来的版本中可能被弃用。
//
//    ResizeObserverEntry.target 只读
//}
//
//external interface