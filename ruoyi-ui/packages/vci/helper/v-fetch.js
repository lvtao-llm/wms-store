import { mergeDeep } from "@vci/helper/src/object";

const VfGlobalOption = {
  rootApi: "",
  method: "GET",
  headers: {},
  mode: "cors",
  credentials: "omit",
  serializeData: false,
  on: {
    beforeRequest: null,
    afterRequest: null,
    resolveData: null,
    error: null
  }
};
const vfs = [];
const IgnoreNotificationMessages = [
  "the user aborted a request.",
  "signal is aborted without reason",
  "vm is destroyed",
  "refresh be killed by next",
  "is aborts"
];

// v-fetch
class Vf {

  constructor(option) {
    this.option = mergeDeep({
      cache: "default",
      headers: {},
      data: {},
      method: null,
      url: null,
      mode: null,
      credentials: null,
      dataType: "json",
      on: {
        abort: null
      },
      serializeData: null,
      isFormRequestData: false,
      withRootApi: true,
      enableResolveData: true,
      enableGlobalError: true
    }, option);
    this.init();
  }

  static Config(globalOption) {
    return mergeDeep(VfGlobalOption, globalOption);
  }

  static abortAllVfs() {
    vfs.forEach(vf => vf.abort());
  }

  init() {
    VfGlobalOption.on.beforeRequest && VfGlobalOption.on.beforeRequest(this.option, this);
    const { method, headers, cache, on, data, isFormRequestData, mode, credentials, serializeData } = this.option;
    this.ac = new AbortController();
    this.ac.signal.addEventListener("abort", () => on.abort && on.abort(this));
    this.isGetRequest = (method || VfGlobalOption.method) === "GET";
    try {
      this.url = this.generateUrl();
      console.log(this.url, 11111);
      this.request = new Request(this.url, {
        cache,
        method: method || VfGlobalOption.method,
        headers: {
          ...VfGlobalOption.headers,
          ...headers
        },
        mode: mode || VfGlobalOption.mode,
        credentials: credentials || VfGlobalOption.credentials,
        body: (() => {
          if (!this.isGetRequest) {
            if (data instanceof FormData) return data;
            if (isFormRequestData) {
              const formData = new FormData();
              Object.keys(data).forEach(key => formData.append(key, data[key]));
              return formData;
            } else {
              const rs = serializeData === null ? VfGlobalOption.serializeData : serializeData;
              if (rs) return Object.keys(data).map(k => `${k}=${data[k]}`).join("&");
              else return JSON.stringify(data);
            }
          }
        })(),
        signal: this.ac.signal
      });
      vfs.push(this);
    } catch (e) {
      this.vfError = this.formatError(e);
    }
  }

  generateUrl() {
    const { url, data, withRootApi } = this.option;
    console.log(withRootApi, VfGlobalOption, 111111);
    return (withRootApi ? VfGlobalOption.rootApi : "") + url + (this.isGetRequest ? ((url.includes("?") ? "&" : (Object.keys(data).length > 0 ? "?" : "")) + Object.keys(data).map(k => `${k}=${data[k]}`).join("&")) : "");
  }

  fetch() {
    return new Promise((resolve, reject) => {
      const { dataType, enableResolveData } = this.option;
      if (this.vfError) {
        reject(this.vfError);
        this.removeFromVfs();
      } else fetch(this.request).then(response => {
        const afterResolve = data => {
          if (!VfGlobalOption.on.resolveData || !enableResolveData) resolve(data);
          else {
            try {
              resolve(VfGlobalOption.on.resolveData(data));
            } catch (e) {
              reject(this.formatError(e, response));
            }
          }
          VfGlobalOption.on.afterRequest && VfGlobalOption.on.afterRequest(data, this);
        };
        if (response.ok) {
          if (dataType === "json") response.json().then(data => afterResolve(data)).catch(e => reject(this.formatError(e, response)));
          if (dataType === "arrayBuffer") response.arrayBuffer().then(data => afterResolve(data)).catch(e => reject(this.formatError(e, response)));
          if (dataType === "blob") response.blob().then(data => afterResolve(data)).catch(e => reject(this.formatError(e, response)));
          if (dataType === "text") response.text().then(data => afterResolve(data)).catch(e => reject(this.formatError(e, response)));
        } else reject(this.formatError(response.statusText, response));
      }).catch(e => reject(this.formatError(e))).finally(() => this.removeFromVfs());
    });
  }

  abort() {
    this.ac.abort();
  }

  formatError(e, response) {
    const { enableGlobalError } = this.option;
    const error = { msg: typeof e === "string" ? (e || "服务异常,请稍后再试") : e.toString(), err: e, response };
    enableGlobalError && VfGlobalOption.on.error && VfGlobalOption.on.error(error);
    return error;
  }

  removeFromVfs() {
    const targetIndex = vfs.findIndex(vf => vf === this);
    targetIndex !== -1 && vfs.splice(targetIndex, 1);
  }
}

function vf() {
  const vfInstance = new Vf(...arguments);
  return {
    vfInstance,
    fetch: vfInstance.fetch()
  };
}
const PluginVf = {
  install: (Vue, option = {}) => {
    const { config, PluginNotification } = option;
    Vf.Config(mergeDeep({
      rootApi: window.rootApi,
      on: {
        resolveData(data) {
          if (data["code"] !== 200) throw new Error(data["msg"] || "服务异常");
          return data["data"];
        },
        error: e => {
          console.error(e);
          PluginNotification && !IgnoreNotificationMessages.some(inm => e.msg.includes(inm)) && PluginNotification.error(e.msg);
        }
      }
    }, config && config({ IgnoreNotificationMessages })));
    Vue.prototype.$vf = vf;
  }
};
export {
  Vf,
  vf,
  PluginVf
};
