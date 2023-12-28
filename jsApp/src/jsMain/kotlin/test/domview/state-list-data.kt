package test.domview

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLElement


fun testStateList() {
    val el = window.document.createElement("div") as HTMLElement
    var count = 0
    onWasmReady {
        RenderingPrepare(title = "标题") {
            val listData = remember { mutableStateListOf<Int>(count++) }
            // 向 web 原生提供数据
            val compositionStateListData = CompositionStateProvider("list", snapshotFlow{ listData.toList() })
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ) {
                    Button(
                        onClick = {
                            listData.add(0, count++)
                        }, modifier = Modifier.fillMaxWidth().height(60.dp).weight(1f)
                    ) {
                        Text("add 0")
                    }

                    Button(
                        onClick = {
                            listData.add(count++)
                        }, modifier = Modifier.fillMaxWidth().height(60.dp).weight(1f)
                    ) {
                        Text("add")
                    }

                    Button(
                        onClick = {
                            listData.removeRange(1,2)
                        }, modifier = Modifier.fillMaxWidth().height(60.dp).weight(1f)
                    ) {
                        Text("removeRange(1,2)")
                    }
                }

                DOMView<HTMLElement, List<Int>>(modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFAAAAAA)),
                    factor = {
                        el.innerHTML = listData.joinToString(separator = "-")
                        el.setAttribute(
                            "style", """
                                position: absolute;
                                background: yellow;
                            """.trimIndent()
                        )
                        el
                    },
                    onUpdate = OnUpdate(listOf(compositionStateListData)){
                       when(value){
                           is List<*> ->selfElement.innerHTML =  (value as List<*>).joinToString(separator = "-")
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