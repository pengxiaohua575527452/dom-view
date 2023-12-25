package helper

import kotlinx.browser.window

/**
 * 通过数值在不同单位之间的转换
 * 这个是不需要的
 * kotlin/wasmJs 会动态的设置 composeCanvas 的 尺寸
 * 会让 ComposeCanvas 中的 dp 尺寸 同 css 的 px 尺寸保持一致
 */

class Converting {
    companion object {
        //        fun dp2px(valueDp: Dp): Double {
//            return valueDp.value * window.devicePixelRatio
//        }
//
//        fun px2dp(valuePx: Int): Dp{
//            return (valuePx / window.devicePixelRatio).dp
//        }
//
//        fun px2dp(valuePx: Float): Dp{
//            return (valuePx / window.devicePixelRatio).dp
//        }
//
//        fun px2dp(valuePx: Double): Dp{
//            return (valuePx / window.devicePixelRatio).dp
//        }
        fun composeElementSizeToWebSize(value: Int): Double {
            return (value / window.devicePixelRatio)
        }

        fun composeElementSizeToWebSize(value: Float): Double {
            return (value / window.devicePixelRatio)
        }

        fun composeElementSizeToWebSize(value: Double): Double {
            return (value / window.devicePixelRatio)
        }
    }
}