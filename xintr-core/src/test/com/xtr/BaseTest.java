package com.xtr;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * <p>测试基类</p>
 *
 * @author 任齐
 * @createTime: 2016/6/30 18:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationcontext.xml")
@TransactionConfiguration(defaultRollback = true)
public class BaseTest {

}
