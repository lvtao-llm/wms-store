import { Fog } from "three";

function createFog(qt) {
  qt.scene.fog = new Fog("#ccc", 100, 500);
  qt.guis.fog = qt.gui.gui().addFolder("雾");
  qt.guis.fog.add(qt.scene.fog, "near").step(.5e3).max(1e4).min(0);
  qt.guis.fog.add(qt.scene.fog, "far").step(.5e3).max(1e4).min(0);
}

export { createFog };