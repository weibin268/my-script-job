-- ----------------------------
-- Table structure for sys_openapi_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_openapi_log`;
CREATE TABLE `sys_openapi_log`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `api_name`          varchar(100) DEFAULT NULL COMMENT 'api名字',
    `api_params`        text COMMENT 'api参数值',
    `api_execute_times` int(11) DEFAULT NULL COMMENT '接口执行时间（单位：毫秒）',
    `client_ip`         varchar(50)  DEFAULT NULL COMMENT '客户端ip',
    `create_time`       datetime     DEFAULT NULL COMMENT '创建时间',
    `openapi_user_id`   varchar(50)  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='OpenApi调用日志';

-- ----------------------------
-- Table structure for sys_openapi_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_openapi_user`;
CREATE TABLE `sys_openapi_user`
(
    `id`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID主键',
    `username`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
    `password`    varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
    `remark`      text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
    `status`      int(11) NULL DEFAULT NULL COMMENT '状态：0=禁用；1=启用；',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
    `modify_by`   varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'OpenApi用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_openapi_user_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_openapi_user_ref`;
CREATE TABLE `sys_openapi_user_ref`
(
    `id`              int(11) NOT NULL COMMENT 'ID',
    `openapi_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OpenApi用户Id',
    `ref_table`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联表名',
    `ref_id`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联记录Id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'OpenApi用户资源关联表' ROW_FORMAT = Dynamic;
