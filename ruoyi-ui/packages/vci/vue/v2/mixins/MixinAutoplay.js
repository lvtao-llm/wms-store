export default {
  data() {
    return {
      autoplay: {
        enabled: false,
        el: null,
        inter: -1,
        index: 0,
        max: Number.MAX_VALUE,
        timeSplit: 2e3,
        enableAlternateReverse: true,
        inDeValue: 1
      }
    };
  },
  beforeDestroy() {
    this.clearAutoplay();
  },
  methods: {
    checkEnabled(next) {
      if (!this.autoplay.enabled) console.warn(this._uid, "autoplay is disabled, please initAutoplay");
      else next();
    },
    initAutoPlay(el, index = 0, max = Number.MAX_VALUE, enableAlternateReverse = true, timeSplit = 5e3, enabled = false, autoPlay = false) {
      this.autoplay.el = el;
      el.addEventListener("mousemove", this.pause);
      el.addEventListener("mouseleave", this.play);
      this.updateAutoPlay(index, max, enableAlternateReverse, timeSplit, enabled, autoPlay);
      return this;
    },
    updateAutoPlay(index = 0, max = Number.MAX_VALUE, enableAlternateReverse = true, timeSplit = 5e3, enabled = true, autoPlay = true) {
      this.autoplay.index = index;
      this.autoplay.max = max;
      this.autoplay.timeSplit = timeSplit;
      this.autoplay.enableAlternateReverse = enableAlternateReverse;
      enabled && this.enabledAutoPlay(false);
      enabled && autoPlay && this.pause();
      enabled && autoPlay && this.play();
      return this;
    },
    updateAutoPlayIndex(index = 0) {
      this.autoplay.index = index;
    },
    updateAutoPlayIndexAndInDe(index = 0, inDeValue = 1) {
      this.autoplay.index = index;
      this.autoplay.inDeValue = inDeValue;
    },
    clearAutoplay() {
      this.autoplay.el.removeEventListener("mousemove", this.pause);
      this.autoplay.el.removeEventListener("mouseleave", this._play);
      this.pause();
      this.autoplay.enabled = false;
    },
    play() {
      // console.info(this._uid, "play - create");
      this.checkEnabled(() => this.autoplay.inter = setTimeout(() => {
        const indexOlder = this.autoplay.index;
        // console.info(this._uid, "play");
        if (this.autoplay.max < 2) {
          this.autoplay.index = 0;
        } else {
          if (this.autoplay.enableAlternateReverse) {
            this.autoplay.index += this.autoplay.inDeValue;
            if (this.autoplay.index >= this.autoplay.max) {
              this.autoplay.inDeValue = -1;
              this.autoplay.index = this.autoplay.max - 2;
            }
            if (this.autoplay.index < 0) {
              this.autoplay.inDeValue = 1;
              this.autoplay.index = 1;
            }
          } else {
            this.autoplay.index++;
            this.autoplay.index >= this.autoplay.max && (this.autoplay.index = 0);
          }
        }
        this.onAutoPlay && this.onAutoPlay(this.autoplay.index, indexOlder);
        this.play();
      }, this.autoplay.timeSplit));
    },
    pause() {
      // console.info(this._uid, "pause");
      clearTimeout(this.autoplay.inter);
    },
    disabledAutoPlay() {
      this.autoplay.enabled = false;
    },
    enabledAutoPlay(autoPlay = true) {
      this.autoplay.enabled = true;
      autoPlay && this.play();
    }
  }
};