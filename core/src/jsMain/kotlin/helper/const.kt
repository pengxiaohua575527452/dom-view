package helper

// 这里的常量之后需要全部优化
// 需要添加唯一性
const val EVENT_TYPE_CLEAR = "clear"
const val EVENT_TYPE_CLEAR_DONE = "clear-done"
const val EVENT_TYPE_REMOVE_NODE = "remove-node"

const val DATA_NEW_CLEAR = "data-new-clear"
const val DATA_PRE_ELEMENT_ID = "data-pre-element-id"
const val DATA_TIMEOUT_ID = "data-timeout-it"



// 保存全部的不是开发者用户使用的attr
// 下面范围内的attr用户不能够通过setAttribute()使用
val NO_USER_ATTR = listOf(
    "id",
    DATA_NEW_CLEAR,
    DATA_PRE_ELEMENT_ID,
    DATA_TIMEOUT_ID
)





