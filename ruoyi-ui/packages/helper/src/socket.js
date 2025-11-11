import { mergeDeep } from "./object";
import { clearScheduledTasks } from "./browser";

const log = (msg, e) => console.info(`ws: ${msg}`, e || "");
export default class Socket {
  constructor(option) {
    this.option = mergeDeep({
      url: null,
      debug: process.env.NODE_ENV !== "production",
      enableReconnectOnError: true,
      on: {
        receiveData: null,
        sendData: null,
        open: null,
        close: null,
        error: null
      }
    }, option);
    this.init();
  }

  init() {
    this.ws = null;
    this.inter = {
      delay: -1
    };
  }

  instantiateWs() {
    this.ws = new WebSocket(this.option.url);
    const { debug } = this.option;
    this.ws.onopen = e => {
      debug && log("连接成功", e);
      this.option.on.open && this.option.on.open(e);
    };
    this.ws.onclose = e => {
      debug && log("断开连接: ", e);
      this.option.on.close && this.option.on.close(e);
    };
    this.ws.onmessage = e => {
      debug && log("接收数据事件信息: ", e);
      try {
        const data = JSON.parse(e.data);
        debug && log("接收数据内容: ", data);
        this.option.on.receiveData && this.option.on.receiveData(data, e);
      } catch {
        const data = { message: e.data };
        debug && log("接收数据内容: ", data);
        this.option.on.receiveData && this.option.on.receiveData(data, e);
      }
    };
    this.ws.onerror = e => {
      debug && log("断开连接: ", e);
      this.option.on.error && this.option.on.error(e);
      this.option.enableReconnectOnError && this.reconnect();
    };
  }

  reconnect() {
    clearTimeout(this.inter.delay);
    this.inter.delay = setTimeout(() => {
      this.option.debug && log("正在尝试重新连接");
      this.instantiateWs();
    }, 3e3);
  }

  boot() {
    this.instantiateWs();
  }

  shutdown() {
    clearScheduledTasks(this.inter);
    this.ws && this.ws.close();
    this.ws = null;
  }

  destroy() {
    this.shutdown();
  }
}
