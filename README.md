# 仓储库区生产作业智能管控系统项目文档

## 项目概述

**仓储库区生产作业智能管控系统** 是基于 RuoYi-Vue 框架开发的企业级仓储管理系统，旨在实现对仓储库区的智能化管理和监控。

## 项目基本信息

- **项目名称**: wms-service (仓储库区生产作业智能管控系统)
- **项目版本**: 3.9.0
- **技术栈**: Spring Boot + Vue.js
- **Java 版本**: 1.8
- **框架**: RuoYi-Vue 3.9.0

## 项目架构

### 后端技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| [spring-boot](#spring-boot) | 2.5.15 | 核心框架 |
| [druid](#druid) | 1.2.23 | 数据库连接池 |
| [mybatis](#mybatis) | - | ORM 框架 |
| [swagger](#swagger) | 3.0.0 | API 文档工具 |
| [pagehelper](#pagehelper) | 1.4.7 | 分页插件 |
| [fastjson](#fastjson) | 2.0.58 | JSON 处理 |
| [oshi](#oshi) | 6.8.3 | 系统信息监控 |

### 前端技术栈

- Vue.js 3.x
- Element UI
- Axios

## 项目模块结构

### 核心模块

#### [ruoyi-system](#ruoyi-system) - 系统管理模块
- **包结构**:
    - `com.ruoyi.system.domain` - 实体类定义
    - `com.ruoyi.system.service` - 业务服务接口
    - `com.ruoyi.system.mapper` - 数据访问层
    - `com.ruoyi.system.controller` - 控制器层

#### [ruoyi-ui](#ruoyi-ui) - 前端界面模块
- **目录结构**:
    - `src/views` - 页面组件
    - `src/router` - 路由配置
    - `src/api` - API 接口定义

#### [map](#map) - 监控界面模块
- **监控页面**:
    - `src/views/monitor/server/index.vue` - 服务器监控界面

## 系统功能模块详解

### 1. 物料管理模块

#### [wms_material_desc](#wms_material_desc) - 物料档案管理
- **前端文件**: `@/views/wms_material_desc/index.vue`
- **功能**: 管理物料的基本信息，支持物料分类和属性定义

#### [wms_material_type](#wms_material_type) - 物料类型管理
- **前端文件**: `@/views/wms_material_type/index.vue`
- **功能**: 定义物料分类体系，管理物料层级关系

### 2. 库存管理模块

#### [wms_material_in](#wms_material_in) - 入库档案管理
- **前端文件**: `@/views/wms_material_in/index.vue`
- **功能**: 管理物料入库记录，记录入库批次和数量

#### [wms_material_out](#wms_material_out) - 出库档案管理
- **前端文件**: `@/views/wms_material_out/index.vue`
- **功能**: 管理物料出库记录，控制出库审批流程

#### [wms_material_stock](#wms_material_stock) - 库存档案管理
- **前端文件**: `@/views/wms_material_stock/index.vue`
- **功能**: 实时库存查询，库存预警功能

### 3. 物料识别模块

#### [wms_material_identify_record](#wms_material_identify_record) - 物料识别记录
- **前端文件**: `@/views/wms_material_identify_record/index.vue`
- **功能**: 记录物料识别过程，追踪物料流转轨迹

### 4. 访问控制模块

#### [wms_access_log](#wms_access_log) - 料场进出记录
- **前端文件**: `@/views/wms_access_log/index.vue`
- **功能**: 记录人员进出料场情况，提供访问审计功能

## ruoyi-system模块概述

**ruoyi-system** 是仓储库区生产作业智能管控系统的核心系统管理模块，负责系统的基础设施管理、物料管理、人员管理、车辆管理、报警管理等功能。

## 模块结构

```
ruoyi-system/
├── src/main/java/com/ruoyi/system/
│   ├── camera/           # 摄像头管理
│   ├── controller/       # 控制器层
│   ├── domain/           # 实体类
│   ├── lanya/data/       # 定位卡数据处理
│   ├── mapper/           # 数据访问层
│   ├── service/          # 业务服务层
│   ├── utils/            # 工具类
│   ├── websocket/        # WebSocket服务
│   └── wzgs/sync/        # 数据同步
└── src/main/resources/mapper/system/  # MyBatis映射文件
```


## 核心包结构详解

### 1. [camera](#camera) - 摄像头管理包

#### [CameraController.java](#CameraController-java) - 摄像头控制器
- **功能**: 管理摄像头设备的控制和配置
- **主要方法**: 摄像头设备的增删改查操作

#### [CameraService.java](#CameraService-java) - 摄像头服务
- **功能**: 提供摄像头设备的业务逻辑处理

#### [FFmpegWrap.java](#FFmpegWrap-java) - FFmpeg封装类
- **功能**: 封装FFmpeg视频处理功能

#### [FlvHandler.java](#FlvHandler-java) - FLV视频处理器
- **功能**: 处理FLV格式视频流

#### [MediaServer.java](#MediaServer-java) - 媒体服务器
- **功能**: 提供视频流媒体服务

### 2. [controller](#controller) - 控制器层

#### 物料管理控制器

##### [WmsMaterialController.java](#WmsMaterialController-java) - 物料基础控制器
- **功能**: 管理物料基础信息
- **API前缀**: `/system/material`

##### [WmsMaterialDescController.java](#WmsMaterialDescController-java) - 物料档案控制器
- **功能**: 管理物料档案信息
- **API前缀**: `/system/material-desc`

##### [WmsMaterialTypeController.java](#WmsMaterialTypeController-java) - 物料类型控制器
- **功能**: 管理物料分类体系
- **API前缀**: `/system/wms_material_type`

##### [WmsMaterialInController.java](#WmsMaterialInController-java) - 物料入库控制器
- **功能**: 管理物料入库记录
- **API前缀**: `/system/material-in`

##### [WmsMaterialOutController.java](#WmsMaterialOutController-java) - 物料出库控制器
- **功能**: 管理物料出库记录
- **API前缀**: `/system/material-out`

##### [WmsMaterialStockController.java](#WmsMaterialStockController-java) - 物料库存控制器
- **功能**: 管理物料库存信息
- **API前缀**: `/system/material-stock`

##### [WmsMaterialIdentifyRecordController.java](#WmsMaterialIdentifyRecordController-java) - 物料识别记录控制器
- **功能**: 管理物料识别记录
- **API前缀**: `/system/material-identify-record`

##### [WmsMaterialStaticsDayController.java](#WmsMaterialStaticsDayController-java) - 物料日统计控制器
- **功能**: 管理物料日统计数据
- **API前缀**: `/system/material-statics-day`

#### 人员管理控制器

##### [WmsPersonController.java](#WmsPersonController-java) - 人员信息控制器
- **功能**: 管理人员基础信息
- **API前缀**: `/system/person`

##### [WmsVisitorController.java](#WmsVisitorController-java) - 访客信息控制器
- **功能**: 管理访客信息
- **API前缀**: `/system/visitor`

#### 车辆管理控制器

##### [WmsVehicleController.java](#WmsVehicleController-java) - 车辆信息控制器
- **功能**: 管理车辆基础信息
- **API前缀**: `/system/vehicle`

##### [WmsVehicleBlacklistController.java](#WmsVehicleBlacklistController-java) - 车辆黑名单控制器
- **功能**: 管理车辆黑名单
- **API前缀**: `/system/vehicle-blacklist`

##### [WmsVehicleRecordController.java](#WmsVehicleRecordController-java) - 车辆记录控制器
- **功能**: 管理车辆通行记录
- **API前缀**: `/system/vehicle-record`

##### [WmsVehicleGatepassController.java](#WmsVehicleGatepassController-java) - 车辆通行证控制器
- **功能**: 管理车辆通行证
- **API前缀**: `/system/vehicle-gatepass`

##### [WmsVehicleRouteController.java](#WmsVehicleRouteController-java) - 车辆路线控制器
- **功能**: 管理车辆行驶路线
- **API前缀**: `/system/vehicle-route`

#### 区域管理控制器

##### [WmsAreaController.java](#WmsAreaController-java) - 区域信息控制器
- **功能**: 管理区域信息
- **API前缀**: `/system/area`

##### [WmsArea360Controller.java](#WmsArea360Controller-java) - 360度区域控制器
- **功能**: 管理360度全景区域信息
- **API前缀**: `/system/area-360`

#### 报警管理控制器

##### [WmsAlarmController.java](#WmsAlarmController-java) - 报警信息控制器
- **功能**: 管理报警信息
- **API前缀**: `/system/alarm`

##### [WmsAlarmLogController.java](#WmsAlarmLogController-java) - 报警日志控制器
- **功能**: 管理报警日志记录
- **API前缀**: `/system/alarm-log`

##### [WmsAlarmRuleController.java](#WmsAlarmRuleController-java) - 报警规则控制器
- **功能**: 管理报警规则配置
- **API前缀**: `/system/alarm-rule`

#### 设备管理控制器

##### [WmsDeviceController.java](#WmsDeviceController-java) - 设备信息控制器
- **功能**: 管理设备基础信息
- **API前缀**: `/system/device`

##### [WmsDeviceCameraLogController.java](#WmsDeviceCameraLogController-java) - 设备摄像头日志控制器
- **功能**: 管理设备摄像头日志
- **API前缀**: `/system/device-camera-log`

##### [WmsCardRecordController.java](#WmsCardRecordController-java) - 发卡记录控制器
- **功能**: 管理发卡记录信息
- **API前缀**: `/system/record`

#### 轨迹与定位控制器

##### [WmsTrajectoryController.java](#WmsTrajectoryController-java) - 轨迹控制器
- **功能**: 管理轨迹信息
- **API前缀**: `/system/trajectory`

##### [WmsVehiclePositionHistoryController.java](#WmsVehiclePositionHistoryController-java) - 车辆位置历史控制器
- **功能**: 管理车辆位置历史记录
- **API前缀**: `/system/vehicle-position-history`

#### 巡检管理控制器

##### [WmsInspectionTaskController.java](#WmsInspectionTaskController-java) - 巡检任务控制器
- **功能**: 管理巡检任务
- **API前缀**: `/system/inspection-task`

##### [WmsInspectionRuleController.java](#WmsInspectionRuleController-java) - 巡检规则控制器
- **功能**: 管理巡检规则
- **API前缀**: `/system/inspection-rule`

##### [WmsInspectionIssueController.java](#WmsInspectionIssueController-java) - 巡检问题控制器
- **功能**: 管理巡检发现的问题
- **API前缀**: `/system/inspection-issue`

#### 定位卡相关控制器

##### [LanyaCoreAreaController.java](#LanyaCoreAreaController-java) - 定位卡区域控制器
- **功能**: 管理定位卡区域信息
- **API前缀**: `/system/lanya-area`

##### [LanyaCorePersonController.java](#LanyaCorePersonController-java) - 定位卡人员控制器
- **功能**: 管理定位卡人员信息
- **API前缀**: `/system/lanya-person`

##### [LanyaCoreVisitorController.java](#LanyaCoreVisitorController-java) - 定位卡访客控制器
- **功能**: 管理定位卡访客信息
- **API前缀**: `/system/lanya-visitor`

##### [LanyaCoreAlarmController.java](#LanyaCoreAlarmController-java) - 定位卡报警控制器
- **功能**: 管理定位卡报警信息
- **API前缀**: `/system/lanya-alarm`

##### [LanyaCoreAlarmPeopleController.java](#LanyaCoreAlarmPeopleController-java) - 定位卡人员报警控制器
- **功能**: 管理定位卡人员报警信息
- **API前缀**: `/system/lanya-alarm-people`

##### [LanyaVehicleAlarmController.java](#LanyaVehicleAlarmController-java) - 定位卡车辆报警控制器
- **功能**: 管理定位卡车辆报警信息
- **API前缀**: `/system/lanya-vehicle-alarm`

##### [LanyaPositionCurrentController.java](#LanyaPositionCurrentController-java) - 实时定位控制器
- **功能**: 管理实时定位信息
- **API前缀**: `/system/lanya-position-current`

##### [LanyaPositionHistoryController.java](#LanyaPositionHistoryController-java) - 定位历史控制器
- **功能**: 管理定位历史信息
- **API前缀**: `/system/lanya-position-history`

##### [LanyaDeviceCardController.java](#LanyaDeviceCardController-java) - 定位卡定位卡控制器
- **功能**: 管理定位卡定位卡信息
- **API前缀**: `/system/lanya-device-card`

##### [LanyaDeviceCardMachineController.java](#LanyaDeviceCardMachineController-java) - 定位卡发卡机控制器
- **功能**: 管理定位卡发卡机信息
- **API前缀**: `/system/lanya-device-card-machine`

##### [LanyaDeviceCardSenderLogController.java](#LanyaDeviceCardSenderLogController-java) - 定位卡发卡日志控制器
- **功能**: 管理定位卡发卡日志
- **API前缀**: `/system/lanya-device-card-sender-log`

##### [LanyaDeviceCardSenderVehicleLogController.java](#LanyaDeviceCardSenderVehicleLogController-java) - 定位卡车辆发卡日志控制器
- **功能**: 管理定位卡车辆发卡日志
- **API前缀**: `/system/lanya-device-card-sender-vehicle-log`

##### [LanyaInteralEmployeeController.java](#LanyaInteralEmployeeController-java) - 定位卡内部员工控制器
- **功能**: 管理定位卡内部员工信息
- **API前缀**: `/system/lanya-internal-employee`

##### [LanyaVehicleInfoVisitorController.java](#LanyaVehicleInfoVisitorController-java) - 定位卡访客车辆信息控制器
- **功能**: 管理定位卡访客车辆信息
- **API前缀**: `/system/lanya-vehicle-info-visitor`

##### [LanyaTransferController.java](#LanyaTransferController-java) - 定位卡转移控制器
- **功能**: 管理定位卡设备转移信息
- **API前缀**: `/system/lanya-transfer`

### 3. [domain](#domain) - 实体类包

#### 物料实体类

##### [WmsMaterial.java](#WmsMaterial-java) - 物料实体
- **功能**: 物料基础信息实体
- **主要字段**: 物料编码、物料名称、规格型号、单位等

##### [WmsMaterialDesc.java](#WmsMaterialDesc-java) - 物料档案实体
- **功能**: 物料档案详细信息实体
- **主要字段**: 物料编码、物料名称、规格、型号、类别等

##### [WmsMaterialIn.java](#WmsMaterialIn-java) - 物料入库实体
- **功能**: 物料入库记录实体，继承自[WmsMaterialDesc](#WmsMaterialDesc)
- **主要字段**: 接料编号、到货编号、货票编号、到货类型、库房等

##### [WmsMaterialOut.java](#WmsMaterialOut-java) - 物料出库实体
- **功能**: 物料出库记录实体
- **主要字段**: 出库编号、出库类型、物料信息、数量等

##### [WmsMaterialStock.java](#WmsMaterialStock-java) - 物料库存实体
- **功能**: 物料库存信息实体
- **主要字段**: 物料编码、库存数量、存放位置、批次等

##### [WmsMaterialType.java](#WmsMaterialType-java) - 物料类型实体
- **功能**: 物料分类实体
- **主要字段**: 类型编码、类型名称、父级类型、层级等

##### [WmsMaterialIdentifyRecord.java](#WmsMaterialIdentifyRecord-java) - 物料识别记录实体
- **功能**: 物料识别过程记录实体
- **主要字段**: 识别时间、物料信息、识别设备、识别结果等

##### [WmsMaterialStaticsDay.java](#WmsMaterialStaticsDay-java) - 物料日统计实体
- **功能**: 物料日统计信息实体，继承自[WmsMaterialDesc](#WmsMaterialDesc)
- **主要字段**: 统计日期、物料编码、物料名称、品种名称、物资类别、存放区域、计量单位等

#### 人员实体类

##### [WmsPerson.java](#WmsPerson-java) - 人员实体
- **功能**: 人员基础信息实体
- **主要字段**: 人员编号、姓名、工号、部门、职位等

##### [WmsVisitor.java](#WmsVisitor-java) - 访客实体
- **功能**: 访客信息实体
- **主要字段**: 访客编号、姓名、身份证、联系电话、访问事由等

#### 车辆实体类

##### [WmsVehicle.java](#WmsVehicle-java) - 车辆实体
- **功能**: 车辆基础信息实体
- **主要字段**: 车牌号码、车辆类型、车主信息、联系方式等

##### [WmsVehicleBlacklist.java](#WmsVehicleBlacklist-java) - 车辆黑名单实体
- **功能**: 车辆黑名单实体
- **主要字段**: 车牌号码、拉黑原因、拉黑时间、处理状态等

##### [WmsVehicleRecord.java](#WmsVehicleRecord-java) - 车辆记录实体
- **功能**: 车辆通行记录实体
- **主要字段**: 车牌号码、通行时间、通行地点、通行方向等

##### [WmsVehicleGatepass.java](#WmsVehicleGatepass-java) - 车辆通行证实体
- **功能**: 车辆通行证实体
- **主要字段**: 通行证编号、车牌号码、有效期限、发证时间等

##### [WmsVehicleRoute.java](#WmsVehicleRoute-java) - 车辆路线实体
- **功能**: 车辆行驶路线实体
- **主要字段**: 路线编号、路线名称、起点终点、途经点等

#### 区域实体类

##### [WmsArea.java](#WmsArea-java) - 区域实体
- **功能**: 区域信息实体
- **主要字段**: 区域编码、区域名称、区域类型、地理坐标等

##### [WmsArea360.java](#WmsArea360-java) - 360度区域实体
- **功能**: 360度全景区域实体
- **主要字段**: 区域编码、全景图片、视角信息等

#### 报警实体类

##### [WmsAlarm.java](#WmsAlarm-java) - 报警信息实体
- **功能**: 报警信息实体
- **主要字段**: 报警ID、报警类型、发卡记录ID、姓名、工号、所属部门、报警时间、报警内容、处理状态等

##### [WmsAlarmLog.java](#WmsAlarmLog-java) - 报警日志实体
- **功能**: 报警日志记录实体
- **主要字段**: 日志ID、报警信息、处理时间、处理人员、处理结果等

##### [WmsAlarmRule.java](#WmsAlarmRule-java) - 报警规则实体
- **功能**: 报警规则配置实体
- **主要字段**: 规则ID、规则名称、时间阈值、距离阈值、目标区域代码、规则状态等

#### 设备实体类

##### [WmsDevice.java](#WmsDevice-java) - 设备实体
- **功能**: 设备基础信息实体
- **主要字段**: 设备ID、设备类型、设备描述、经度、纬度、高度、设备名称等

##### [WmsDeviceCameraLog.java](#WmsDeviceCameraLog-java) - 设备摄像头日志实体
- **功能**: 设备摄像头操作日志实体
- **主要字段**: 日志ID、设备信息、操作类型、操作时间、操作详情等

##### [WmsCardRecord.java](#WmsCardRecord-java) - 发卡记录实体
- **功能**: 发卡记录实体
- **主要字段**: 记录ID、卡号、发卡时间、发卡人员、关联人员等

#### 轨迹实体类

##### [WmsTrajectory.java](#WmsTrajectory-java) - 轨迹实体
- **功能**: 轨迹信息实体
- **主要字段**: 轨迹ID、轨迹点集合、轨迹类型、创建时间等

##### [WmsVehiclePositionHistory.java](#WmsVehiclePositionHistory-java) - 车辆位置历史实体
- **功能**: 车辆位置历史记录实体
- **主要字段**: 记录ID、车辆ID、位置坐标、时间戳、速度等

#### 定位卡相关实体类

##### [LanyaCoreArea.java](#LanyaCoreArea-java) - 定位卡区域实体
- **功能**: 定位卡定位区域实体
- **主要字段**: 区域ID、区域名称、区域范围、区域类型等

##### [LanyaCorePerson.java](#LanyaCorePerson-java) - 定位卡人员实体
- **功能**: 定位卡定位人员实体
- **主要字段**: 人员ID、卡号、姓名、部门、定位状态等

##### [LanyaCoreVisitor.java](#LanyaCoreVisitor-java) - 定位卡访客实体
- **功能**: 定位卡定位访客实体
- **主要字段**: 访客ID、卡号、姓名、访问目的、定位状态等

##### [LanyaCoreAlarm.java](#LanyaCoreAlarm-java) - 定位卡报警实体
- **功能**: 定位卡报警信息实体
- **主要字段**: 报警ID、报警类型、报警时间、报警内容等

##### [LanyaCoreAlarmPeople.java](#LanyaCoreAlarmPeople-java) - 定位卡人员报警实体
- **功能**: 定位卡人员报警实体
- **主要字段**: 报警ID、人员ID、报警原因、报警时间等

##### [LanyaVehicleAlarm.java](#LanyaVehicleAlarm-java) - 定位卡车辆报警实体
- **功能**: 定位卡车辆报警实体
- **主要字段**: 报警ID、车辆ID、报警时间、报警类型、报警内容等

##### [LanyaPositionCurrent.java](#LanyaPositionCurrent-java) - 实时定位实体
- **功能**: 实时定位信息实体
- **主要字段**: ID、卡号、坐标信息、时间戳、定位精度等

##### [LanyaPositionHistory.java](#LanyaPositionHistory-java) - 定位历史实体
- **功能**: 定位历史记录实体
- **主要字段**: ID、卡号、坐标信息、时间戳、定位状态等

##### [LanyaDeviceCard.java](#LanyaDeviceCard-java) - 定位卡定位卡实体
- **功能**: 定位卡定位卡管理实体
- **主要字段**: ID、卡号、卡类型、卡型号、IC卡号、电量百分比等

##### [LanyaDeviceCardMachine.java](#LanyaDeviceCardMachine-java) - 定位卡发卡机实体
- **功能**: 定位卡发卡机管理实体
- **主要字段**: ID、设备SN、设备名称、设备类型、安装位置等

##### [LanyaDeviceCardSenderLog.java](#LanyaDeviceCardSenderLog-java) - 定位卡发卡日志实体
- **功能**: 定位卡人脸发卡记录实体
- **主要字段**: ID、设备SN、设备名称、充电口号、定位卡号、发卡时间等

##### [LanyaDeviceCardSenderVehicleLog.java](#LanyaDeviceCardSenderVehicleLog-java) - 定位卡车辆发卡日志实体
- **功能**: 定位卡车辆发卡记录实体
- **主要字段**: ID、设备信息、车辆信息、发卡时间等

##### [LanyaInteralEmployee.java](#LanyaInteralEmployee-java) - 定位卡内部员工实体
- **功能**: 定位卡内部员工实体，继承自[BaseEntity](#BaseEntity)
- **主要字段**: 人员ID、人员类型、员工类型、定位图标类型、卡号等

##### [LanyaVehicleInfoVisitor.java](#LanyaVehicleInfoVisitor-java) - 定位卡访客车辆信息实体
- **功能**: 定位卡访客车辆信息实体
- **主要字段**: ID、访客信息、车辆信息、访问时间等

##### [LanyaTransfer.java](#LanyaTransfer-java) - 定位卡转移实体
- **功能**: 定位卡设备转移信息实体
- **主要字段**: ID、转移类型、转移时间、源设备、目标设备等

#### 系统日志实体类

##### [WmsSystemLog.java](#WmsSystemLog-java) - 系统日志实体
- **功能**: 后台任务调度日志实体
- **主要字段**: 日志ID、任务名称、任务类别、调用目标、日志信息、执行状态等

##### [SysOperLog.java](#SysOperLog-java) - 操作日志实体
- **功能**: 系统操作日志记录实体
- **主要字段**: 操作ID、操作模块、业务类型、请求方法、请求地址、操作人、操作时间等

#### 发卡工作日志实体类

##### [WmsDeviceCardWorkLog.java](#WmsDeviceCardWorkLog-java) - 发卡工作日志实体
- **功能**: 发卡工作记录实体
- **主要字段**: ID、定位卡号、人脸识别照片、人员ID、发卡时间、工作内容等

#### 巡检相关实体类

##### [WmsInspectionRule.java](#WmsInspectionRule-java) - 巡检规则实体
- **功能**: 巡检规则配置实体
- **主要字段**: ID、规则名称、巡检周期、时间误差、路径点、巡检点误差等

##### [WmsInspectionTask.java](#WmsInspectionTask-java) - 巡检任务实体
- **功能**: 巡检任务管理实体
- **主要字段**: ID、任务名称、任务状态、开始时间、结束时间、负责人等

##### [WmsInspectionIssue.java](#WmsInspectionIssue-java) - 巡检问题实体
- **功能**: 巡检发现问题实体
- **主要字段**: ID、问题描述、发现时间、处理状态、处理人员等

### 4. [mapper](#mapper) - 数据访问层

#### 物料相关Mapper

- [WmsMaterialMapper.java](#WmsMaterialMapper-java) - 物料数据访问接口
- [WmsMaterialDescMapper.java](#WmsMaterialDescMapper-java) - 物料档案数据访问接口
- [WmsMaterialInMapper.java](#WmsMaterialInMapper-java) - 物料入库数据访问接口
- [WmsMaterialOutMapper.java](#WmsMaterialOutMapper-java) - 物料出库数据访问接口
- [WmsMaterialStockMapper.java](#WmsMaterialStockMapper-java) - 物料库存数据访问接口
- [WmsMaterialTypeMapper.java](#WmsMaterialTypeMapper-java) - 物料类型数据访问接口
- [WmsMaterialIdentifyRecordMapper.java](#WmsMaterialIdentifyRecordMapper-java) - 物料识别记录数据访问接口
- [WmsMaterialStaticsDayMapper.java](#WmsMaterialStaticsDayMapper-java) - 物料日统计数据访问接口

#### 人员相关Mapper

- [WmsPersonMapper.java](#WmsPersonMapper-java) - 人员数据访问接口
- [WmsVisitorMapper.java](#WmsVisitorMapper-java) - 访客数据访问接口

#### 车辆相关Mapper

- [WmsVehicleMapper.java](#WmsVehicleMapper-java) - 车辆数据访问接口
- [WmsVehicleBlacklistMapper.java](#WmsVehicleBlacklistMapper-java) - 车辆黑名单数据访问接口
- [WmsVehicleRecordMapper.java](#WmsVehicleRecordMapper-java) - 车辆记录数据访问接口
- [WmsVehicleGatepassMapper.java](#WmsVehicleGatepassMapper-java) - 车辆通行证数据访问接口
- [WmsVehicleRouteMapper.java](#WmsVehicleRouteMapper-java) - 车辆路线数据访问接口

#### 区域相关Mapper

- [WmsAreaMapper.java](#WmsAreaMapper-java) - 区域数据访问接口
- [WmsArea360Mapper.java](#WmsArea360Mapper-java) - 360度区域数据访问接口

#### 报警相关Mapper

- [WmsAlarmMapper.java](#WmsAlarmMapper-java) - 报警数据访问接口
- [WmsAlarmLogMapper.java](#WmsAlarmLogMapper-java) - 报警日志数据访问接口
- [WmsAlarmRuleMapper.java](#WmsAlarmRuleMapper-java) - 报警规则数据访问接口

#### 设备相关Mapper

- [WmsDeviceMapper.java](#WmsDeviceMapper-java) - 设备数据访问接口
- [WmsDeviceCameraLogMapper.java](#WmsDeviceCameraLogMapper-java) - 设备摄像头日志数据访问接口
- [WmsCardRecordMapper.java](#WmsCardRecordMapper-java) - 发卡记录数据访问接口
- [WmsDeviceCardWorkLogMapper.java](#WmsDeviceCardWorkLogMapper-java) - 发卡工作日志数据访问接口

#### 轨迹相关Mapper

- [WmsTrajectoryMapper.java](#WmsTrajectoryMapper-java) - 轨迹数据访问接口
- [WmsVehiclePositionHistoryMapper.java](#WmsVehiclePositionHistoryMapper-java) - 车辆位置历史数据访问接口

#### 定位卡相关Mapper

- [LanyaCoreAreaMapper.java](#LanyaCoreAreaMapper-java) - 定位卡区域数据访问接口
- [LanyaCorePersonMapper.java](#LanyaCorePersonMapper-java) - 定位卡人员数据访问接口
- [LanyaCoreVisitorMapper.java](#LanyaCoreVisitorMapper-java) - 定位卡访客数据访问接口
- [LanyaCoreAlarmMapper.java](#LanyaCoreAlarmMapper-java) - 定位卡报警数据访问接口
- [LanyaCoreAlarmPeopleMapper.java](#LanyaCoreAlarmPeopleMapper-java) - 定位卡人员报警数据访问接口
- [LanyaVehicleAlarmMapper.java](#LanyaVehicleAlarmMapper-java) - 定位卡车辆报警数据访问接口
- [LanyaPositionCurrentMapper.java](#LanyaPositionCurrentMapper-java) - 实时定位数据访问接口
- [LanyaPositionHistoryMapper.java](#LanyaPositionHistoryMapper-java) - 定位历史数据访问接口
- [LanyaDeviceCardMapper.java](#LanyaDeviceCardMapper-java) - 定位卡定位卡数据访问接口
- [LanyaDeviceCardMachineMapper.java](#LanyaDeviceCardMachineMapper-java) - 定位卡发卡机数据访问接口
- [LanyaDeviceCardSenderLogMapper.java](#LanyaDeviceCardSenderLogMapper-java) - 定位卡发卡日志数据访问接口
- [LanyaDeviceCardSenderVehicleLogMapper.java](#LanyaDeviceCardSenderVehicleLogMapper-java) - 定位卡车辆发卡日志数据访问接口
- [LanyaInteralEmployeeMapper.java](#LanyaInteralEmployeeMapper-java) - 定位卡内部员工数据访问接口
- [LanyaVehicleInfoVisitorMapper.java](#LanyaVehicleInfoVisitorMapper-java) - 定位卡访客车辆信息数据访问接口
- [LanyaTransferMapper.java](#LanyaTransferMapper-java) - 定位卡转移数据访问接口

#### 巡检相关Mapper

- [WmsInspectionRuleMapper.java](#WmsInspectionRuleMapper-java) - 巡检规则数据访问接口
- [WmsInspectionTaskMapper.java](#WmsInspectionTaskMapper-java) - 巡检任务数据访问接口
- [WmsInspectionIssueMapper.java](#WmsInspectionIssueMapper-java) - 巡检问题数据访问接口

#### 系统相关Mapper

- [SysOperLogMapper.java](#SysOperLogMapper-java) - 操作日志数据访问接口
- [WmsSystemLogMapper.java](#WmsSystemLogMapper-java) - 系统日志数据访问接口

### 5. [service](#service) - 业务服务层

#### 物料服务接口及实现

- [IWmsMaterialService.java](#IWmsMaterialService-java) - 物料服务接口
- [IWmsMaterialDescService.java](#IWmsMaterialDescService-java) - 物料档案服务接口
- [IWmsMaterialInService.java](#IWmsMaterialInService-java) - 物料入库服务接口
- [IWmsMaterialOutService.java](#IWmsMaterialOutService-java) - 物料出库服务接口
- [IWmsMaterialStockService.java](#IWmsMaterialStockService-java) - 物料库存服务接口
- [IWmsMaterialTypeService.java](#IWmsMaterialTypeService-java) - 物料类型服务接口
- [IWmsMaterialIdentifyRecordService.java](#IWmsMaterialIdentifyRecordService-java) - 物料识别记录服务接口
- [IWmsMaterialStaticsDayService.java](#IWmsMaterialStaticsDayService-java) - 物料日统计服务接口

#### 人员服务接口及实现

- [IWmsPersonService.java](#IWmsPersonService-java) - 人员服务接口
- [IWmsVisitorService.java](#IWmsVisitorService-java) - 访客服务接口

#### 车辆服务接口及实现

- [IWmsVehicleService.java](#IWmsVehicleService-java) - 车辆服务接口
- [IWmsVehicleBlacklistService.java](#IWmsVehicleBlacklistService-java) - 车辆黑名单服务接口
- [IWmsVehicleRecordService.java](#IWmsVehicleRecordService-java) - 车辆记录服务接口
- [IWmsVehicleGatepassService.java](#IWmsVehicleGatepassService-java) - 车辆通行证服务接口
- [IWmsVehicleRouteService.java](#WmsVehicleRouteService-java) - 车辆路线服务接口

#### 区域服务接口及实现

- [IWmsAreaService.java](#IWmsAreaService-java) - 区域服务接口
- [IWmsArea360Service.java](#IWmsArea360Service-java) - 360度区域服务接口

#### 报警服务接口及实现

- [IWmsAlarmService.java](#IWmsAlarmService-java) - 报警服务接口
- [IWmsAlarmLogService.java](#IWmsAlarmLogService-java) - 报警日志服务接口
- [IWmsAlarmRuleService.java](#IWmsAlarmRuleService-java) - 报警规则服务接口

#### 设备服务接口及实现

- [IWmsDeviceService.java](#IWmsDeviceService-java) - 设备服务接口
- [IWmsDeviceCameraLogService.java](#IWmsDeviceCameraLogService-java) - 设备摄像头日志服务接口
- [IWmsCardRecordService.java](#IWmsCardRecordService-java) - 发卡记录服务接口

#### 轨迹服务接口及实现

- [IWmsTrajectoryService.java](#IWmsTrajectoryService-java) - 轨迹服务接口
- [IWmsVehiclePositionHistoryService.java](#IWmsVehiclePositionHistoryService-java) - 车辆位置历史服务接口

#### 定位卡服务接口及实现

- [ILanyaCoreAreaService.java](#ILanyaCoreAreaService-java) - 定位卡区域服务接口
- [ILanyaCorePersonService.java](#ILanyaCorePersonService-java) - 定位卡人员服务接口
- [ILanyaCoreVisitorService.java](#ILanyaCoreVisitorService-java) - 定位卡访客服务接口
- [ILanyaCoreAlarmService.java](#ILanyaCoreAlarmService-java) - 定位卡报警服务接口
- [ILanyaCoreAlarmPeopleService.java](#ILanyaCoreAlarmPeopleService-java) - 定位卡人员报警服务接口
- [ILanyaVehicleAlarmService.java](#ILanyaVehicleAlarmService-java) - 定位卡车辆报警服务接口
- [ILanyaPositionCurrentService.java](#ILanyaPositionCurrentService-java) - 实时定位服务接口
- [ILanyaPositionHistoryService.java](#ILanyaPositionHistoryService-java) - 定位历史服务接口
- [ILanyaDeviceCardService.java](#ILanyaDeviceCardService-java) - 定位卡定位卡服务接口
- [ILanyaDeviceCardMachineService.java](#ILanyaDeviceCardMachineService-java) - 定位卡发卡机服务接口
- [ILanyaDeviceCardSenderLogService.java](#ILanyaDeviceCardSenderLogService-java) - 定位卡发卡日志服务接口
- [ILanyaDeviceCardSenderVehicleLogService.java](#ILanyaDeviceCardSenderVehicleLogService-java) - 定位卡车辆发卡日志服务接口
- [ILanyaInteralEmployeeService.java](#ILanyaInteralEmployeeService-java) - 定位卡内部员工服务接口
- [ILanyaVehicleInfoVisitorService.java](#ILanyaVehicleInfoVisitorService-java) - 定位卡访客车辆信息服务接口
- [ILanyaTransferService.java](#ILanyaTransferService-java) - 定位卡转移服务接口

#### 巡检服务接口及实现

- [IWmsInspectionRuleService.java](#IWmsInspectionRuleService-java) - 巡检规则服务接口
- [IWmsInspectionTaskService.java](#IWmsInspectionTaskService-java) - 巡检任务服务接口
- [IWmsInspectionIssueService.java](#IWmsInspectionIssueService-java) - 巡检问题服务接口

#### 系统服务接口及实现

- [IWmsSystemLogService.java](#IWmsSystemLogService-java) - 系统日志服务接口
- [IWmsDeviceCardWorkLogService.java](#IWmsDeviceCardWorkLogService-java) - 发卡工作日志服务接口

### 6. [utils](#utils) - 工具类包

- [WmsUtils.java](#WmsUtils-java) - 仓储管理系统工具类
- [LanyaUtils.java](#LanyaUtils-java) - 定位卡定位工具类

### 7. [websocket](#websocket) - WebSocket服务包

- [WebSocketServer.java](#WebSocketServer-java) - WebSocket服务器
- [WebSocketConfig.java](#WebSocketConfig-java) - WebSocket配置
- [WebSocketService.java](#WebSocketService-java) - WebSocket服务接口
- [WebSocketServiceImpl.java](#WebSocketServiceImpl-java) - WebSocket服务实现

### 8. [lanya/data](#lanya-data) - 定位卡数据处理包

- [LanyaDataProcessor.java](#LanyaDataProcessor-java) - 定位卡数据处理器
- [LanyaDataSyncService.java](#LanyaDataSyncService-java) - 定位卡数据同步服务
- [LanyaPositionCalculator.java](#LanyaPositionCalculator-java) - 定位卡定位计算器
- [LanyaAlarmProcessor.java](#LanyaAlarmProcessor-java) - 定位卡报警处理器

### 9. [wzgs/sync](#wzgs-sync) - 数据同步包

- [WzgsDataSyncService.java](#WzgsDataSyncService-java) - 物资管理系统数据同步服务
- [WzgsDataSyncScheduler.java](#WzgsDataSyncScheduler-java) - 物资管理系统数据同步调度器
- [WzgsDataSyncConfig.java](#WzgsDataSyncConfig-java) - 物资管理系统数据同步配置

## 依赖关系

### [pom.xml](#pom-xml) - 模块依赖配置

```xml
<dependencies>
    <!-- WebSocket支持 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>

    <!-- 测试支持 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- 通用工具 -->
    <dependency>
        <groupId>com.ruoyi</groupId>
        <artifactId>ruoyi-common</artifactId>
    </dependency>

    <!-- Spring Web模块 -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
    </dependency>

    <!-- JTS几何库 -->
    <dependency>
        <groupId>org.locationtech.jts</groupId>
        <artifactId>jts-core</artifactId>
    </dependency>
</dependencies>
```


## 模块特点

### 1. 完整的物料管理体系
- 支持物料档案、类型、入库、出库、库存的全生命周期管理
- 提供物料识别和追踪功能

### 2. 智能化人员车辆管控
- 结合定位卡定位技术实现人员实时定位
- 车辆进出管理、黑名单管理、通行证管理

### 3. 全方位报警监控
- 多维度报警规则设置
- 实时报警推送和处理

### 4. 精确定位功能
- 基于定位卡技术的实时定位
- 历史轨迹回放功能

### 5. 设备集成管理
- 摄像头设备集成
- 发卡设备管理
- 设备日志记录

### 6. 巡检管理
- 巡检规则配置
- 巡检任务管理
- 巡检问题跟踪

## 使用建议

### 对于甲方用户
- [ruoyi-system](#ruoyi-system) 模块提供了完整的仓储管理功能，包括物料、人员、车辆、区域、报警等核心业务
- 定位卡定位功能提供了实时的位置监控能力
- 报警系统支持多种类型的报警规则配置

### 对于后续开发者
- 所有控制器都遵循统一的RESTful API设计规范
- 实体类使用Swagger注解，便于API文档生成
- 服务层采用接口实现分离的设计模式
- 数据访问层使用MyBatis，SQL语句集中在XML文件中
- 支持WebSocket实时通信功能




























## 核心实体类说明

### [WmsSystemLog](#WmsSystemLog) - 系统日志实体

位于 [com.ruoyi.system.domain.WmsSystemLog](file://D:\Jobs\dachun\仓储库区生产作业智能管控系统\RuoYi-Vue\ruoyi-system\src\main\java\com\ruoyi\system\domain\WmsSystemLog.java#L15-L136):

```java
public class WmsSystemLog extends BaseEntity {
    private Long jobLogId;        // 日志ID
    private String jobName;       // 后台任务名称  
    private String jobGroup;      // 后台任务类别
    private String invokeTarget;  // 调用目标字符串
    private String jobMessage;    // 日志信息
}
```


### [SysDept](#SysDept) - 部门实体

位于 [com.ruoyi.common.core.domain.entity.SysDept](file://D:\Jobs\dachun\仓储库区生产作业智能管控系统\RuoYi-Vue\ruoyi-common\src\main\java\com\ruoyi\common\core\domain\entity\SysDept.java#L17-L202):

- **功能**: 部门管理相关实体
- **关联服务**: [ISysDeptService](#ISysDeptService)

### [ISysDeptService](#ISysDeptService) - 部门管理服务接口

位于 [com.ruoyi.system.service.ISysDeptService](file://D:\Jobs\dachun\仓储库区生产作业智能管控系统\RuoYi-Vue\ruoyi-system\src\main\java\com\ruoyi\system\service\ISysDeptService.java#L11-L123):

```java
public interface ISysDeptService {
    public List<SysDept> selectDeptList(SysDept dept);
    public List<TreeSelect> selectDeptTreeList(SysDept dept);
    public List<SysDept> buildDeptTree(List<SysDept> depts);
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);
    public List<Long> selectDeptListByRoleId(Long roleId);
    public SysDept selectDeptById(Long deptId);
}
```


## 前端路由配置

### [router/index.js](#router-index-js) - 路由配置文件

```javascript
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
},
{
  path: 'access-log',
  component: () => import('@/views/wms_access_log/index'),
  name: '料场进出记录',
  meta: { title: '料场进出记录' }
},
{
  path: 'material-type',
  component: () => import('@/views/wms_material_type/index'),
  name: '料场进出记录',
  meta: { title: '料场进出记录' }
}
```


## 监控功能

### [server/index.vue](#server-index-vue) - 服务器监控页面

位于 `map/src/views/monitor/server/index.vue`:

- **显示项目路径**: `server.sys.userDir`
- **显示运行参数**: `server.jvm.inputArgs`
- **磁盘状态监控**: 磁盘使用情况展示

## 系统配置

### 项目根目录结构
```
RuoYi-Vue/
├── map/                    # 前端监控界面
│   └── src/
│       └── views/
│           └── monitor/
│               └── server/ # 服务器监控界面
├── ruoyi-system/          # 系统模块
│   └── src/main/java/com/ruoyi/system/
├── ruoyi-ui/              # 前端UI模块
└── pom.xml               # Maven 项目配置
```


### 主要配置文件

#### [pom.xml](#pom.xml) - 项目依赖配置
- Spring Boot 2.5.15
- Druid 数据库连接池 1.2.23
- Swagger API 文档工具 3.0.0
- PageHelper 分页插件 1.4.7
- Fastjson JSON 处理 2.0.58
- Oshi 系统信息监控 6.8.3

## 部署说明

### 环境要求
- JDK 1.8+
- Node.js 12+ (前端构建)
- MySQL 5.7+ 或其他兼容数据库
- Maven 3.6+

### 后端启动步骤
1. 导入数据库脚本
2. 修改 [application.yml](#application.yml) 中的数据库连接配置
3. 执行 `mvn clean install`
4. 执行 `mvn spring-boot:run` 启动后端服务

### 前端启动步骤
1. 进入 [ruoyi-ui](#ruoyi-ui) 目录
2. 执行 `npm install`
3. 执行 `npm run dev` 启动前端开发服务器

## 开发者说明

### 代码规范
- 遵循 Spring Boot 和 Vue.js 最佳实践
- 使用 Swagger 注解生成 API 文档
- 采用 RESTful API 设计风格

### 模块扩展
- **新增功能模块**: 需遵循现有架构模式
- **数据库变更**: 需同步更新实体类和 Service 层
- **前端页面**: 需集成到现有路由体系中

### 服务层设计
- **[ruoyi-system](#ruoyi-system)** 模块负责核心业务逻辑
- **[ISysDeptService](#ISysDeptService)** 接口定义部门管理服务
- **[WmsSystemLog](#WmsSystemLog)** 实体用于记录系统日志

### 测试要点
- 单元测试覆盖核心业务逻辑
- 集成测试验证前后端交互
- 性能测试关注高并发场景

## 注意事项

1. **权限控制**: 系统具备完善的权限管理体系，新增功能需考虑权限配置
2. **数据安全**: 涉及物料数据的操作需进行安全性检查
3. **性能优化**: 大数据量查询需使用分页和缓存机制
4. **日志记录**: 重要的业务操作需记录系统日志便于追溯