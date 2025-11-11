import Thing from "./Thing";
import { AnimationClip, AnimationMixer, LoopOnce, LoopPingPong, LoopRepeat } from "three";
import { mergeDeep } from "@vci/helper/src/object";

export default class Animator extends Thing {
  init() {
    super.init();
    this.isThingAnimator = true;
    this.animationActions = {};
    this.mixer = null;
    this.isPlayAnimations = false;
  }

  bootAnimation(o3) {
    !o3 && (o3 = this.object);
    this.registerAnimations(o3);
    return this;
  }

  getAnimationsName() {
    if (!this.animations) {
      console.warn("请先执行方法bootAnimation");
      return [];
    }
    return this.animations.map(a => a.name);
  }

  animate(delta) {
    super.animate(delta);
    this.mixer && this.mixer.update(delta);
  }

  // 注册模型动画
  registerAnimations(o3) {
    this.mixer && this.releaseMixer();
    this.mixer = new AnimationMixer(o3);
    this.animations = o3.animations;
    return this;
  }

  // 播放模型动画
  play(option) {
    option = mergeDeep({
      name: null,
      custom: null,
      loop: {
        pingPong: false,
        times: 1
      },
      timeScale: 1,
      clampWhenFinished: false
    }, option);
    const { name, custom, loop, timeScale, clampWhenFinished } = option;
    delete this._name;
    const animationClip = AnimationClip.findByName(this.object, name);
    if (animationClip) {
      loop.times = Math.max(1, loop.times);
      !this.getAction(name) && this.setAction(animationClip);
      const action = this.getAction(name);
      action.clampWhenFinished = clampWhenFinished;
      this._name = name;
      if (custom) custom(action);
      else {
        action.reset()
          .setEffectiveTimeScale(timeScale)
          .setLoop(
            loop.pingPong ? LoopPingPong : (loop.times === 1 ? LoopOnce : LoopRepeat),
            loop.times
          )
          .play();
      }
    } else this.qt.log.warn(`未匹配到名为“${name}”的动画`);
    return this;
  }

  // 暂停播放模型动画
  pause(name) {
    const action = this.getAction(name);
    action && (action.paused = true);
    return this;
  }

  // 恢复“暂停播放的模型动画”
  resume(name) {
    const action = this.getAction(name);
    action && (action.paused = false);
    return this;
  }

  // 停止播放模型动画
  stop(name) {
    const action = this.getAction(name);
    action && action.stop();
    return this;
  }

  playAnimations(nameAnimations, loop = false, timeScale = 1, call) {
    this.isPlayAnimations && this.stopAnimations();
    const animations = Array.isArray(nameAnimations) && nameAnimations.length > 0 ? this.filterAnimationsByName(nameAnimations) : this.animations;
    if (animations.length < 2) {
      this.play({
        name: animations[0].name,
        timeScale,
        loop: {
          pingPong: false,
          times: loop ? Infinity : 1
        }
      });
      return this;
    }
    this.isPlayAnimations = true;
    const handlePlay = finishedAnimationClip => {
      const indexAnimationsCurrent = finishedAnimationClip ? animations.indexOf(finishedAnimationClip) : -1;
      const indexPlay = indexAnimationsCurrent + 1;
      // console.info("indexPlay", indexPlay);
      if (indexPlay >= animations.length) {
        this.mixer.removeEventListener("finished", this._handlePlayEvent);
        this.isPlayAnimations = false;
        call && call();
        loop && this.playAnimations(nameAnimations, loop, timeScale, call);
      } else this.play({
        name: animations[indexPlay].name,
        timeScale
      });
    };
    handlePlay();
    this._handlePlayEvent = e => handlePlay(e.action.getClip());
    this.mixer.addEventListener("finished", this._handlePlayEvent);
    return this;
  }

  stopAnimations() {
    this.mixer.removeEventListener("finished", this._handlePlayEvent);
    this._name !== undefined && this.stop(this._name);
    this.isPlayAnimations = false;
  }

  pauseAnimations() {
    this._name !== undefined && this.pause(this._name);
    return this;
  }

  resumeAnimations() {
    this._name !== undefined && this.resume(this._name);
    return this;
  }

  filterAnimationsByName(nameAnimations = []) {
    return nameAnimations.reduce(
      (pool, na) => {
        const indexAnimationClip = this.animations.findIndex(clip => clip.name === na);
        if (indexAnimationClip !== -1) {
          const animationClip = this.animations[indexAnimationClip];
          animationClip.indexAnimationClip = indexAnimationClip;
          pool.push(animationClip);
        }
        return pool;
      },
      []
    );
  }

  setAction(clip) {
    if (clip) this.animationActions[clip.name] = this.mixer.clipAction(clip);
    return this.animationActions[clip.name];
  }

  getAction(name) {
    return this.animationActions[name];
  }

  // 释放mixer内存
  releaseMixer() {
    // 停止所有模型动画
    this.animationActions && Object.keys(this.animationActions).forEach(k => {
      const action = this.animationActions[k];
      delete this.animationActions[k];
      // 暂停动画
      action.stop();
      // 释放动作的内存资源
      this.mixer && this.mixer.uncacheAction(action.getClip(), this.object);
    });
    // 释放根对象的所有内存资源
    this.mixer && this.mixer.uncacheRoot(this.object);
    return this;
  }

  destroy(force = false) {
    super.destroy(force);
    // 如果正在播放连续动画，则清除
    this.isPlayAnimations && this.stopAnimations();
    // 释放mixer
    this.releaseMixer();
    return this;
  }
}
