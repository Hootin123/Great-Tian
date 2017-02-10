package com.xtr.company.annotation;


import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.company.CompanyLogBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.service.company.CompanyLogService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.company.util.SessionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Date;


/**
 * <p>Service 切点类</p>
 *
 * @createTime: 2016/8/18 11:08
 */
@Aspect
@Component
public class SystemLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect.class);

    @Resource
    private CompanyLogService companyLogService;

    //Service层切点
    @Pointcut("@annotation(com.xtr.comm.annotation.SystemServiceLog)")
    public void serviceAspect() {
    }

    //Controller层切点
    @Pointcut("@annotation(com.xtr.comm.annotation.SystemControllerLog)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            //*========控制台输出=========*//  
            LOGGER.debug("=====前置通知开始=====");
            LOGGER.debug("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            LOGGER.debug("方法描述:" + getControllerOperation(joinPoint));

            //*========数据库日志=========*//
            CompanyLogBean sysLogBean = new CompanyLogBean();
            //所属模块
            sysLogBean.setModelName(getControllerModelName(joinPoint));
            //用户所做的操作
            sysLogBean.setOperation(getControllerOperation(joinPoint));
            //日志类型  0：操作日志 1：异常日志
            sysLogBean.setType(Integer.valueOf(0));
            //请求Ip
            sysLogBean.setRequestIp(request.getRemoteAddr());
            //服务器名称
            sysLogBean.setServerName(InetAddress.getLocalHost().getHostName());
            //操作人
            sysLogBean.setUserId(companyMembersBean.getMemberId());
            //创建时间
            sysLogBean.setCreateTime(new Date());

            //插入数据库
            companyLogService.insert(sysLogBean);

            LOGGER.debug("=====前置通知结束=====");
        } catch (Exception e) {
            //记录本地异常日志  
            LOGGER.error("==前置通知异常==");
            LOGGER.error("异常信息:{}", e.getMessage());
        }
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
            LOGGER.debug("方法描述:" + getControllerOperation(joinPoint));
            LOGGER.debug("请求IP:" + InetAddress.getLocalHost().getHostAddress());
            LOGGER.debug("请求参数:" + params);


            //*========数据库日志=========*//

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
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerOperation(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String operation = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    operation = method.getAnnotation(SystemControllerLog.class).operation();
                    break;
                }
            }
        }
        return operation;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerModelName(JoinPoint joinPoint) throws Exception {
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
                    modelName = method.getAnnotation(SystemControllerLog.class).modelName();
                    break;
                }
            }
        }
        return modelName;
    }
}  