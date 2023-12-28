import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * 目标
 * 实现把 @Composable 中的 State 转为 Web 中的State
 * - @Composable 中的 State 发生了改变，会触发 State的改变
 */


typealias CompositionStateInjectFun = (key: String, arg: Any) -> Unit

/**
 * 提供Compose的状态
 * 只能够在 @Composable 函数中使用
 */
@Composable
fun CompositionStateProvider(key: String, f: Flow<Any>): CompositionState {
    return remember { CompositionState(key, f) }
}


class CompositionState(
    private val key: String,
    private val f: Flow<Any>
){
    init {
        CoroutineScope(Dispatchers.Main).launch {
            f.collect{
                list.forEach { callback ->
                    callback(key, it)
                }
            }
        }
    }
    private val list = mutableListOf<CompositionStateInjectFun>()
    fun add(fn: CompositionStateInjectFun){
        list.add(fn)
    }
}
