import { mergeDeep, release } from "./object";

export default class EventDispatcher {
  constructor() {
    this.listeners = {};
  }

  addEventListener(type, listener, option) {
    const listeners = this.listeners;
    listeners[type] === undefined && (listeners[type] = []);
    listener.option = mergeDeep({ once: false }, option);
    !listeners[type].includes(listener) && listeners[type].push(listener);
    return this;
  }

  hasEventListener(type, listener) {
    const listeners = this.listeners;
    return listeners[type] !== undefined && listeners[type].includes(listener);
  }

  removeEventListener(type, listener) {
    const listeners = this.listeners;
    const listenerTyped = listeners[type];
    if (listenerTyped !== undefined) {
      const index = listenerTyped.indexOf(listener);
      index !== -1 && listenerTyped.splice(index, 1);
    }
    return this;
  }

  dispatchEvent(type, detail) {
    const listeners = this.listeners;
    type = (() => {
      if (typeof type === "string") {
        !detail && (detail = {});
        detail.type = type;
        return type;
      } else {
        detail = type;
        return detail.type;
      }
    })();
    const listenerTyped = listeners[type];
    if (Array.isArray(listenerTyped)) {
      const event = {};
      event.target = this;
      event.detail = detail;
      const listenerRemove = [];
      listenerTyped.slice(0).forEach(listener => {
        listener.call(this, event);
        listener.option.once && listenerRemove.push(listener);
      });
      listeners[type] = listeners[type].filter(listener => !listenerRemove.includes(listener));
    }
    return this;
  }

  destroy() {
    release(this.listeners);
  }
}
