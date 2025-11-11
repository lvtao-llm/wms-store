import { BufferGeometry, Float32BufferAttribute, Shape, ShapeUtils, Vector2 } from "three";

export default class ShapeUvGeometry extends BufferGeometry {
  constructor(shapes, curveSegments = 12) {
    super();
    this.type = "ShapeUvGeometry";
    if (!shapes) shapes = new Shape([new Vector2(0, 0.5), new Vector2(-0.5, -0.5), new Vector2(0.5, -0.5)]);
    this.parameters = {
      shapes: shapes,
      curveSegments: curveSegments
    };
    // buffers
    const indices = [];
    const vertices = [];
    const normals = [];
    const uvs = [];
    // helper variables
    let groupStart = 0;
    let groupCount = 0;
    // allow single and array values for "shapes" parameter
    !Array.isArray(shapes) && (shapes = [shapes]);
    const shapeVertices = shapes.map(shape => shape.getPoints(curveSegments)).flat(1);
    // 计算矩形包围盒
    const max = new Vector2(
      Math.max(...shapeVertices.map(v => v.x)),
      Math.max(...shapeVertices.map(v => v.y))
    );
    const min = new Vector2(
      Math.min(...shapeVertices.map(v => v.x)),
      Math.min(...shapeVertices.map(v => v.y))
    );
    this.range = max.clone().subVectors(max, min);
    for (let i = 0; i < shapes.length; i++) {
      addShape(shapes[i], this.range);
      this.addGroup(groupStart, groupCount, i); // enables MultiMaterial support
      groupStart += groupCount;
      groupCount = 0;
    }
    // build geometry
    this.setIndex(indices);
    this.setAttribute("position", new Float32BufferAttribute(vertices, 3));
    this.setAttribute("normal", new Float32BufferAttribute(normals, 3));
    this.setAttribute("uv", new Float32BufferAttribute(uvs, 2));
    // helper functions
    function addShape(shape, range) {
      const indexOffset = vertices.length / 3;
      const points = shape.extractPoints(curveSegments);
      let shapeVertices = points.shape;
      const shapeHoles = points.holes;
      // check direction of vertices
      if (ShapeUtils.isClockWise(shapeVertices) === false) {
        shapeVertices = shapeVertices.reverse();
      }
      for (let i = 0, l = shapeHoles.length; i < l; i++) {
        const shapeHole = shapeHoles[i];
        if (ShapeUtils.isClockWise(shapeHole) === true) {
          shapeHoles[i] = shapeHole.reverse();
        }
      }
      const faces = ShapeUtils.triangulateShape(shapeVertices, shapeHoles);
      // join vertices of inner and outer paths to a single array
      for (let i = 0, l = shapeHoles.length; i < l; i++) {
        const shapeHole = shapeHoles[i];
        shapeVertices = shapeVertices.concat(shapeHole);
      }
      // vertices, normals, uvs
      for (let i = 0, l = shapeVertices.length; i < l; i++) {
        const vertex = shapeVertices[i];
        vertices.push(vertex.x, vertex.y, 0);
        normals.push(0, 0, 1);
        uvs.push(
          (vertex.x - min.x) / range.x,
          (vertex.y - min.y) / range.y
        ); // world uvs
      }
      // incides
      for (let i = 0, l = faces.length; i < l; i++) {
        const face = faces[i];
        const a = face[0] + indexOffset;
        const b = face[1] + indexOffset;
        const c = face[2] + indexOffset;
        indices.push(a, b, c);
        groupCount += 3;
      }
    }
  }
}