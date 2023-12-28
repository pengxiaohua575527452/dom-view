package jsApp.test.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady

fun testDisposableEffect() {
    var count = 0
    onWasmReady {
        RenderingPrepare(title = "title") {
            val listData = remember { mutableStateListOf("$count++") }
            var isShow by remember { mutableStateOf(true) }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Button(
                        onClick = {
                            listData.add(0, "${count++}")
                        }, modifier = Modifier.width(0.dp).fillMaxHeight().weight(1f)
                    ) { Text("add 0") }
                    Button(
                        onClick = {
                            listData.add("${count++}")
                        }, modifier = Modifier.width(0.dp).fillMaxHeight().weight(1f)
                    ) { Text("add") }

                    Button(
                        onClick = {
                            isShow = !isShow
                        }, modifier = Modifier.width(0.dp).fillMaxHeight().weight(1f)
                    ) { Text("isShow") }
                }

                if(isShow){

                }
                Test()

//                listData.forEach {
//                    Test()
//                }
            }
        }
    }
}



@Composable
inline fun Test() {
    console.log("Test execute")

    LaunchedEffect(Unit){
        // 如果传递给LaunchedEffect没有发生改变，就只会执行一次
        console.log("LaunchedEffect(Unit)")
    }

    DisposableEffect(Unit) {
        // 列表状态下这里只是执行一次，不会多次执行
        console.log("DisposableEffect execute")
        onDispose {
            console.log("on dispose")
        }
    }
}