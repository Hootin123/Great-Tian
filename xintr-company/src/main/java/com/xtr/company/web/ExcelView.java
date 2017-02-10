/**
 *
 */
package com.xtr.company.web;

import com.xtr.comm.oss.AliOss;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * <p>Excel下载</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/16 13:50
 */
@Component("excelView")
public class ExcelView implements IExcelView {

    private static Log log = LogFactory.getLog(ExcelView.class);


    /**
     * 文件下载
     *
     * @param newFileName
     * @param response
     * @param fileName
     * @throws Exception
     */
    public void download(String fileType, String newFileName, String fileName, HttpServletResponse response) throws Exception {
        InputStream inputStream = AliOss.downloadFile(fileType, newFileName);

        //设置没有缓存
        response.reset();

        if (log.isDebugEnabled())
            log.debug("ExcelView... file name:" + fileName);

        if (StringUtils.isNotBlank(fileName)) {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }else{
            fileName = newFileName;
        }

        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");
        OutputStream outputStream = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        outputStream.close();
    }
}
