package jsApp.test.domview

import DOMView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLElement


/**
 * 通过列表实现多个插入和删除
 */
fun multipleInsertRemove() {
    onWasmReady {
        var count = 0
        RenderingPrepare(title = "标题") {
            val listData = rememberSaveable { mutableStateListOf<String>("add-${count++}") }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ) {
                    Button(
                        onClick = {
                            listData.add(0, "add-${count++}")
                        }, modifier = Modifier.fillMaxWidth().height(50.dp).weight(1f)
                    ) {
                        Text("add 0")
                    }

                    Button(
                        onClick = {
                            listData.add("add-${count++}")
                        }, modifier = Modifier.fillMaxWidth().height(50.dp).weight(1f)
                    ) {
                        Text("add ")
                    }
//
                    Button(
                        onClick = {
                            console.log("removeRange 之前")
                            listData.forEachIndexed { index, str ->
                                console.log("index: $index; str: $str")
                            }
                            listData.removeRange(1, 2)
                            console.log("removeRange 之后")
                            listData.forEachIndexed { index, str ->
                                console.log("index: $index; str: $str")
                            }
                        }, modifier = Modifier.fillMaxWidth().height(50.dp).weight(1f)
                    ) {
                        Text("removeRange(1,2) ")
                    }
//
                    Button(
                        onClick = {
                            listData.clear()
                        }, modifier = Modifier.fillMaxWidth().height(50.dp).weight(1f)
                    ) {
                        Text("clear")
                    }

                }

                listData.forEach {
                    DOMView(modifier = Modifier.fillMaxWidth().height(30.dp).background(Color(0xFFAAAAAA)), factor = {
                        val el = window.document.createElement("div") as HTMLElement
                        el.innerHTML = it;
                        el.setAttribute(
                            "style", """
                                position: absolute;
                                background: yellow;
                            """.trimIndent()
                        )
                        el
                    }, onResize = {
                        (selfElement as HTMLElement).run {
                            style.width = "${rect.width}px"
                            style.height = "${rect.height}px"
                            style.top = "${rect.y}px"
                            style.left = "${rect.x}px"
                        }
                    }, onDestroy = {
                        console.log("onDestroy", this.selfElement)
                        selfElement.remove()
                    })
                }
            }

//            Row(
//                modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
//            ) {}
        }
    }
}
