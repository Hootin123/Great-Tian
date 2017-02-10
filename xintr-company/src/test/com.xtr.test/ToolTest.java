package com.xtr.test;

import com.xtr.comm.custom.XtrTools;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/13 14:08
 */
public class ToolTest {

    public final static int LIMIT = 2000;

    public static void main(String[] args) {
//        XtrTools xtrTools = new XtrTools();
//        String m = xtrTools.money(new BigDecimal(10033333.00d));
//        System.out.println(m);
        try {
//            int cur = 1000;
//            int order  = 2147483647;
//            if(order >0 && order + 1000 <= 2000){
//                System.out.println("成功");
//            } else {
//                System.out.println("失败");
//            }
            System.setSecurityManager(new SecurityManager() {
                @Override
                public void checkExit(int status) {
                    throw new ThreadDeath();
                }
            });
            try {
                System.exit(0);
            } finally {
                System.out.println("In the finally block");
            }
        }catch (Exception e){
            System.out.println("你大爷报错了···");
        }
    }

}
