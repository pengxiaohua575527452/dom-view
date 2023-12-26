package jsApp.test.domview

import DOMView
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
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLElement


fun testSingleInertRemove() {
    onWasmReady {
        var count = 0
        RenderingPrepare(title = "标题") {
            var isShow by remember { mutableStateOf(true) }
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ) {
                    Button(
                        onClick = {
                            isShow = !isShow
                        }, modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                        Text("click")
                    }
                }

                if (isShow) {
                    DOMView(modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFAAAAAA)), factor = {
                        val el = window.document.createElement("div") as HTMLElement
                        el.innerHTML = "content"
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
                        selfElement.remove()
                    })
                }else{
                    DOMView(modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFAAAAAA)), factor = {
                        val el = window.document.createElement("div") as HTMLElement
                        el.innerHTML = "content"
                        el.setAttribute(
                            "style", """
                                position: absolute;
                                background: blue;
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
                        selfElement.remove()
                    })
                }

                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFFF0000))
                ) {}
            }
        }
    }
}