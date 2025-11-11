import MaterialPatch from "./MaterialPatch";
import { release } from "@vci/helper/src/object";

export default class O3DisposerHelper {
  /**
   * 销毁几何体的材质
   * @param material        材质
   * @param onlyDisposeMap  是否只释放材质贴图
   * @param enableDisposeImage  是否只释放材质贴图对应的image
   */
  static DisposeMaterial(material, onlyDisposeMap = false, enableDisposeImage = false) {
    Object.keys(material).forEach(mk => {
      if (material[mk] && material[mk].isTexture) {
        const texture = material[mk];
        if (texture.image && enableDisposeImage) {
          if (texture.image instanceof ImageBitmap) texture.image.close();
          else {
            texture.image.remove();
            URL.revokeObjectURL(texture.image.src);
            texture.image.src = "";
          }
        }
        texture.dispose && texture.dispose();
      }
    });
    !onlyDisposeMap && material.dispose();
  }

  /**
   * 销毁几何体的材质
   * @param o3      Object3D实例
   */
  static DisposeMaterialOfO3(o3) {
    MaterialPatch.TraverseMaterials(o3, material => O3DisposerHelper.DisposeMaterial(material));
  }

  /**
   * 销毁几何体
   * @param o3    Object3D实例
   */
  static DisposeObject3DHandle(o3) {
    o3.material && O3DisposerHelper.DisposeMaterialOfO3(o3);
    o3.geometry && o3.geometry.dispose();
  }

  /**
   * 销毁某个几何体下的所有子实例
   * @param o3s         集合体实例数组
   * @param parentO3    父级几何体
   * @constructor
   */
  static DisposeObject3D(o3s = [], parentO3) {
    const removes = [];
    !Array.isArray(o3s) && (o3s = [o3s]);
    for (let i = 0; i < o3s.length; i++) {
      const o3 = o3s[i];
      if (o3.children && o3.children.length > 0) O3DisposerHelper.DisposeObject3D(o3.children, o3);
      O3DisposerHelper.DisposeObject3DHandle(o3);
      removes.push(o3);
    }
    removes.forEach(ro => {
      parentO3.remove(ro);
      release(ro);
    });
    removes.length = 0;
  }
}
