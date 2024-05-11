package com.zhuang.openapi;

import com.zhuang.scriptjob.SysScriptJobAutoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration(SysScriptJobAutoConfig.class)
public class ScriptJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScriptJobApplication.class, args);
    }


}
