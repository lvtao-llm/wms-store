import { QtPlugin } from "./QtPlugin";
import { QtEvents } from "../events/QtEvents";
import { Euler, Matrix4, Quaternion, Vector3 } from "three";
import { World } from "oimo";
import { addCss, getElementBody, getElementMaxZIndex } from "@vci/helper/src/element";
import { radToDeg } from "three/src/math/MathUtils";

class PluginPhysicsOimo extends QtPlugin {
  static namespace = "oimo";
  static Events = {
    AmmoReady: QtEvents.AmmoReady = "AmmoReady"
  };

  async init() {
    super.init();
    this.world = new World({
      timestep: 1 / 60,
      iterations: 8,
      broadphase: 2, // 1 brute force, 2 sweep and prune, 3 volume tree
      worldscale: 1, // scale full world
      random: true,  // randomize sample
      info: this.qt.debug,   // calculate statistic or not
      gravity: [0, -9.8, 0]
    });
    this.elStatus = document.createElement("div");
    addCss(this.elStatus, {
      position: "fixed",
      right: "12px",
      bottom: "64px",
      zIndex: getElementMaxZIndex(),
      fontSize: "12px",
      textShadow: "0 0 1px #000, 0 0 1px #000, 0 0 1px #000, 0 0 1px #000",
      color: "#fff"
    });
    getElementBody().appendChild(this.elStatus);
    this._euler = new Euler();
    this._matrix4 = new Matrix4();
    this._vec3 = new Vector3();
    this._quaternion = new Quaternion();
    this.meshes = [];
    this.bodies = new WeakMap();
    this.things = new WeakMap();
    this.world.postLoop = this.update.bind(this);
    this.world.play();
  }

  addThing(thing, isStatic = false, friction = 0.2) {
    const { meshes, bodies, things } = this;
    const size = thing.getSize();
    const position = thing.object.position;
    const quaternion = thing.object.quaternion;
    const body = this.createBody(size, position, quaternion, isStatic, friction);
    meshes.push(thing.object);
    bodies.set(thing.object, body);
    things.set(thing.object, thing);
    // thing.phy = { body };
    return body;
  }

  removeThing(thing) {
    const { meshes, bodies, things, world } = this;
    const o3 = thing.object;
    things.delete(o3);
    meshes.splice(meshes.indexOf(o3), 1);
    world.removeRigidBody(bodies.get(o3));
    bodies.delete(o3);
  }

  addThingInstancedMesh(thingInstancedMesh, size = new Vector3(1, 1, 1), isStatic = false, friction = 0.2) {
    const { meshes, bodies, things, _matrix4, _vec3, _quaternion, _euler } = this;
    const instancedMesh = thingInstancedMesh.object;
    const _bodies = [];
    for (let i = 0; i < instancedMesh.count; i++) {
      instancedMesh.getMatrixAt(i, _matrix4);
      const position = _vec3.setFromMatrixPosition(_matrix4);
      const quaternion = _quaternion.setFromEuler(_euler.setFromRotationMatrix(_matrix4));
      const body = this.createBody(size, position, quaternion, isStatic, friction);
      _bodies.push(body);
    }
    meshes.push(thingInstancedMesh.object);
    bodies.set(thingInstancedMesh.object, _bodies);
    things.set(thingInstancedMesh.object, thingInstancedMesh);
    // thingInstancedMesh.phy = { body: _bodies };
    return _bodies;
  }

  createBody(size, position, quaternion, isStatic = false, friction = 0.2) {
    const { world, _euler } = this;
    _euler.setFromQuaternion(quaternion);
    return world.add({
      size: size.toArray(), // size of shape
      pos: position.toArray(), // start position in degree
      rot: _euler.toArray().slice(0, 3).map(v => radToDeg(v)), // start rotation in degree
      move: !isStatic, // dynamic or statique
      density: 1,
      friction,
      restitution: 0.2,
      belongsTo: 1, // The bits of the collision groups to which the shape belongs.
      collidesWith: 0xffffffff // The bits of the collision groups with which the shape collides.
    });
  }

  syncMatrixForMesh(mesh, body) {
    mesh.position.copy(body.getPosition());
    mesh.quaternion.copy(body.getQuaternion());
  }

  syncMatrixForInstanceMesh(thingInstancedMesh, body, index) {
    const instanceMesh = thingInstancedMesh.object;
    const { _matrix4 } = this;
    const position = body.getPosition();
    const quaternion = body.getQuaternion();
    instanceMesh.getMatrixAt(index, _matrix4);
    _matrix4.setPosition(position.x, position.y, position.z);
    instanceMesh.setMatrixAt(index, _matrix4);
    _matrix4.makeRotationFromQuaternion(quaternion);
    thingInstancedMesh.dispatchEvent("phy-update-im", {
      index,
      body
    });
  }

  update() {
    const { world, meshes, bodies, things, elStatus } = this;
    meshes.forEach(mesh => {
      const body = bodies.get(mesh);
      const thing = things.get(mesh);
      if (mesh.isInstancedMesh) {
        body.forEach((body, i) => this.syncMatrixForInstanceMesh(thing, body, i));
        mesh.instanceMatrix.needsUpdate = true;
      } else this.syncMatrixForMesh(mesh, body);
      thing.dispatchEvent("phy-update", { thing, body });
    });
    elStatus.innerHTML = world.getInfo();
  }

  destroy() {
    super.destroy();
    this.world.clear();
    getElementBody().removeChild(this.elStatus);
  }
}

export { PluginPhysicsOimo };
