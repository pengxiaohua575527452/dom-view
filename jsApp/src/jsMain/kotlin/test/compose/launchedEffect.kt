package jsApp.test.compose

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
import kotlinx.coroutines.delay
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady


/**
 * 用来验证 @Composable 在 list.forEach 的执行逻辑
 */



fun testLaunchedEffect() {
    @Composable
    fun Test(){
        // LaunchEffect 的执行分析
        // 当 Test 组合进入父组合的时候【也就是第一次执行的时候】
        // LaunchedEffect 会执行一次
        // 之后不管Test重新执行了任意次，都不会被再次调用
        // 但是如果传递给 LaunchedEffect 函数的状态参数发生了变化
        // 那么这个 LaunchedEffect 就会被再次执行
        LaunchedEffect(Unit){
            console.log("LaunchedEffect(Unit)")
        }
    }

//    @Composable
//    fun UpdateState(){
//        var isShow by remember { mutableStateOf(false) }
//        LaunchedEffect(isShow){
//            console.log("LaunchedEffect(Unit)")
//            delay(1000)
//            isShow = !isShow
//        }
//    }

    onWasmReady {
        var count = 0
        RenderingPrepare(title = "标题") {
            // 列表元素
            val listData = rememberSaveable { mutableStateListOf<String>() }
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
//                console.log("父组合重新执行了")
//                listData.forEachIndexed { index, s ->
//                    console.log("listData遍历执行了一次 index: $index")
//                    Test()
//                }
//                Test()

//                UpdateState()
            }
        }
    }
}




