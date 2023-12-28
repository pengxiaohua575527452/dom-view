import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

/**
 * 目标
 * 实现把 @Composable 中的 State 转为 Web 中的State
 * - @Composable 中的 State 发生了改变，会触发 State的改变
 */


typealias CompositionStateInjectFun = (arg: Any) -> Unit
private val observeMap = mutableMapOf<String, MutableList<CompositionStateInjectFun>>()

/**
 * 想web原生注入compose 状态
 */
@Suppress("FunctionName")
fun  CompositionStateInject (key: String, inject: CompositionStateInjectFun){
    observeMap[key] = (observeMap[key]?:mutableListOf()).apply {
        add(inject)
    }
}

/**
 * 提供Compose的状态
 * 只能够在 @Composable 函数中使用
 */
@Composable
fun CompositionStateProvider(key: String, f: Flow<Any>){
    LaunchedEffect(Unit){
        observeMap[key] =  observeMap[key]?: mutableListOf()
        f.collect{
            observeMap[key]?.forEach { callback ->
                callback(it)
            }
        }
    }
}


//val compositionStates = mutableMapOf<CompositionState, List<CompositionStateInjectFun>>()

class CompositionState(
    val key: String,
    private val f: Flow<Any>
){
    private val list = mutableListOf<CompositionStateInjectFun>()

    fun onUpdate(fn: CompositionStateInjectFun){
        list.add(fn)
    }
}
