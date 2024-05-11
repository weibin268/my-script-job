package com.zhuang.scriptjob;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@MapperScan("com.zhuang.scriptjob.mapper")
public class SysScriptJobAutoConfig {

}
