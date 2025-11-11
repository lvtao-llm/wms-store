import { now } from "@vci/helper/src/browser";

export class Clock {
  constructor(autoStart = true) {
    this.autoStart = autoStart;
    this.startTime = 0;
    this.oldTime = 0;
    this.elapsedTime = 0;
    this.running = false;
  }

  start() {
    this.startTime = now();
    this.oldTime = this.startTime;
    this.elapsedTime = 0;
    this.running = true;
  }

  stop() {
    this.getElapsedTime();
    this.running = false;
    this.autoStart = false;
  }

  getElapsedTime() {
    this.getDelta();
    return this.elapsedTime;
  }

  getDelta() {
    this.diff = 0;
    if (this.autoStart && !this.running) {
      this.start();
      return false;
    }
    if (this.running) {
      const newTime = now();
      this.diff = (newTime - this.oldTime) / 1000;
      this.oldTime = newTime;
      this.elapsedTime += this.diff;
    }
    return this.diff;
  }
}