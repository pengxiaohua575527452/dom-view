package test.domview

import CompositionStateInject
import CompositionStateProvider
import DOMView
import OnUpdate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.*

import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLElement


fun testState() {
    val el = window.document.createElement("div") as HTMLElement
    onWasmReady {
        RenderingPrepare(title = "标题") {
            var count by remember { mutableStateOf(0) }
            // 向 web 原生提供数据
            CompositionStateProvider("count", snapshotFlow { count })
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ) {
                    Button(
                        onClick = {
                            count++
                        }, modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                        Text("count++")
                    }
                }

                DOMView<HTMLElement, Int>(modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFAAAAAA)),
                    factor = {
                        el.innerHTML = count.toString()
                        el.setAttribute(
                            "style", """
                                position: absolute;
                                background: yellow;
                            """.trimIndent()
                        )
                        el
                    },
                    // listOf() 是用来标注使用那些 CompositionStateProvider 提供的数据
                    onUpdate = OnUpdate(listOf("count")){
                        when(key){
                            "count" -> selfElement.innerHTML = value.toString()
                            else -> console.error("还没有处理更新")
                        }
                    },
                    onResize = {
                        selfElement.run {
                            style.width = "${rect.width}px"
                            style.height = "${rect.height}px"
                            style.top = "${rect.y}px"
                            style.left = "${rect.x}px"
                        }
                    },
                    onDestroy = {
                        selfElement.remove()
                    })
            }
        }
    }
}