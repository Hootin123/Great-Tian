package com.xtr.comm.basic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by abiao on 2016/6/22.
 */
public class GenerateMD5 {

    private static MessageDigest md5 = null;
    private static Object mutex = new Object();
    public static MessageDigest getMD5(){
        synchronized(mutex){
            if(md5 == null){
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            return md5;
        }
    }
}
