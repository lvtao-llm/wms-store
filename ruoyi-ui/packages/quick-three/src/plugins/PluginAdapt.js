import { QtPlugin } from "./QtPlugin";

class PluginAdapt extends QtPlugin {
  static namespace = "adapt";

  init() {
    super.init();
    this.resize();
    this._resize = this.resize.bind(this);
    window.addEventListener("resize", this._resize);
  }

  // 自适应
  resize() {
    const { qt } = this;
    const resize = () => {
      qt.cameras.forEach(camera => {
        if (camera.isPerspectiveCamera) camera.aspect = qt.elWidth / qt.elHeight;
        if (camera.isOrthographicCamera) {
          qt.left = -qt.elWidth / 2;
          qt.right = qt.elWidth / 2;
          qt.top = qt.elHeight / 2;
          qt.bottom = -qt.elHeight / 2;
        }
        camera.updateProjectionMatrix();
      });
      qt.plugins.forEach(plugin => plugin.dispatchEvent(QtPlugin.Events.Resize, {
        width: qt.elWidth,
        height: qt.elHeight
      }));
      qt.matrixChangedHandle(true);
    };
    if (qt.state.isMounted) {
      clearTimeout(qt.inter.resize);
      qt.inter.resize = setTimeout(() => resize(), 200);
    } else resize();
  }

  destroy() {
    window.removeEventListener("resize", this._resize);
  }
}

export { PluginAdapt };
