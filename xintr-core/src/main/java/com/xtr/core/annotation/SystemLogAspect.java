package com.xtr.core.annotation;


import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.sys.SysLogBean;
import com.xtr.api.service.sys.SysLogService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * <p>Service 切点类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/5 11:08
 */
@Aspect
@Component
public class SystemLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect.class);

    @Resource
    private SysLogService sysLogService;


    //Service层切点
    @Pointcut("@annotation(com.xtr.comm.annotation.SystemServiceLog)")
    public void serviceAspect() {

    }


    /**
     * 异常通知 用于拦截service层记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";";
            }
        }
        try {
              /*========控制台输出=========*/
            LOGGER.debug("=====异常通知开始=====");
            LOGGER.debug("异常代码:" + e.getClass().getName());
            LOGGER.debug("异常信息:" + e.getMessage());
            LOGGER.debug("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            LOGGER.debug("方法描述:" + getServiceOperation(joinPoint));
            LOGGER.debug("请求参数:" + params);


            //*========数据库日志=========*//
            SysLogBean sysLogBean = new SysLogBean();
            //创建时间
            sysLogBean.setCreateTime(new Date());
            //用户所做的操作
            sysLogBean.setOperation(getServiceOperation(joinPoint));
            //所属模块
            sysLogBean.setModelName(getServiceModelName(joinPoint));
            //日志类型0：操作日志 1：异常日志
            sysLogBean.setType(Integer.valueOf(1));
            //异常内容
            String msg = StringUtils.getStackTrace(e);
            msg = msg.length() > 2048 ? msg.substring(0, 2047) : msg;
            sysLogBean.setContent(msg);

            sysLogService.insert(sysLogBean);
            LOGGER.debug("=====异常通知结束=====");
        } catch (Exception ex) {
            //记录本地异常日志
            LOGGER.error("==异常通知异常==");
            LOGGER.error("异常信息:{}", ex.getMessage());
        }
         /*==========记录本地异常日志==========*/
        LOGGER.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);

    }


    /**
     * 获取注解中对方法的描述信息 用于Task层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getServiceOperation(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemServiceLog.class).operation();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Service层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getServiceModelName(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String modelName = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    modelName = method.getAnnotation(SystemServiceLog.class).modelName();
                    break;
                }
            }
        }
        return modelName;
    }
}  