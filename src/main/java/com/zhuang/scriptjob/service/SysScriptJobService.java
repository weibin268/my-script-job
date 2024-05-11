package com.zhuang.scriptjob.service;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.TaskTable;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuang.scriptjob.config.ScriptJobProperties;
import com.zhuang.scriptjob.entity.SysScriptJobLog;
import com.zhuang.scriptjob.mapper.SysScriptJobMapper;
import com.zhuang.scriptjob.entity.SysScriptJob;
import com.zhuang.scriptjob.util.JsEngineUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 脚本任务 服务类
 * </p>
 *
 * @author zwb
 * @since 2024-05-11
 */
@Slf4j
@Service
public class SysScriptJobService extends ServiceImpl<SysScriptJobMapper, SysScriptJob> {

    public static final String TASK_ID_PREFIX = "script_";
    @Autowired
    private List<ScriptContextBean> contextBeanList;
    @Autowired
    private ScriptJobProperties scriptJobProperties;
    @Autowired
    private SysScriptJobLogService sysScriptJobLogService;

    /**
     * 获取已启用的脚本任务列表
     *
     * @return
     */
    public List<SysScriptJob> getEnabledList() {
        return list(new LambdaQueryWrapper<SysScriptJob>()
                .eq(SysScriptJob::getStatus, 1)
        );
    }

    /**
     * 加载定时脚本任务
     */
    public void loadScriptJobs() {
        // 判断是否启用
        if (!scriptJobProperties.isEnabled()) return;
        // 停止任务调度
        if (CronUtil.getScheduler().isStarted()) {
            CronUtil.stop();
        }
        //清除任务调度
        TaskTable taskTable = CronUtil.getScheduler().getTaskTable();
        for (String id : taskTable.getIds()) {
            if (id.startsWith(TASK_ID_PREFIX)) {
                CronUtil.remove(id);
            }
        }
        // 开启任务调度
        if (!CronUtil.getScheduler().isMatchSecond()) {
            CronUtil.setMatchSecond(true);
        }
        if (!CronUtil.getScheduler().isStarted()) {
            CronUtil.start();
        }
        // 添加任务调度
        List<SysScriptJob> sysScriptJobList = getEnabledList();
        // 获取脚本上下文
        Map<String, Object> context = getScriptContext();
        for (SysScriptJob sysScriptJob : sysScriptJobList) {
            CronUtil.schedule(TASK_ID_PREFIX + sysScriptJob.getId(), sysScriptJob.getCron(), () -> {
                execute(sysScriptJob, context);
            });
        }
    }

    /**
     * 执行脚本任务
     *
     * @param id
     */
    public SysScriptJobLog execute(String id) {
        SysScriptJob sysScriptJob = getById(id);
        Map<String, Object> context = getScriptContext();
        SysScriptJobLog sysScriptJobLog = execute(sysScriptJob, context);
        return sysScriptJobLog;
    }

    /**
     * 执行脚本任务
     *
     * @param sysScriptJob
     * @param context
     */
    public SysScriptJobLog execute(SysScriptJob sysScriptJob, Map<String, Object> context) {
        SysScriptJobLog sysScriptJobLog = new SysScriptJobLog();
        sysScriptJobLog.setJobId(sysScriptJob.getId());
        sysScriptJobLog.setJobName(sysScriptJob.getName());
        long startTime = System.currentTimeMillis();
        try {
            // 执行脚本
            Object result = JsEngineUtils.eval(sysScriptJob.getScript(), context, true);
            String jsonResult = JSONUtil.toJsonStr(result);
            sysScriptJobLog.setExecutionCode(0);
            sysScriptJobLog.setExecutionMessage(truncateExecutionMessage(jsonResult));
        } catch (Exception ex) {
            sysScriptJobLog.setExecutionCode(1);
            sysScriptJobLog.setExecutionMessage(truncateExecutionMessage(ExceptionUtil.stacktraceToString(ex)));
            log.error("script job execute fail! -> jobId={}, jobName={}, script={}", sysScriptJob.getId(), sysScriptJob.getName(), sysScriptJob.getScript(), ex);
        }
        Long executionTime = System.currentTimeMillis() - startTime;
        sysScriptJobLog.setCreateTime(new Date());
        sysScriptJobLog.setExecutionTime(executionTime.intValue());
        // 记录日志
        sysScriptJobLogService.save(sysScriptJobLog);
        return sysScriptJobLog;
    }

    /**
     * 获取脚本任务上下文
     *
     * @return
     */
    public Map<String, Object> getScriptContext() {
        Map<String, Object> map = new HashMap<>();
        for (ScriptContextBean contextBean : contextBeanList) {
            map.put(contextBean.getBanName(), contextBean);
        }
        return map;
    }

    /**
     * 截断执行信息
     *
     * @param executionMessage
     * @return
     */
    public String truncateExecutionMessage(String executionMessage) {
        if (StrUtil.isNotEmpty(executionMessage) && executionMessage.length() > 1000) {
            return executionMessage.substring(0, 1000);
        }
        return executionMessage;
    }
}
