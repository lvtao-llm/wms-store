import proj4 from "proj4";

proj4.defs("EPSG:4551", "+proj=tmerc +lat_0=0 +lon_0=126 +k=1 +x_0=500000 +y_0=0 +ellps=GRS80 +units=m +no_defs +type=crs");
const WGS84 = "EPSG:4326";
const WebMercator = "EPSG:4551";

const ratioZoom = 1;
/**
 * 经纬度(WGS84)->墨卡托坐标(web mercator)
 * @param lngLat
 * @param offset
 * @returns []
 */
function lngLatToCoords(lngLat = [0, 0], offset = [0, 0]) {
  lngLat = [lngLat[0], lngLat[1]];
  if ((lngLat[0] === 180 || lngLat[0] === -180)) {
    if (lngLat[1] === 90) lngLat[1] = 86;
    else if (lngLat[1] === -90) lngLat[1] = -86;
  }
  lngLat[1] *= -1;
  return proj4(WGS84, WebMercator, lngLat).map((v, i) => v * ratioZoom + offset[i]);
}
/**
 * 墨卡托坐标(web mercator)->经纬度(WGS84)
 * @param coords
 * @param offset
 * @returns []
 */
function coordsToLnglat(coords = [0, 0], offset = [0, 0]) {
  return proj4(WebMercator, WGS84, coords.map((v, i) => (v - offset[i]) / ratioZoom * (i === 1 ? -1 : 1)));
}
// 测试代码
// const offset = [-12105301.23129848, 4085956.4181389506].map(v => v * ratioZoom);
// const offset = [0, 0];
// const testLnglat = [125.0421233, 46.5829954];
// console.info("testLnglat", testLnglat.toString());
// const testCoords = lngLatToCoords(testLnglat);
// console.info("testLnglat -> testCoords", testCoords.toString());
// console.info("testCoords -> testLnglat", coordsToLnglat(testCoords, offset));
/**
 * geoJSON 经纬度(WGS84)->墨卡托坐标(web mercator)
 * @param geoJSON
 * @param offset
 * @returns geoJSON
 */
function transformGeoJSON(geoJSON, offset = [0, 0]) {
  geoJSON = JSON.parse(JSON.stringify(geoJSON));
  geoJSON.features.map(feature => {
    let coordinates = [];
    if (feature.geometry.type === "MultiPolygon") coordinates = feature.geometry.coordinates;
    else if (feature.geometry.type === "Polygon") coordinates = [feature.geometry.coordinates];
    feature.geometry.coordinates = [];
    coordinates.forEach((coordinate, ci) => feature.geometry.coordinates[ci] = coordinate.map(cd => cd.map(c => lngLatToCoords(c, offset)).filter(c => !isNaN(c[0]) && !isNaN(c[1]))));
    return feature;
  });
  return geoJSON;
}
window.coordsToLnglat = coordsToLnglat;
window.lngLatToCoords = lngLatToCoords;
export {
  ratioZoom,
  lngLatToCoords,
  coordsToLnglat,
  transformGeoJSON
};
