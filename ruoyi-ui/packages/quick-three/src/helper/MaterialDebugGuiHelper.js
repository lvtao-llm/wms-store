import * as THREE from "three";

const constants = {
  combine: {
    "THREE.MultiplyOperation": THREE.MultiplyOperation,
    "THREE.MixOperation": THREE.MixOperation,
    "THREE.AddOperation": THREE.AddOperation
  },
  side: {
    "THREE.FrontSide": THREE.FrontSide,
    "THREE.BackSide": THREE.BackSide,
    "THREE.DoubleSide": THREE.DoubleSide
  },
  blendingDst: {
    "THREE.ZeroFactor": THREE.ZeroFactor,
    "THREE.OneFactor": THREE.OneFactor,
    "THREE.SrcColorFactor": THREE.SrcColorFactor,
    "THREE.OneMinusSrcColorFactor": THREE.OneMinusSrcColorFactor,
    "THREE.SrcAlphaFactor": THREE.SrcAlphaFactor,
    "THREE.OneMinusSrcAlphaFactor": THREE.OneMinusSrcAlphaFactor,
    "THREE.DstAlphaFactor": THREE.DstAlphaFactor,
    "THREE.OneMinusDstAlphaFactor": THREE.OneMinusDstAlphaFactor,
    "THREE.DstColorFactor": THREE.DstColorFactor,
    "THREE.OneMinusDstColorFactor": THREE.OneMinusDstColorFactor,
    "THREE.SrcAlphaSaturateFactor": THREE.SrcAlphaSaturateFactor
  },
  blendingMode: {
    "THREE.NoBlending": THREE.NoBlending,
    "THREE.NormalBlending": THREE.NormalBlending,
    "THREE.AdditiveBlending": THREE.AdditiveBlending,
    "THREE.SubtractiveBlending": THREE.SubtractiveBlending,
    "THREE.MultiplyBlending": THREE.MultiplyBlending,
    "THREE.CustomBlending": THREE.CustomBlending
  },
  depthFunc: {
    "THREE.NeverDepth": THREE.NeverDepth,
    "THREE.AlwaysDepth": THREE.AlwaysDepth,
    "THREE.LessDepth": THREE.LessDepth,
    "THREE.LessEqualDepth": THREE.LessEqualDepth,
    "THREE.GreaterEqualDepth": THREE.GreaterEqualDepth,
    "THREE.GreaterDepth": THREE.GreaterDepth,
    "THREE.NotEqualDepth": THREE.NotEqualDepth
  },
  blendEquation: {
    "THREE.AddEquation": THREE.AddEquation,
    "THREE.SubtractEquation": THREE.SubtractEquation,
    "THREE.ReverseSubtractEquation": THREE.ReverseSubtractEquation,
    "THREE.MinEquation": THREE.MinEquation,
    "THREE.MaxEquation": THREE.MaxEquation
  },
  blending: {
    "THREE.NoBlending": THREE.NoBlending,
    "THREE.NormalBlending": THREE.NormalBlending,
    "THREE.AdditiveBlending": THREE.AdditiveBlending,
    "THREE.SubtractiveBlending": THREE.SubtractiveBlending,
    "THREE.MultiplyBlending": THREE.MultiplyBlending,
    "THREE.CustomBlending": THREE.CustomBlending
  },
  blendSrc: {
    "THREE.ZeroFactor": THREE.ZeroFactor,
    "THREE.OneFactor": THREE.OneFactor,
    "THREE.SrcColorFactor": THREE.SrcColorFactor,
    "THREE.OneMinusSrcColorFactor": THREE.OneMinusSrcColorFactor,
    "THREE.SrcAlphaFactor": THREE.SrcAlphaFactor,
    "THREE.OneMinusSrcAlphaFactor": THREE.OneMinusSrcAlphaFactor,
    "THREE.DstAlphaFactor": THREE.DstAlphaFactor,
    "THREE.OneMinusDstAlphaFactor": THREE.OneMinusDstAlphaFactor,
    "THREE.DstColorFactor": THREE.DstColorFactor,
    "THREE.OneMinusDstColorFactor": THREE.OneMinusDstColorFactor,
    "THREE.SrcAlphaSaturateFactor": THREE.SrcAlphaSaturateFactor
  },
  equations: {
    "THREE.AddEquation": THREE.AddEquation,
    "THREE.SubtractEquation": THREE.SubtractEquation,
    "THREE.ReverseSubtractEquation": THREE.ReverseSubtractEquation
  },
  destinationFactors: {
    "THREE.ZeroFactor": THREE.ZeroFactor,
    "THREE.OneFactor": THREE.OneFactor,
    "THREE.SrcColorFactor": THREE.SrcColorFactor,
    "THREE.OneMinusSrcColorFactor": THREE.OneMinusSrcColorFactor,
    "THREE.SrcAlphaFactor": THREE.SrcAlphaFactor,
    "THREE.OneMinusSrcAlphaFactor": THREE.OneMinusSrcAlphaFactor,
    "THREE.DstAlphaFactor": THREE.DstAlphaFactor,
    "THREE.OneMinusDstAlphaFactor": THREE.OneMinusDstAlphaFactor
  },
  sourceFactors: {
    "THREE.DstColorFactor": THREE.DstColorFactor,
    "THREE.OneMinusDstColorFactor": THREE.OneMinusDstColorFactor,
    "THREE.SrcAlphaSaturateFactor": THREE.SrcAlphaSaturateFactor
  },
  colorSpace: {
    "NoColorSpace": THREE.NoColorSpace,
    "SRGBColorSpace": THREE.SRGBColorSpace,
    "LinearSRGBColorSpace": THREE.LinearSRGBColorSpace
  }
};
function guiMaterial(gui, material, geometry) {
  const folder = gui.addFolder("THREE.Material");
  const data = { color: material.color.getHex() };
  folder.addColor(data, "color").onChange(handleColorChange(material.color));
  folder.add(material, "transparent").onChange(needsUpdate(material, geometry));
  folder.add(material, "opacity", 0, 1).step(0.01);
  folder.add(material, "blending", constants.blendingMode);
  folder.add(material, "blendSrc", constants.destinationFactors);
  folder.add(material, "blendDst", constants.destinationFactors);
  folder.add(material, "blendEquation", constants.equations);
  folder.add(material, "depthTest");
  folder.add(material, "depthWrite");
  folder.add(material, "polygonOffset");
  folder.add(material, "polygonOffsetFactor");
  folder.add(material, "polygonOffsetUnits");
  folder.add(material, "alphaTest", 0, 1).step(0.01).onChange(needsUpdate(material, geometry));
  folder.add(material, "alphaHash").onChange(needsUpdate(material, geometry));
  folder.add(material, "visible");
  folder.add(material, "side", constants.side).onChange(needsUpdate(material, geometry));
  material.isMeshBasicMaterial && guiMeshBasicMaterial(gui, material, geometry);
  material.isMeshDepthMaterial && guiMeshDepthMaterial(gui, material, geometry);
  material.isMeshLambertMaterial && guiMeshLambertMaterial(gui, material, geometry);
  material.isMeshMatcapMaterial && guiMeshMatcapMaterial(gui, material, geometry);
  material.isMeshNormalMaterial && guiMeshNormalMaterial(gui, material, geometry);
  material.isMeshPhongMaterial && guiMeshPhongMaterial(gui, material, geometry);
  material.isMeshStandardMaterial && !material.isMeshPhysicalMaterial && guiMeshStandardMaterial(gui, material, geometry);
  material.isMeshPhysicalMaterial && guiMeshPhysicalMaterial(gui, material, geometry);
  material.isMeshToonMaterial && guiMeshToonMaterial(gui, material, geometry);
  material.isLineBasicMaterial && guiLineBasicMaterial(gui, material, geometry);
}
function guiMeshBasicMaterial(gui, material, geometry) {
  const folder = gui.addFolder("THREE.MeshBasicMaterial");
  folder.add(material, "wireframe");
  folder.add(material, "vertexColors").onChange(needsUpdate(material, geometry));
  folder.add(material, "fog").onChange(needsUpdate(material, geometry));
  folder.add(material, "combine", constants.combine).onChange(updateCombine(material));
  folder.add(material, "reflectivity", 0, 1);
  folder.add(material, "refractionRatio", 0, 1);
}
function guiMeshDepthMaterial(gui, material) {
  const folder = gui.addFolder("THREE.MeshDepthMaterial");
  folder.add(material, "wireframe");
}
function guiMeshNormalMaterial(gui, material, geometry) {
  const folder = gui.addFolder("THREE.MeshNormalMaterial");
  folder.add(material, "flatShading").onChange(needsUpdate(material, geometry));
  folder.add(material, "wireframe");
}
function guiLineBasicMaterial(gui, material, geometry) {
  const folder = gui.addFolder("THREE.LineBasicMaterial");
  folder.add(material, "linewidth", 0, 10);
  folder.add(material, "linecap", ["butt", "round", "square"]);
  folder.add(material, "linejoin", ["round", "bevel", "miter"]);
  folder.add(material, "vertexColors").onChange(needsUpdate(material, geometry));
  folder.add(material, "fog").onChange(needsUpdate(material, geometry));
}
function guiMeshLambertMaterial(gui, material, geometry) {
  const data = { emissive: material.emissive.getHex() };
  const folder = gui.addFolder("THREE.MeshLambertMaterial");
  folder.addColor(data, "emissive").onChange(handleColorChange(material.emissive));
  folder.add(material, "emissiveIntensity");
  folder.add(material, "wireframe");
  folder.add(material, "vertexColors").onChange(needsUpdate(material, geometry));
  folder.add(material, "fog").onChange(needsUpdate(material, geometry));
  folder.add(material, "combine", constants.combine).onChange(updateCombine(material));
  folder.add(material, "reflectivity", 0, 1);
  folder.add(material, "refractionRatio", 0, 1);
}
function guiMeshMatcapMaterial(gui, material, geometry) {
  const folder = gui.addFolder("THREE.MeshMatcapMaterial");
  folder.add(material, "flatShading").onChange(needsUpdate(material, geometry));
}
function guiMeshPhongMaterial(gui, material, geometry) {
  const data = {
    emissive: material.emissive.getHex(),
    specular: material.specular.getHex()
  };
  const folder = gui.addFolder("THREE.MeshPhongMaterial");
  folder.addColor(data, "emissive").onChange(handleColorChange(material.emissive));
  folder.add(material, "emissiveIntensity");
  folder.addColor(data, "specular").onChange(handleColorChange(material.specular));
  folder.add(material, "shininess", 0, 100);
  folder.add(material, "flatShading").onChange(needsUpdate(material, geometry));
  folder.add(material, "wireframe");
  folder.add(material, "vertexColors").onChange(needsUpdate(material, geometry));
  folder.add(material, "fog").onChange(needsUpdate(material, geometry));
  folder.add(material, "combine", constants.combine).onChange(updateCombine(material));
  folder.add(material, "reflectivity", 0, 1);
  folder.add(material, "refractionRatio", 0, 1);
}
function guiMeshToonMaterial(gui, material) {
  // const folder = gui.addFolder("THREE.MeshToonMaterial");
  console.info(gui, material, "no more debug");
}
function guiMeshStandardMaterial(gui, material, geometry) {
  const data = { emissive: material.emissive.getHex() };
  const folder = gui.addFolder("THREE.MeshStandardMaterial");
  folder.addColor(data, "emissive").onChange(handleColorChange(material.emissive));
  folder.add(material, "emissiveIntensity");
  folder.add(material, "roughness", 0, 1);
  folder.add(material, "metalness", 0, 1);
  folder.add(material, "flatShading").onChange(needsUpdate(material, geometry));
  folder.add(material, "wireframe");
  folder.add(material, "vertexColors").onChange(needsUpdate(material, geometry));
  folder.add(material, "fog").onChange(needsUpdate(material, geometry));
  guiMaps(material, geometry, folder);
}
function guiMeshPhysicalMaterial(gui, material, geometry) {
  const data = {
    emissive: material.emissive.getHex(),
    sheenColor: material.sheenColor.getHex(),
    specularColor: material.specularColor.getHex()
  };
  const folder = gui.addFolder("THREE.MeshPhysicalMaterial");
  folder.addColor(data, "emissive").onChange(handleColorChange(material.emissive));
  folder.add(material, "emissiveIntensity");
  folder.add(material, "roughness", 0, 1);
  folder.add(material, "metalness", 0, 1);
  folder.add(material, "ior", 1, 2.333);
  folder.add(material, "reflectivity", 0, 1);
  folder.add(material, "iridescence", 0, 1);
  folder.add(material, "iridescenceIOR", 1, 2.333);
  folder.add(material, "sheen", 0, 1);
  folder.add(material, "sheenRoughness", 0, 1);
  folder.addColor(data, "sheenColor").onChange(handleColorChange(material.sheenColor));
  folder.add(material, "clearcoat", 0, 1).step(0.01);
  folder.add(material, "clearcoatRoughness", 0, 1).step(0.01);
  folder.add(material, "specularIntensity", 0, 1);
  folder.addColor(data, "specularColor").onChange(handleColorChange(material.specularColor));
  folder.add(material, "transmission", 0, 1);
  folder.add(material, "flatShading").onChange(needsUpdate(material, geometry));
  folder.add(material, "wireframe");
  folder.add(material, "vertexColors").onChange(needsUpdate(material, geometry));
  folder.add(material, "fog").onChange(needsUpdate(material, geometry));
  guiMaps(material, geometry, folder);
}
function guiMaps(material, geometry, folder) {
  const keysMap = [
    { key: "map", hasScale: false },
    { key: "normalMap", hasScale: false },
    { key: "emissiveMap", hasScale: false },
    { key: "metalnessMap", hasScale: false },
    { key: "roughnessMap", hasScale: false },
    { key: "alphaMap", hasScale: false },
    { key: "transmissionMap", hasScale: false },
    { key: "bumpMap", hasScale: true },
    { key: "displacementMap", hasScale: true },
    { key: "envMap", hasScale: false }
  ];
  keysMap.forEach(v => {
    const { key, hasScale } = v;
    if (material[key]) {
      const getMap = () => material[key] || material[`_${key}`];
      folder.add({ visible: true }, "visible").name(`${key}-visible`).onChange(v => {
        if (v) {
          if (material[`_${key}`]) material[key] = material[`_${key}`];
        } else {
          material[`_${key}`] = material[key];
          material[key] = null;
        }
        needsUpdate(material, geometry, getMap())();
      });
      if (hasScale) {
        const keyScale = `${key.replace(/Map/ig, "")}Scale`;
        folder.add(material, keyScale).onChange(v => {
          material[keyScale] = v;
          needsUpdate(material, geometry, getMap())();
        });
      }
      if (key === "normalMap") {
        folder.add(material.normalScale, "x").name("normalScale.x").onChange(v => {
          material.normalScale.x = v;
          needsUpdate(material, geometry, getMap())();
        });
        folder.add(material.normalScale, "y").name("normalScale.y").onChange(v => {
          material.normalScale.y = v;
          needsUpdate(material, geometry, getMap())();
        });
      }
      key === "envMap" && folder.add(material, "envMapIntensity", 0, 1).name("envMapIntensity").onChange(() => needsUpdate(material, geometry, material.envMap)());
    }
  });
}
function handleColorChange(color) {
  return function (value) {
    if (typeof value === "string") {
      value = value.replace("#", "0x");
    }
    color.setHex(value);
  };
}
function needsUpdate(material, geometry, map) {
  return function () {
    material.side = parseInt(material.side); //Ensure number
    map && (map.needsUpdate = true);
    material.needsUpdate = true;
    geometry.attributes.position && (geometry.attributes.position.needsUpdate = true);
    geometry.attributes.normal && (geometry.attributes.normal.needsUpdate = true);
    geometry.attributes.color && (geometry.attributes.color.needsUpdate = true);
  };
}
function updateCombine(material) {
  return function (combine) {
    material.combine = parseInt(combine);
    material.needsUpdate = true;
  };
}
function updateTexture(material, materialKey, textures) {
  return function (key) {
    material[materialKey] = textures[key];
    material.needsUpdate = true;
  };
}
export {
  guiMaterial,
  guiMeshBasicMaterial,
  guiMeshDepthMaterial,
  guiMeshNormalMaterial,
  guiLineBasicMaterial,
  guiMeshLambertMaterial,
  guiMeshMatcapMaterial,
  guiMeshPhongMaterial,
  guiMeshToonMaterial,
  guiMeshStandardMaterial,
  guiMeshPhysicalMaterial,
  // helper
  handleColorChange,
  updateTexture
};
