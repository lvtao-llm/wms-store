package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.WmsInspectionRule;
import com.ruoyi.system.domain.WmsInspectionTask;
import com.ruoyi.system.service.IWmsInspectionRuleService;
import com.ruoyi.system.service.IWmsInspectionTaskService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 巡检守护任务
 *
 * @author 吕涛
 * @version 1.0
 * @since 2025/10/25
 */
@Component("inspectionGuardian")
public class InspectionGuardian {
    /**
     * 检查并生成巡检任务
     */
    public void checkAndGenerateTasks() {
        IWmsInspectionRuleService inspectionRuleService = SpringUtils.getBean(IWmsInspectionRuleService.class);
        IWmsInspectionTaskService inspectionTaskService = SpringUtils.getBean(IWmsInspectionTaskService.class);
        // 查询所有启用的巡检规则
        WmsInspectionRule queryRule = new WmsInspectionRule();
        queryRule.setStatus(0); // 启用状态
        List<WmsInspectionRule> rules = inspectionRuleService.selectWmsInspectionRuleList(queryRule);

        // 遍历规则，检查是否需要生成任务
        for (WmsInspectionRule rule : rules) {
            Date lastTaskTime = rule.getNextTaskTime() == null ? new Date() : rule.getNextTaskTime();
            Date now = new Date();
            // 根据cron表达式判断是否应该触发任务
            if (rule.getStatus() == 0 && (lastTaskTime.before(now) || rule.getNextTaskTime() == null)) {
                String cron = rule.getCron();
                // 使用CronTrigger计算下次执行时间
                CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                        .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                        .build();
                Date nextExecutionTime = cronTrigger.getFireTimeAfter(now);

                WmsInspectionTask task = new WmsInspectionTask();
                task.setRuleId(rule.getId());
                task.setRuleName(rule.getName());
                task.setTaskTime(nextExecutionTime); // 设置任务时间
                task.setInspectionResult("0"); // 默认未巡检状态
                // 插入任务记录
                inspectionTaskService.insertWmsInspectionTask(task);
                rule.setNextTaskTime(nextExecutionTime);
                rule.setNextTaskID(task.getId());
                inspectionRuleService.updateWmsInspectionRuleNext(rule);
            }
        }
    }
}
