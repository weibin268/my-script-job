package com.zhuang.scriptjob.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 脚本任务
 * </p>
 *
 * @author zwb
 * @since 2024-05-11
 */
@Getter
@Setter
@TableName("sys_script_job")
@ApiModel(value = "SysScriptJob对象", description = "脚本任务")
public class SysScriptJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("cron表达式")
    private String cron;

    @ApiModelProperty("脚本")
    private String script;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态：0=禁用；1=启用；")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String modifyBy;

    @ApiModelProperty("修改时间")
    private Date modifyTime;


}
