import { PluginGui } from "./PluginGui";
import { PluginAnimation } from "./PluginAnimation";
import { PluginRenderer } from "./PluginRenderer";
import { PluginAdapt } from "./PluginAdapt";
import { PluginEvents } from "./PluginEvents";
import { PluginEdit } from "./PluginEdit";
import { PluginFns } from "./PluginFns";
import { PluginPerformance } from "./PluginPerformance";
import { PluginEffectComposer } from "./PluginEffectComposer";
import { PluginControl } from "./PluginControl";
import { PluginPerspective } from "./PluginPerspective";
import { PluginLoading } from "./PluginLoading";
import { PluginLoadingManager } from "./PluginLoadingManager";
import { PluginControlOrbit } from "./PluginControlOrbit";

const ChunkPluginsCommon = [
  PluginGui,
  PluginAnimation,
  {
    plugin: PluginRenderer,
    option: { enableShadow: true }
  },
  PluginAdapt,
  PluginEvents,
  PluginEdit,
  PluginFns,
  PluginPerformance
];
function getChunkPluginsTradition(loading) {
  return [
    ...ChunkPluginsCommon,
    PluginControlOrbit,
    PluginPerspective,
    { plugin: PluginLoading, option: loading || {} },
    PluginLoadingManager,
    PluginEffectComposer
  ];
}
function getChunkPlugins(loading) {
  return [
    ...ChunkPluginsCommon,
    PluginControl,
    { plugin: PluginLoading, option: loading || {} },
    PluginLoadingManager,
    PluginEffectComposer
  ];
}
export {
  ChunkPluginsCommon,
  getChunkPlugins,
  getChunkPluginsTradition
};
