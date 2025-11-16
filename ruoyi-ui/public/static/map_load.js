let bmapcfg = {
  // 瓦片图的后缀-一般不用改
  imgext: '.png',
  // 瓦片图的地址，基地址路径（默认名字）
  tiles_dir: '/roadmap',
  // 瓦片图地址-尽量使用127或者localhost-方便断网测试
  tiles_path: 'http://112.98.110.101:3000',
  // tiles_path: 'http://10.63.228.90:3000',
  // 卫星瓦片图的地址-可为空
  tiles_hybrid: '',
  //自定义图层的地址-可为空
  tiles_self: ''
}

// 下面可以保持不动
var scripts = document.getElementsByTagName('script')
var JS__FILE__ = scripts[scripts.length - 1].getAttribute('src') //获得当前js文件路径
bmapcfg.home = JS__FILE__.substr(0, JS__FILE__.lastIndexOf('/') + 1) //地图API主目录
  ; (function () {
    window.BMap_loadScriptTime = new Date().getTime()
    //加载地图API主文件
    document.write(
      '<script type="text/javascript" src="' +
      bmapcfg.home +
      'bmap_offline_api_v3.0_min.js"></script>'
    )
  })()
