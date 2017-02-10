package com.xtr.company.web;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>文件下载</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 9:24
 */

public interface IExcelView {


    /**
     * 文件下载
     *
     * @param newFileName
     * @param response
     * @param fileName
     * @throws Exception
     */
    void download(String fileType,String newFileName, String fileName, HttpServletResponse response) throws Exception;
}
