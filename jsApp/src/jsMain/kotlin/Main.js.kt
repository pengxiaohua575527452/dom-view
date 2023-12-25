import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.Window
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLElement

fun main(){
    onWasmReady {
        RenderingPrepare(title="标题"){
            var isShow by remember { mutableStateOf(true) }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ){
                    Button(
                        onClick = {
                            isShow = !isShow
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    ){
                        Text("click")
                    }
                }

                DOMView(
                    show = isShow,
                    id="dom-view-1",
                    modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFAAAAAA)),
                    factor = {
                        val el = window.document.createElement("div")
                        parentElement.appendChild(el)
                        el.setAttribute("style", """
                        position: absolute;
                        top: ${this.rect.y}px;
                        left: ${this.rect.x}px;
                        width: 100px; 
                        height: 100px;
                        background: #ccc;
                    """.trimIndent())
                        el
                    },
                    onUpdate = {
                        (el as HTMLElement).run{
                            style.width = "${rect.width}px"
                            style.top = "${rect.y}px"
                            style.left = "${rect.x}px"
                        }
                    },
                    onDestroy = {
                        console.log("domView 销毁了", el)
                        el?.remove()
                    }
                ){
                    // TODO: 1 
                    // TODO: 需要支持类似 mutableState 的类似数据 MutableInsideState
                    // TODO: 让 DOMView 内部的非 Composable函数，能够订阅 外部Composable函数中
                    // TODO: mutableState 数据的突变

                    // TODO: 2 
                    // TODO: 需要实现一个类似  CompositionLocalProvider 的功能
                    parentElement.appendChild(window.document.createElement("div").apply {
                        innerHTML = "content"
                    })
                }

                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ){
                }
            }
        }
    }
}