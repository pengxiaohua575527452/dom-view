import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.Window
import jsApp.test.compose.testDisposableEffect
import jsApp.test.domview.multipleInsertRemove
import jsApp.test.domview.testSingleInertRemove
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.dweb_browser.compose.RenderingPrepare
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

fun main() {
    testSingleInertRemove()
//    multipleInsertRemove()
}

/**
 * 在没有isShow的Test内部的 rememberSaveable 是一个相同的状态
 * 但是如果在Show内，也就是在mutableState内是不同的
 * 便面了isShow的重复执行
 */
//var count = 0;
//var old: Element? = null
//@Composable
//inline fun Test(){
//    console.log("第${count++}次执行Test")
//    val el by rememberSaveable{ mutableStateOf<Element>(window.document.createElement("div")) }
//    console.log("old == el ${old == el}")
//    old = el
//    console.log("end------")
//
//    DisposableEffect(Unit){
//        console.log("at DisposableEffect")
//        onDispose {
//            console.log("at onDispose")
//        }
//    }
//}

/**
 * 如何避免列表的重复执行
 * Composable 在列表中的重用规则
 * 是一列表的index位标准的，
 * 如果是从后面添加的参数，那么前面执行的Div是使用的原来的 remember的
 * 列表的渲染是之前渲染的都会重用，同 index 相关，
 * 例如 listData.length 从 2 ->5 那么 第1，2执行的Compose函数里面的 remember 如果没有关联外部的参数是会重用的
 * 第3，4，5个执行的Compose函数是会创建新的
 *
 * 删除依然，
 * 如果 listData.length 从 5->2
 * listData.length 发生了变化，所以第3，4，5个Composable函数，就会触发 DisposableEffect.onDispose{} 函数，都是删除最后一个
 *
 */

//listData.forEachIndexed {index, name ->
//    Div("$index")
//}

//@Composable
//inline fun Div(
//    name: String
//){
//    console.log("执行了Div name: $name")
//    val el by remember { mutableStateOf(window.document.createElement("div")) }
//    el.innerHTML = name
//
//    LaunchedEffect(el){
//        console.log("新增加了元素 name: $name; 需要插入")
//    }
////    el.innerHTML = name
////    console.log("name: $name",map[name], "map[name] == el:${map[name] == el}")
////    map[name] = el;
////    console.log("---------------------- end")
//}


@Composable
inline fun Span(
    // key 作为一个标识符
    key: String
){
    val el by remember { mutableStateOf(window.document.createElement("div")) }
    val shouldUpdate = rememberUpdatedState(key)



    if (shouldUpdate.value == key ) {
        console.log("不需要更新")
    }else{
        console.log("需要更新")
    }
}


