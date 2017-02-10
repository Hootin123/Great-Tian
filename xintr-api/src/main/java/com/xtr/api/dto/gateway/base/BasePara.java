package com.xtr.api.dto.gateway.base;

import com.xtr.comm.basic.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuewu on 2016/8/3.
 * 支付请求 参数 基类
 */
public abstract class BasePara implements Serializable, GatewayLog {

    protected BusinessType businessType;

    protected String businessId;

    protected String logContent;

    protected int logType;

    protected String apiName;


    /**
     * 创建Jd的map
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> createReqMap(boolean dumpEmpty) throws BusinessException {
        Map<String, String> reqMap = new HashMap<>();
        List<CachedMethod> methodList = getAllDeclaredMethod();
        for (CachedMethod cachedGetterMethod : methodList) {
            try {
                String value = (String)cachedGetterMethod.getGetterMethod().invoke(this);
                if (dumpEmpty && StringUtils.isEmpty(value))
                    continue;
                reqMap.put(cachedGetterMethod.getReqName(), value);
            } catch (Exception e) {
                throw new BusinessException("参数转换异常", e);
            }
        }

        return reqMap;
    }

    /**
     * 获取到某个bean的所有的方法
     *
     * @return
     */
    protected List<CachedMethod> getAllDeclaredMethod() {
        if (beanGetterMethodMap.get(this.getClass()) == null) {
            List<CachedMethod> methodList = getCachedGetterMethods(this.getClass());
            beanGetterMethodMap.put(this.getClass(), methodList);
        }
        return beanGetterMethodMap.get(this.getClass());
    }

    /**
     * 反射Jd实体bean的属性名和get方法的缓存
     **/
    static Map<Class<?>, List<CachedMethod>> beanGetterMethodMap = new ConcurrentHashMap<Class<?>, List<CachedMethod>>();

    /**
     * @param clz
     * @return
     */
    protected static synchronized List<CachedMethod> getCachedGetterMethods(Class<?> clz) {
        List<CachedMethod> list = new ArrayList<CachedMethod>();
        List<Field> allFileds = getAllFiled(clz);
        for (Field field : allFileds) {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clz, field.getName());
            Method getterMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            list.add(new CachedMethod(field.getAnnotation(ParaName.class).value(), getterMethod, writeMethod));
        }
        return list;

    }

    /**
     * 获取到该bean的所有Jd的属性
     *
     * @param clz
     * @return
     */
    static List<Field> getAllFiled(Class<?> clz) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields = clz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(ParaName.class) != null) {
                list.add(fields[i]);
            }
        }
        if (!clz.equals(BasePara.class)) {
            list.addAll(getAllFiled(clz.getSuperclass()));
        }
        return list;
    }

    public static Map<Class<?>, List<CachedMethod>> getBeanGetterMethodMap() {
        return beanGetterMethodMap;
    }

    public static void setBeanGetterMethodMap(Map<Class<?>, List<CachedMethod>> beanGetterMethodMap) {
        BasePara.beanGetterMethodMap = beanGetterMethodMap;
    }

    @Override
    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    @Override
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    @Override
    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    @Override
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
