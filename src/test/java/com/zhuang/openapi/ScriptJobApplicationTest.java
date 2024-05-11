package com.zhuang.openapi;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Rollback
@Transactional
@SpringBootTest(classes = {ScriptJobApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
@EnableCaching
public class ScriptJobApplicationTest {

}
