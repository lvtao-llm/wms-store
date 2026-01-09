import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
 noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
 title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
 icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
 breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
 activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
 }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' }
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/monitor/job/log'),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/monitor/job' }
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/tool/gen/editTable'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
      }
    ]
  },
  {
    path: '/lanya-core-area',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/lanya_core_area/index'),
        name: '区域管理',
        meta: { title: '区域管理' }
      }
    ]
  },
  {
    path: '/person',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/wms_person/index'),
        name: '员工档案',
        meta: { title: '员工档案' }
      }
    ]
  },
  {
    path: '/lanya-vehicleInfo-visitor',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/lanya_vehicleInfo_visitor/index'),
        name: '访客车辆',
        meta: { title: '访客车辆' }
      }
    ]
  },
  {
    path: '/gatepass',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/wms_gatepass/index'),
        name: '车辆预约',
        meta: { title: '车辆预约' }
      }
    ]
  },
  {
    path: '/route-set',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/wms_route_set/index'),
        name: '路线规划',
        meta: { title: '路线规划' }
      }
    ]
  },
  {
    path: '/lanya-core-visitor',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/lanya_core_visitor/index'),
        name: '访客信息',
        meta: { title: '访客信息' }
      }
    ]
  },
  // {
  //   path: '/alarm',
  //   component: Layout,
  //   hidden: true,
  //   permissions: ['system:role:edit'],
  //   children: [
  //     {
  //       path: 'core',
  //       component: () => import('@/views/lanya_core_alarm/index'),
  //       name: '核心报警记录',
  //       meta: { title: '核心报警记录' }
  //     },
  //     {
  //       path: 'vehicle',
  //       component: () => import('@/views/lanya_vehicle_alarm/index'),
  //       name: '车辆报警记录',
  //       meta: { title: '车辆报警记录' }
  //     },
  //     {
  //       path: 'people',
  //       component: () => import('@/views/lanya_core_alarm_people/index'),
  //       name: '人员报警记录',
  //       meta: { title: '人员报警记录' }
  //     }
  //   ]
  // },
  {
    path: '/card-log',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'people',
        component: () => import('@/views/lanya_device_card_sender_log/index'),
        name: '人员发卡记录',
        meta: { title: '人员发卡记录' }
      },
      {
        path: 'vehicle',
        component: () => import('@/views/lanya_device_card_sender_vehicle_log/index'),
        name: '车辆发卡记录',
        meta: { title: '车辆发卡记录' }
      }
    ]
  },
  {
    path: '/material-area-manage',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'inbound',
        component: () => import('@/views/wms_material_in/index'),
        name: '入库档案',
        meta: { title: '入库档案' }
      },
      {
        path: 'outbound',
        component: () => import('@/views/wms_material_out/index'),
        name: '出库档案',
        meta: { title: '出库档案' }
      },
      {
        path: 'inventory',
        component: () => import('@/views/wms_material_stock/index'),
        name: '库存档案',
        meta: { title: '库存档案' }
      },
      {
        path: 'material-desc',
        component: () => import('@/views/wms_material_desc/index'),
        name: '物料档案',
        meta: { title: '物料档案' }
      },
      {
        path: 'material-identify-record',
        component: () => import('@/views/wms_material_identify_record/index'),
        name: '物料识别记录',
        meta: { title: '物料识别记录' }
      }
    ]
  },
  {
    path: '/trajectory',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/lanya_position_history/index'),
        name: '车辆轨迹',
        meta: { title: '轨迹' }
      }
    ]
  },
  {
    path: '/lanya-core-person',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/lanya_core_person/index'),
        name: '人员轨迹',
        meta: { title: '轨迹' }
      }
    ]
  },
  {
    path: '/activity-tracking',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'activity-manage',
        component: () => import('@/views/activity_tracking/index'),
        name: '行动轨迹(模型)',
        meta: { title: '行动轨迹(模型)' }
      },
      {
        path: 'activity-manage1',
        component: () => import('@/views/activity_tracking/index1'),
        name: '行动轨迹(地图)',
        meta: { title: '行动轨迹(地图)' }
      },
    ]
  },
  {
    path: '/RealTimeLocation',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'RealTimeLocations',
        component: () => import('@/views/RealTimeLocation/RealTimeLocations/index'),
        name: '人员实时定位',
        meta: { title: '人员实时定位' }
      },
    ]
  },
  {
    path: '/device-manage',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'card-manage',
        component: () => import('@/views/lanya_device_card/index'),
        name: '定位卡管理',
        meta: { title: '定位卡管理' }
      },
      {
        path: 'machine-manage',
        component: () => import('@/views/lanya_device_machine/index'),
        name: '发卡机管理',
        meta: { title: '发卡机管理' }
      },
      {
        path: 'beacon',
        component: () => import('@/views/wms_device_beacon/index'),
        name: '信标管理',
        meta: { title: '信标管理' }
      },
      {
        path: 'plate-recognition',
        component: () => import('@/views/wms_device_plate/index'),
        name: '车牌识别管理',
        meta: { title: '车牌识别管理' }
      },
      {
        path: 'diff-gps-station',
        component: () => import('@/views/wms_device_refer/index'),
        name: '差分基站管理',
        meta: { title: '差分基站管理' }
      },
      {
        path: 'camera',
        component: () => import('@/views/wms_device_camera/index'),
        name: '摄像头管理',
        meta: { title: '摄像头管理' }
      },
      {
        path: 'send-content',
        component: () => import('@/views/wms_card_content_send/index'),
        name: '发送内容给卡播报',
        meta: { title: '发送内容给卡播报' }
      },
      {
        path: 'sos-alarm-log',
        component: () => import('@/views/lanya_core_alarm/index'),
        name: 'sos报警记录',
        meta: { title: 'sos报警记录' }
      },
      {
        path: 'SensorManagement',
        component: () => import('@/views/sensorManagement/index'),
        name: '传感器管理',
        meta: { title: '传感器管理' }
      }
    ]
  },
  {
    path: '/people-manage',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'internal-employee',
        component: () => import('@/views/lanya_internal_employee/index'),
        name: '内部员工',
        meta: { title: '内部员工' }
      },
      {
        path: 'person-in-out-record',
        component: () => import('@/views/lanya_person_in_out_record/index'),
        name: '人员进出列表',
        meta: { title: '人员进出列表' }
      }
    ]
  },
  {
    path: '/core-area',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'area-draw',
        component: () => import('@/views/wms_area/index'),
        name: '区域绘制',
        meta: { title: '区域绘制' }
      },
      {
        path: 'area-material-manage',
        component: () => import('@/views/wms_area_material_manage/index'),
        name: '物料区域管理',
        meta: { title: '物料区域管理' }
      },
      {
        path: 'area-360',
        component: () => import('@/views/wms_area_360/index'),
        name: '360全景',
        meta: { title: '360全景' }
      },
      // {
      //   path: 'person-in-out-record',
      //   component: () => import('@/views/lanya_person_in_out_record/index'),
      //   name: '人员进出记录',
      //   meta: { title: '人员进出记录' }
      // },
    ]
  },
  {
    path: '/alarm-manage',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'people-rule/:type',
        component: () => import('@/views/wms_alarm_rule/index'),
        name: '人员报警规则',
        meta: { title: '人员报警规则' }
      },
      {
        path: 'people-log/:type',
        component: () => import('@/views/wms_alarm_log/index'),
        name: '人员报警记录',
        meta: { title: '人员报警记录' }
      }
    ]
  },
  {
    path: '/inspection',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'rule',
        component: () => import('@/views/wms_inspection_rule/index'),
        name: '巡检规则管理',
        meta: { title: '巡检规则管理' }
      },
      {
        path: 'task',
        component: () => import('@/views/wms_inspection_task/index'),
        name: '巡检任务管理',
        meta: { title: '巡检任务管理' }
      },
      {
        path: 'issue',
        component: () => import('@/views/wms_inspection_issue/index'),
        name: '巡检问题管理',
        meta: { title: '巡检问题管理' }
      }
    ]
  },
  {
    path: '/vehicle-auth-management',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'appointment',
        component: () => import('@/views/wms_vehicle_appointment/index'),
        name: '车辆预约列表',
        meta: { title: '车辆预约列表' }
      },
      {
        path: 'blacklist',
        component: () => import('@/views/wms_vehicle_blacklist/index'),
        name: '车辆黑名单那列表',
        meta: { title: '车辆黑名单那列表' }
      },
      {
        path: 'vechicle-in-out-record',
        component: () => import('@/views/wms_vehicle_record/index'),
        name: '车辆进出记录',
        meta: { title: '车辆进出记录' }
      },
      {
        path: 'vehicle-regconize-log',
        component: () => import('@/views/wms_device_camera_log/index'),
        name: '车辆识别记录',
        meta: { title: '车辆识别记录' }
      },
      {
        path: 'path-definition',
        component: () => import('@/views/wms_paths_definetion/index'),
        name: '车辆路径定义',
        meta: { title: '车辆路径定义' }
      }
    ]
  },
  {
    path: '/reports',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'people',
        component: () => import('@/views/wms_statistics_people/index'),
        name: '人员统计报表',
        meta: { title: '人员统计报表' }
      },
      {
        path: 'vehicle',
        component: () => import('@/views/wms_statistics_vehicle/index'),
        name: '车辆统计报表',
        meta: { title: '车辆统计报表' }
      },
      {
        path: 'vechicle-in-out-record',
        component: () => import('@/views/wms_vehicle_record/index'),
        name: '车辆进出记录报表',
        meta: { title: '车辆进出记录报表' }
      }
    ]
  },
  {
    path: '/log',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'backend-log',
        component: () => import('@/views/wms_system_log/index'),
        name: '后台日志',
        meta: { title: '后台日志' }
      }
    ]
  }
]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push
let routerReplace = Router.prototype.replace
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
