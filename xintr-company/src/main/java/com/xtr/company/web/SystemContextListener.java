package com.xtr.company.web;

import com.xtr.api.domain.sys.SysMenuBean;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.SpringUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import java.io.*;

/**
 * 系统启动加载类。所有需要初始化的数据请放在这里。
 *
 * @author 张峰
 */
public class SystemContextListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        super.contextInitialized(sce);
        SpringUtils.setApplicationContext((WebApplicationContext) sce.getServletContext().
                getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));

//        File file = new File("C:\\Users\\Administrator\\Desktop\\接口问题.docx");
//
//        try {
//            // 获取指定文件的输入流
////            InputStream inputStream  = new FileInputStream(file);
////            boolean flag = AliOss.uploadFile(inputStream,"测试.docx");
////            System.out.println("返回结果：" + flag);
//
//            InputStream inputStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.file"),"测试.docx");
//            System.out.println("返回结果：" + inputStream.read());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
