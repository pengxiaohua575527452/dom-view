import communicationWithWasm from "./communication-with-wasm.mjs"

// 在全局添加一个对象
window.wasm = {
  // 实现同wasm通信的模块
  communicationWithWasm: communicationWithWasm
}



