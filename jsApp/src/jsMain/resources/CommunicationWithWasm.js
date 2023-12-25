
/**
 * 在js运行环境中用来通wasm通信的类
 * 在wasm需要有匹配的实例才能够使用
 * 在wasm发起链接之前，无法向wasm发送消息
 */
class CommunicationWithWasm{
  /**
   * 判断 wasm 是否已经链接成功
   */
  static hasConnectedWASM = false

  static PROTOCOL = "communication://"
  static HOSTNAME_TO_JS = "to.js"
  static HOSTNAME_TO_WASM = "to.wasm"
  static URL_PREFIX_TO_JS = `${CommunicationWithWasm.PROTOCOL}${CommunicationWithWasm.HOSTNAME_TO_JS}`
  static URL_PREFIX_TO_WASM = `${CommunicationWithWasm.PROTOCOL}${CommunicationWithWasm.HOSTNAME_TO_WASM}`
  /**
   * wasm向js运行环境发送监听DOM尺寸变化的请求路径
   * communication://to.js/1?id=xxx
   * example:
   * communication://to.js/1?id=body
   * 
   */
  static ACTION_WATCH_DOM_SIZE = "/watch_dom_size"

  /**
  * js环境向wasm发送DOM尺寸发生变化的请求路径
  */
  static ACTION_DOM_RESIZE = "/dom_resize"


  /**
   * wasm向js运行环境发送初始化链接的请求路径
   */
  static ACTION_INIT_DONE = "/init_done"

  static execute_resize_id = 0

  /**
   * 监听尺寸变化的observer
   * 单例模式
   */
  static RESIZE_OBSERVER = new ResizeObserver((entries, observer) => {
    for(const entry of entries){
      CommunicationWithWasm.toWasmDomResize(entry.target.id, entry.contentRect)
    }
  })

  constructor(){
    const that = this
    /**
     * 拦截fetch请求
     * 如果url符合本地处理的规则，就进行本地处理
     * 否则用标准的fetch请求
     * 
     * 具体的规则是:
     * 请求URL的协议+路径前缀是 communication://to.js
     */
    fetch = new Proxy(
      window.fetch,
      {
          apply: function(target, thisArg, args){
              const [input, init] = args;
              if(
                  (typeof input === "string" && input.startsWith(CommunicationWithWasm.URL_PREFIX_TO_JS))
                  || (input instanceof Request && input.url.startsWith(CommunicationWithWasm.URL_PREFIX_TO_JS))
              ){
                  return that.handleCommunication(input, init)
              }
             return target.apply(thisArg, args)
          }
      }
    )
  }

  handleCommunication(input, init){
    if(typeof input === "string"){
      return this.handleCommunicationString(input, init)
    }else{
      return this.handleCommunicationRequest(input, init)
    }
  }

  /**
   * 用来处理 input === String 类型的 fetch 请求
   * @param {string} input 
   * @param {RequestInit} init 
   */
  handleCommunicationString(input, init){
    switch(true){
      case input.startsWith(`${CommunicationWithWasm.URL_PREFIX_TO_JS}${CommunicationWithWasm.ACTION_INIT_DONE}`):
        return this.handleInitDone();
      case input.startsWith(`${CommunicationWithWasm.URL_PREFIX_TO_JS}${CommunicationWithWasm.ACTION_WATCH_DOM_SIZE}`):
        return this.handleWatchDomSize(input, init)
      default:
        throw new Error(`handleCommunicationString 没有匹配的 Input: ${input}`)
    }
  }

  /**
   * 用来处理 input === Request 的请求
   * @param {Request} input 
   * @param {RequestInit} init 
   */
    handleCommunicationRequest(input, init){
      throw new Error("还没有处理 handleCommunicationRequest")
    }

  /**
   * 
   * 处理初始化完成同wasm链接成功的处理器
   */
  handleInitDone(){
    CommunicationWithWasm.hasConnectedWASM = true
    return Promise.resolve(new Response(null, { status: 200, statusText: "ok" }))
  }


  /**
   * 处理监听某个DOM尺寸变化的请求
   * @param {Request | String} input 请求的url或者Request对象
   * @param {RequestInit} init 请求的RequestInit对象
   */
  handleWatchDomSize(input, init){
    const id = new URL(typeof input === "string" ? input : input.url).searchParams.get("id")
    CommunicationWithWasm.RESIZE_OBSERVER.observe(document.querySelector(`#${id}`))
    return Promise.resolve(new Response(null, {status: 200,statusText: "ok"}))
  }



  /***
   * example:
   * CommunicationWithWasm.toWasm("/path?value=1")
   * .then(async (res) => {
   *   
   * })
   * 
   */
  /**
   * 
   * @param {String} pathQuery  路径和查询参数
   * @param {*} init 
   * @returns 
   */
  static toWasm(pathQuery, init){
    // 后期优化可以把这个请求放在队列里面，当同 wasm 联系完成后，在发送
    if(CommunicationWithWasm.hasConnectedWASM === false) throw new Error("还没有同 wasm 创建链接 - 之后优化把请求放到队列中，链接成功后自动发送");
    // 还没有完成
    return fetch(`${CommunicationWithWasm.URL_PREFIX_TO_WASM}${pathQuery}`, init)
  }

  /**
   * 
   * 向 wasm 发送某个需要监听Dom尺寸发生改变的消息
   * @param {String} id 
   * @param {contentRect} contentRect 
   */
  static toWasmDomResize(id, contentRect){
    const body = {
      x: contentRect.x,
      y: contentRect.y, 
      top: contentRect.top,
      right: contentRect.right,
      bottom: contentRect.bottom,
      left: contentRect.left,
      width: contentRect.width,
      height: contentRect.height,
      id: id
    }
    CommunicationWithWasm.toWasm(
      CommunicationWithWasm.ACTION_DOM_RESIZE,
      {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
      }
    ).then(res => {
      if(res.status !== 200){
        throw new Error(`向wasm发送DOM Resize 失败 ${res.statusText}`)
      }
    })
  }
}

/**
 * 用来接受从 wasm 中发送过来的消息
 * wasm发送消息过来的规范格式
 * url 
 *  - communication://to.js/actionType
 *  - actionType 用来规范请求的行为类型
 *  
 */
const communicationWithWasm = new CommunicationWithWasm()

export default communicationWithWasm;

 