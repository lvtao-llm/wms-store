import { QtPlugin } from "./QtPlugin";
import { QtEvents } from "../events/QtEvents";
import { resourceRootAliyun } from "../constants";
import { getElementBody } from "@vci/helper/src/element";
import { Euler, Matrix4, Quaternion, Vector3 } from "three";

class PluginPhysicsAmmo extends QtPlugin {
  static namespace = "ammo";
  static Events = {
    AmmoReady: QtEvents.AmmoReady = "AmmoReady"
  };

  async init() {
    super.init();
    const { qt } = this;
    const elScript = document.createElement("script");
    elScript.setAttribute("src", `${resourceRootAliyun}/libs/ammo@0.0.2/ammo.js`);
    getElementBody().appendChild(elScript);
    await new Promise(resolve => elScript.onload = resolve);
    this.inter = -1;
    this.lastTime = 0;
    const ammo = this.ammo = await window["Ammo"]({ locateFile: () => `${resourceRootAliyun}/libs/ammo@0.0.2/ammo.wasm.wasm` });
    const collisionConfiguration = new ammo.btDefaultCollisionConfiguration();
    const dispatcher = new ammo.btCollisionDispatcher(collisionConfiguration);
    const broadphase = new ammo.btDbvtBroadphase();
    const solver = new ammo.btSequentialImpulseConstraintSolver();
    const world = this.world = new ammo.btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
    world.setGravity(new ammo.btVector3(0, -9.8, 0));
    this._btTransform = new ammo.btTransform();
    this._matrix4 = new Matrix4();
    this._vec3 = new Vector3();
    this._quaternion = new Quaternion();
    this._euler = new Euler();
    this.meshes = [];
    this.bodies = new WeakMap();
    this.things = new WeakMap();
    qt.dispatchEvent(PluginPhysicsAmmo.Events.AmmoReady, { ammo });
    this.inter = setInterval(this.update.bind(this), 1000 / 60);
  }

  getShape(size = new Vector3(1, 1, 1)) {
    const { ammo } = this;
    const shape = new ammo.btBoxShape(new ammo.btVector3(size.x / 2, size.y / 2, size.z / 2));
    shape.setMargin(0.05);
    return shape;
  }

  addThing(thing, mass = 1, friction = 1) {
    const { meshes, bodies, things } = this;
    const size = thing.getSize();
    const position = thing.object.position;
    const quaternion = thing.object.quaternion;
    const body = this.createBody(size, position, quaternion, mass, friction);
    if (mass > 0) {
      meshes.push(thing.object);
      bodies.set(thing.object, body);
      things.set(thing.object, thing);
      // thing.phy = { body };
    }
    return body;
  }

  addThingInstancedMesh(thingInstancedMesh, size = new Vector3(1, 1, 1), mass = 1, friction = 1) {
    const { meshes, bodies, things, _matrix4, _vec3, _quaternion, _euler } = this;
    const instancedMesh = thingInstancedMesh.object;
    const _bodies = [];
    for (let i = 0; i < instancedMesh.count; i++) {
      instancedMesh.getMatrixAt(i, _matrix4);
      const position = _vec3.setFromMatrixPosition(_matrix4);
      const quaternion = _quaternion.setFromEuler(_euler.setFromRotationMatrix(_matrix4));
      const body = this.createBody(size, position, quaternion, mass, friction);
      _bodies.push(body);
    }
    meshes.push(thingInstancedMesh.object);
    bodies.set(thingInstancedMesh.object, _bodies);
    things.set(thingInstancedMesh.object, thingInstancedMesh);
    // thingInstancedMesh.phy = { body: _bodies };
    return _bodies;
  }

  createBody(size, position, quaternion, mass = 1, friction = 1) {
    const { ammo, world } = this;
    const shape = this.getShape(size);
    const transform = new ammo.btTransform();
    transform.setIdentity();
    transform.setOrigin(new ammo.btVector3(position.x, position.y, position.z));
    transform.setRotation(new ammo.btQuaternion(quaternion.x, quaternion.y, quaternion.z, quaternion.w));
    const motionState = new ammo.btDefaultMotionState(transform);
    const localInertia = new ammo.btVector3(0, 0, 0);
    shape.calculateLocalInertia(mass, localInertia);
    const rbInfo = new ammo.btRigidBodyConstructionInfo(mass, motionState, shape, localInertia);
    const body = new ammo.btRigidBody(rbInfo);
    body.setFriction(friction);
    world.addRigidBody(body);
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

  syncMatrixForMesh(mesh, body) {
    const { _btTransform } = this;
    const motionState = body.getMotionState();
    motionState.getWorldTransform(_btTransform);
    const position = _btTransform.getOrigin();
    const quaternion = _btTransform.getRotation();
    mesh.position.set(position.x(), position.y(), position.z());
    mesh.quaternion.set(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
  }

  syncMatrixForInstanceMesh(thingInstancedMesh, body, index) {
    const instanceMesh = thingInstancedMesh.object;
    const { ammo, _btTransform, _matrix4, _quaternion } = this;
    const motionState = body.getMotionState();
    motionState.getWorldTransform(_btTransform);
    const position = _btTransform.getOrigin();
    const quaternion = _btTransform.getRotation();
    instanceMesh.getMatrixAt(index, _matrix4);
    _matrix4.setPosition(position.x(), position.y(), position.z());
    _quaternion.set(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
    _matrix4.makeRotationFromQuaternion(_quaternion);
    instanceMesh.setMatrixAt(index, _matrix4);
    instanceMesh.instanceMatrix.needsUpdate = true;
    thingInstancedMesh.dispatchEvent("phy-update-im", {
      ammo,
      index,
      body
    });
  }

  update() {
    const { meshes, bodies, world, things, ammo } = this;
    const time = (typeof performance === "undefined" ? Date : performance).now();
    if (this.lastTime > 0) {
      const delta = (time - this.lastTime) / 1000;
      world.stepSimulation(delta, 10);
      meshes.forEach(mesh => {
        const body = bodies.get(mesh);
        const thing = things.get(mesh);
        if (mesh.isInstancedMesh) body.forEach((body, i) => this.syncMatrixForInstanceMesh(thing, body, i));
        else this.syncMatrixForMesh(mesh, body);
        thing.dispatchEvent("phy-update", {
          ammo,
          thing,
          body
        });
      });
    }
    this.lastTime = time;
  }

  destroy() {
    super.destroy();
    clearInterval(this.inter);
  }
}

export { PluginPhysicsAmmo };
