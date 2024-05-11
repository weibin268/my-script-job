package com.zhuang.scriptjob.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 脚本任务日志
 * </p>
 *
 * @author zwb
 * @since 2024-05-11
 */
@Getter
@Setter
@TableName("sys_script_job_log")
@ApiModel(value = "SysScriptJobLog对象", description = "脚本任务日志")
public class SysScriptJobLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("任务Id")
    private String jobId;

    @ApiModelProperty("任务名字")
    private String jobName;

    @ApiModelProperty("任务执行时间（毫秒）")
    private Integer executionTime;

    @ApiModelProperty("执行结果编码：0=成功;非0=失败；")
    private Integer executionCode;

    @ApiModelProperty("执行结果信息")
    private String executionMessage;

    @ApiModelProperty("创建时间")
    private Date createTime;


}
