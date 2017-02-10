package com.xtr.comm.jd.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class SignUtils {
    private static final String[] hexStrings;

    static {
        hexStrings = new String[256];
        for (int i = 0; i < 256; i++) {
            StringBuilder d = new StringBuilder(2);
            char ch = Character.forDigit(((byte) i >> 4) & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            ch = Character.forDigit((byte) i & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            hexStrings[i] = d.toString();
        }
    }

    private static String[] IGNORE = new String[]{
            "sign_type", "sign_data", "encrypt_type", "encrypt_data","salt"
    };

    /**
     * 计算签名
     * @param map 有key和value的map，使用=和&拼接所有参数，
     *            "sign_type", "sign_data", "encrypt_type", "encrypt_data"不参加计算
     * @param algorithm 签名算法 MD5, SHA-1, SHA-256
     * @param salt 签名密钥
     * @param charset 字符串编码
     * @return 签名
     */
    public static String sign(Map<String, String> map, String algorithm, String salt, String charset) throws UnsupportedEncodingException {
        String linkString = map2LinkString(map);
        String data = linkString + salt;
        System.out.println("signData = " + data);
        return digestHex(algorithm, data, charset);
    }

    /**
     * 验证签名正确性。
     * @param sign 签名数据
     * @param map 数据
     * @param algorithm 签名算法 MD5, SHA-1, SHA-256
     * @param salt 签名密钥
     * @param charset 字符串
     * @return 验证结果
     */
    public static boolean verify(String sign, Map<String, String> map, String algorithm, String salt,
                                 String charset) throws UnsupportedEncodingException {
        if (sign==null || "".equals(sign.trim()) || map.size()==0) return false;
        String newSign = sign(map, algorithm, salt, charset);
        return newSign.equals(sign);
    }

    /**
     * 将MAP数据用=和&拼接成String
     * @param map 数据
     * @return 字符串
     */
    public static String map2LinkString(Map<String, String> map) {
        ArrayList<String> mapKeys = new ArrayList<String>(map.keySet());
        Collections.sort(mapKeys);
        StringBuilder link = new StringBuilder();
        boolean first = true;
        for_map_keys:
        for(String key: mapKeys) {
            String value = map.get(key);
            if (value==null || "".equals(value.trim())) continue;
            for(String i: IGNORE) {
                if (i.equalsIgnoreCase(key)) continue for_map_keys;
            }

            if (!first) link.append("&");
            link.append(key).append("=").append(value);
            if (first) first = false;
        }
        return link.toString();
    }
    
   
    

    /**
     * 对数据进行指定算法的数据摘要
     *
     * @param algorithm 算法名，如MD2, MD5, SHA-1, SHA-256, SHA-512
     * @param data      待计算的数据
     * @param charset   字符串的编码
     * @return 摘要结果
     */
    public static String digestHex(String algorithm, String data, String charset) throws UnsupportedEncodingException {
        byte[] bytes = data.getBytes(charset);
    	byte[] digest = DigestUtils.getDigest(algorithm).digest(data.getBytes(charset));
        return hexString(digest);
    }

    /**
     * 将字节数组转换成HEX String
     *
     * @param b
     * @return HEX String
     */
    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            d.append(hexStrings[(int) aB & 0xFF]);
        }
        System.out.println("sign="+d.toString());
        return d.toString();
    }
}
