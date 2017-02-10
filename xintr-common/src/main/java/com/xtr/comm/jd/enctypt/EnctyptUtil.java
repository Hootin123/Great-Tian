package com.xtr.comm.jd.enctypt;

import com.google.common.collect.Lists;
import com.wangyin.aks.security.sign.SignEnvelopService;
import com.wangyin.aks.security.sign.SignEnvelopServiceImpl;
import com.wangyin.aks.security.sign.util.Base64;
import com.wangyin.aks.security.sign.util.FileUtil;
import com.xtr.comm.jd.util.DES;
import com.xtr.comm.jd.util.SignUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnctyptUtil {
	/**
	 * 使用证书加密数据工具方法
	 * @param map  数据
	 * @param password 加密密码
	 * @param pirFilePath 私钥文件路径
	 * @param pubFilePath 公钥文件路径
	 * @return 返回加密后的字符串
	 * @throws Exception 
	 */
	public static String signEnvelop(Map<String,String> map,String password,String pirFilePath,String pubFilePath) throws Exception{
		String link= SignUtils.map2LinkString(map);//根据请求业务数据key字母排序将数据拼接成字符串
		byte[] data=link.getBytes(Contants.charset);
		String priCert=Base64.encode(FileUtil.readFile(new ClassPathResource(pirFilePath).getFile().getAbsolutePath()));//获取私钥
		String pubCert=Base64.encode(FileUtil.readFile(new ClassPathResource(pubFilePath).getFile().getAbsolutePath()));//获取公钥
		System.out.println("pfxBase64="+priCert);
		System.out.println("cerBase64="+pubCert);
		SignEnvelopService se=new SignEnvelopServiceImpl();
		String res=se.signEnvelop(priCert, password, pubCert, data);//数据加密
		return res;
	}
	
	public static String encryptDES(Map<String,String> map,String key) throws Exception{
		String link= SignUtils.map2LinkString(map);//根据请求业务数据key字母排序将数据拼接成字符串
		String encryptData = DES.encrypt(link,key,Contants.charset);
		return encryptData;
	}

	
	/**
	 * 签名验证方法
	 * @param map 
	 * @param singKey  签名密钥
	 * @return  验签通过返回解析后的数据,验证失败返回null
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String,String> verifySign (Map<String,String> map,String singKey) throws UnsupportedEncodingException{
		//获取签名类型和签名数据
        String sign_type = map.remove("sign_type").toString();
        String sign_data = map.remove("sign_data").toString();
        //验证签名(必须)
        boolean falg = SignUtils.verify(sign_data,map, sign_type, singKey, Contants.charset);
        if(falg){//签名通过返回解析后的数据，不包含签名类型和签名数据
        	return map; 
        }
        //签名不通过返回null
        return null;
	}

	/**
	 * 签名
	 * @param paramMap
	 * @param signKey
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(Map<String, String> paramMap,String signKey) throws UnsupportedEncodingException{
		String sign = SignUtils.sign(paramMap, Contants.singType, signKey, Contants.charset);
		
		return sign;
	}
}
