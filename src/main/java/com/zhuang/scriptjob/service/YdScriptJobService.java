package com.zhuang.scriptjob.service;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.TaskTable;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhuang.scriptjob.config.ScriptJobProperties;
import com.zhuang.scriptjob.entity.YdScriptJobLog;
import com.zhuang.scriptjob.mapper.YdScriptJobMapper;
import com.zhuang.scriptjob.entity.YdScriptJob;
import com.zhuang.scriptjob.util.JsEngineUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class YdScriptJobService extends ServiceImpl<YdScriptJobMapper, YdScriptJob> {

    public static final String TASK_ID_PREFIX = "script_";
    @Autowired
    private List<ScriptContextBean> contextBeanList;
    @Autowired
    private ScriptJobProperties scriptJobProperties;
    @Autowired
    private YdScriptJobLogService ydScriptJobLogService;

    /**
     * 获取已启用的脚本任务列表
     *
     * @return
     */
    public List<YdScriptJob> getEnabledList() {
        return list(new LambdaQueryWrapper<YdScriptJob>()
                .eq(YdScriptJob::getStatus, 1)
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
        List<YdScriptJob> ydScriptJobList = getEnabledList();
        // 获取脚本上下文
        Map<String, Object> context = getScriptContext();
        for (YdScriptJob ydScriptJob : ydScriptJobList) {
            CronUtil.schedule(TASK_ID_PREFIX + ydScriptJob.getId(), ydScriptJob.getCron(), () -> {
                execute(ydScriptJob, context);
            });
        }
    }

    /**
     * 执行脚本任务
     *
     * @param id
     */
    public YdScriptJobLog execute(String id) {
        YdScriptJob ydScriptJob = getById(id);
        Map<String, Object> context = getScriptContext();
        YdScriptJobLog ydScriptJobLog = execute(ydScriptJob, context);
        return ydScriptJobLog;
    }

    /**
     * 执行脚本任务
     *
     * @param ydScriptJob
     * @param context
     */
    public YdScriptJobLog execute(YdScriptJob ydScriptJob, Map<String, Object> context) {
        YdScriptJobLog ydScriptJobLog = new YdScriptJobLog();
        ydScriptJobLog.setJobId(ydScriptJob.getId());
        ydScriptJobLog.setJobName(ydScriptJob.getName());
        long startTime = System.currentTimeMillis();
        try {
            // 执行脚本
            Object result = JsEngineUtils.eval(ydScriptJob.getScript(), context, true);
            String jsonResult = JSONUtil.toJsonStr(result);
            ydScriptJobLog.setExecutionCode(0);
            ydScriptJobLog.setExecutionMessage(truncateExecutionMessage(jsonResult));
        } catch (Exception ex) {
            ydScriptJobLog.setExecutionCode(1);
            ydScriptJobLog.setExecutionMessage(truncateExecutionMessage(ExceptionUtil.stacktraceToString(ex)));
            log.error("script job execute fail! -> jobId={}, jobName={}, script={}", ydScriptJob.getId(), ydScriptJob.getName(), ydScriptJob.getScript(), ex);
        }
        Long executionTime = System.currentTimeMillis() - startTime;
        ydScriptJobLog.setCreateTime(new Date());
        ydScriptJobLog.setExecutionTime(executionTime.intValue());
        // 记录日志
        ydScriptJobLogService.save(ydScriptJobLog);
        return ydScriptJobLog;
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
