-- ----------------------------
-- Table structure for sys_script_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_script_job`;
CREATE TABLE `sys_script_job`
(
    `id`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
    `name`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名字',
    `type`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
    `cron`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
    `script`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '脚本',
    `remark`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
    `status`      tinyint NULL DEFAULT NULL COMMENT '状态：0=禁用；1=启用；',
    `create_by`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `modify_by`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '脚本任务' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_script_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_script_job_log`;
CREATE TABLE `sys_script_job_log`
(
    `id`                int NOT NULL AUTO_INCREMENT COMMENT '主键',
    `job_id`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务Id',
    `job_name`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名字',
    `execution_time`    int NULL DEFAULT NULL COMMENT '任务执行时间（毫秒）',
    `execution_code`    tinyint NULL DEFAULT NULL COMMENT '执行结果编码：0=成功;非0=失败；',
    `execution_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行结果信息',
    `create_time`       datetime NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX               `idx_job_id`(`job_id` ASC) USING BTREE,
    INDEX               `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '脚本任务日志' ROW_FORMAT = Dynamic;

