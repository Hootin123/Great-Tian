package com.xtr.comm.util;

import java.util.Random;

/**
 * Created by abiao on 2016/6/26.
 */
public class RandomNumber {

    /**
     * 生成某长度随机数字符串
     * @param random
     * @param len
     * @return
     */
    public static String getRandomNumberByLength(Random random,int len){

        if(len<1 || len>100){
            return "";
        }

        String r="";
        for(int i=0;i<len;i++){
            r=r+random.nextInt(10);
        }

        return r;
    }

    /**
     * 生成某长度随机数字字母串
     * @param random
     * @param len
     * @return
     */
    public static String getRandomStringByLength(Random random,int len){

        if(len<1 || len>100){
            return "";
        }

        String r="";
        for(int i=0;i<len;i++){
            int t=random.nextInt(2);

            if(t==0){
                r=r+random.nextInt(10);
            }else{
                char c=(char)(random.nextInt(26)+65);
                r=r+c;
            }
        }
        return r.toLowerCase();
    }

    /**
     *  生成某长度随机字母串
     * @param random
     * @param len
     * @return
     */
    public static String getRandomLetterByLength(Random random, int len){

        if(len<1 || len>100){
            return "";
        }

        String r="";
        for(int i=0;i<len;i++){

            char c=(char)(random.nextInt(26)+65);
            r=r+c;

        }
        return r.toLowerCase();
    }
}
