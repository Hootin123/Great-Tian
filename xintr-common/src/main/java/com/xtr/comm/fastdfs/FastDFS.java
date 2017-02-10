package com.xtr.comm.fastdfs;

import com.xtr.comm.util.PropertyUtils;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * <p>文件上传工具类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/16 16:02
 */
public class FastDFS {

    public static TrackerClient trackerClient;

    private static final Logger logger = LoggerFactory.getLogger(FastDFS.class);


    //将文件转化为临时文件，方便获取图片的宽度和高度
    public static void inputstreamtofile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 上传文件
     * @param  InputStream
     * @return FastDFSResult
     */
    public static FastDFSResult uploadFile(InputStream fis, String fileType) {
        FastDFSResult result = new FastDFSResult();
        StorageClient storageClient = null;
        //FileInputStream fis = new FileInputStream(file);
        try {
            storageClient = getStorageClient();
            if (storageClient == null) {
                result.is_success = false;
                result.error_info = "storageClient出错,storageClient=null";
                return result;
            }

            byte[] file_buff = null;


            if (fis != null) {
                int len = fis.available();
                file_buff = new byte[len];
                fis.read(file_buff);
            }

//
//            result.img_width = 0;
//            result.img_height = 0;

            logger.info("file length: " + file_buff.length);

            String group_name = null;


            long startTime = System.currentTimeMillis();
            //String[] results = storageClient.upload_file(file_buff, "jpg", meta_list);
            //判断文件后缀名
            logger.info("======================start upload_file");
            String[] results = storageClient.upload_file(file_buff, fileType, null);
            logger.info("======================end upload_file");
            logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

            if (results == null) {
                logger.error("upload file fail, error code: " + storageClient.getErrorCode());
                result.is_success = false;
                result.error_info = "upload file fail, error code: " + storageClient.getErrorCode();
                return result;
            }

            group_name = results[0];
            String remote_filename = results[1];
            logger.info("uploadFile---->group_name: " + group_name + ", remote_filename: " + remote_filename);
            logger.info(storageClient.get_file_info(group_name, remote_filename) + "");

            result.group_name = group_name;
            result.remote_filename = remote_filename;
            result.remote_file_url = "/" + group_name + "/" + remote_filename;

            releaseStorageClient(storageClient);
        } catch (InterruptedException e) {
            //确实没有空闲连接,并不需要删除与fastdfs连接
            result.is_success = false;
            result.error_info = e.getMessage();
            logger.warn("没有空闲连接：" + e);
            return result;
        } catch (Exception e) {
            logger.warn("fastdfs出错：" + e.getMessage(), e);
            result.is_success = false;
            result.error_info = e.getMessage();
            ConnectionPool.getPoolInstance().drop(storageClient);
            return result;
        }
        result.is_success = true;
        return result;
    }

    /**
     * 文件下载
     *
     * @return
     */
    public static byte[] downloadFile(String groupName, String filePath) {
        StorageClient storageClient = null;
        try {
            logger.info("downloadFile---->group_name: " + groupName + ", remote_filename: " + filePath);
            storageClient = FastDFS.getStorageClient();
            byte[] bytes = storageClient.download_file(groupName, filePath);
            releaseStorageClient(storageClient);
            return bytes;
        } catch (Exception e) {
            logger.warn("fastdfs出错：" + e.getMessage(), e);
            ConnectionPool.getPoolInstance().drop(storageClient);
            return null;
        }
    }

    /**
     * s删除文件
     * @param groupName
     * @param filePath
     */
    public static void deleteFile(String groupName, String filePath) {
        StorageClient storageClient = null;
        try {
            logger.info("deleteFile---->group_name: " + groupName + ", remote_filename: " + filePath);
            storageClient = FastDFS.getStorageClient();
            storageClient.delete_file(groupName, filePath);
            releaseStorageClient(storageClient);
        } catch (Exception e) {
            logger.warn("fastdfs出错：" + e.getMessage(), e);
            ConnectionPool.getPoolInstance().drop(storageClient);
        }
    }


    /**
     * 获取fastDFS 访问文件的URL地址
     * fastDFS  可能没有通过域名访问文件的权限，只能通过IP访问
     */
    public static String getFASTDFSRequestIMGURL(String remote_url) {
        String url = "";
        String fastdfs_server = PropertyUtils.getString("FASTDFS_SERVER_IP");
        url += fastdfs_server;
        int http_tracker_http_port = PropertyUtils.getIntValue("http_tracker_http_port", 80);
        if (http_tracker_http_port != 80) {
            url += (":" + http_tracker_http_port);
        }
        url += remote_url;
        return url;
    }

	/*
     * 获取文件的URL地址
	 * @param Filepath
	 * @return FastDFSResult
	 */

    public static String getFastDFSFileURL(String remote_url) {
        String url = "";
        //此处直接使用FASTDFS_SERVER
        String fastdfs_server = PropertyUtils.getString("FASTDFS_SERVER");
        //是否使用了域名  fastdfs.http.ip_use
        //int fastdfs_http_ip_use=FastDFSConfig.http_ip_use;
        //PropertyUtils.getIntValue
        url += fastdfs_server;

        int http_tracker_http_port = PropertyUtils.getIntValue("http_tracker_http_port", 80);

        if (http_tracker_http_port != 80) {
            url += (":" + http_tracker_http_port);
        }
        url += remote_url;
        return url;
    }


    public static StorageClient getStorageClient1() {
        StorageClient storageClient = null;
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);
            storageClient = new StorageClient(trackerServer, storageServer);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {

            try {
                if (trackerServer != null) {
                    trackerServer.close();
                }
                if (storageServer != null) {
                    storageServer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storageClient;
    }

    public static StorageClient getStorageClient() throws InterruptedException {
        StorageClient storageClient = ConnectionPool.getPoolInstance().getStorgeClient(10);
        return storageClient;
    }

    public static void releaseStorageClient(StorageClient storageClient) {
        ConnectionPool.getPoolInstance().releaseStorgeClient(storageClient);
    }
}