package com.xtr.comm.fastdfs;


import com.xtr.comm.util.PropertyUtils;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>文件上传工具类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/16 16:02
 */
public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    //链接池次数
    private int size = PropertyUtils.getIntValue("FASTDFS_POOLSIZE", 5);
    //工作连接
    private ConcurrentMap<StorageClient, Object> busyConnectionPool = null;
    //连接池
    private ArrayBlockingQueue<StorageClient> idleConnectionPool = null;
    private Object obj = new Object();

    //构造函数
    private ConnectionPool() {
        busyConnectionPool = new ConcurrentHashMap<StorageClient, Object>();
        idleConnectionPool = new ArrayBlockingQueue<StorageClient>(size);
        init(size);
    }

    private void initClientGlobal() {
        int connect_timeout = PropertyUtils.getIntValue("connect_timeout", 300);
        logger.info("----connect_timeout:" + connect_timeout);

        int network_timeout = PropertyUtils.getIntValue("network_timeout", 3000);
        int http_tracker_http_port = PropertyUtils.getIntValue("http_tracker_http_port", 80);
        int http_anti_steal_token = PropertyUtils.getIntValue("http_anti_steal_token", 0);
        String http_secret_key = PropertyUtils.getString("http_secret_key");
        String str_arr_tracker_server = PropertyUtils.getString("tracker_server");

        logger.info("------tracker_server:" + str_arr_tracker_server);

        String[] arr_tracker_servers = str_arr_tracker_server.split(";");
        String charset = PropertyUtils.getString("charset");

        boolean b_http_anti_steal_token = false;
        if (http_anti_steal_token == 1) {
            b_http_anti_steal_token = true;
        }

        ClientGlobal.setG_anti_steal_token(b_http_anti_steal_token);
        ClientGlobal.setG_charset(charset);
        ClientGlobal.setG_connect_timeout(connect_timeout);
        ClientGlobal.setG_network_timeout(network_timeout);
        ClientGlobal.setG_secret_key(http_secret_key);
        ClientGlobal.setG_tracker_http_port(http_tracker_http_port);
        InetSocketAddress[] tracker_servers = new InetSocketAddress[arr_tracker_servers.length];
        String[] parts;
        for (int i = 0; i < arr_tracker_servers.length; i++) {
            parts = arr_tracker_servers[i].split("\\:", 2);
            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        TrackerGroup g_tracker_group;
        g_tracker_group = new TrackerGroup(tracker_servers);

        ClientGlobal.setG_tracker_group(g_tracker_group);
    }

    private static ConnectionPool instance = new ConnectionPool();

    // get the connection pool instance
    public static ConnectionPool getPoolInstance() {
        return instance;
    }

    //初始化
    private void init(int size) {
        initClientGlobal();
        TrackerServer trackerServer = null;
        try {
            TrackerClient trackerClient = new TrackerClient();

            for (int i = 0; i < size; i++) {
                //Only tracker
                trackerServer = trackerClient.getConnection();
                //建立连接后，需要发送一次请求，可以避免recv package size -1 != 10
                //参照http://bbs.chinaunix.net/forum.php?mod=viewthread&tid=4057013&highlight=%B3%D8
                ProtoCommon.activeTest(trackerServer.getSocket());
                StorageServer storageServer = null;
                StorageClient client = new StorageClient(trackerServer,
                        storageServer);
                idleConnectionPool.add(client);
            }
        } catch (IOException e) {
            logger.error("fastdfs池初始出错");
            logger.error(e.getMessage());
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (IOException e) {

                    logger.error("fastdfs池trackerServer关闭出错");
                    logger.error(e.getMessage());
                }
            }
        }
    }

    // 1. pop one connection from the idleConnectionPool,
    // 2. push the connection into busyConnectionPool;
    // 3. return the connection
    // 4. if no idle connection, do wait for wait_time seconds, and check again
    public StorageClient getStorgeClient(int waitTimes) throws InterruptedException {
        StorageClient client = idleConnectionPool.poll(waitTimes,
                TimeUnit.SECONDS);
        busyConnectionPool.put(client, obj);
        return client;
    }

    // 1. pop the connection from busyConnectionPool;
    // 2. push the connection into idleConnectionPool;
    // 3. do nessary cleanup works.
    public synchronized void releaseStorgeClient(StorageClient client) {
        if (busyConnectionPool.remove(client) != null) {
            idleConnectionPool.add(client);
        }
    }

    // so if the connection was broken due to some erros (like
    // : socket init failure, network broken etc), drop this connection
    // from the busyConnectionPool, and init one new connection.
    public synchronized void drop(StorageClient client) {
        if (client == null) {
            logger.warn("fast链接池 drop 出错 为空");
            return;
        }
        if (busyConnectionPool.remove(client) != null) {
            TrackerServer trackerServer = null;
            try {
                TrackerClient trackerClient = new TrackerClient();
                //TODO 此处有内存泄露，因为trackerServer没有关闭连接
                trackerServer = trackerClient.getConnection();
                //建立连接后，需要发送一次请求，可以避免recv package size -1 != 10
                //参照http://bbs.chinaunix.net/forum.php?mod=viewthread&tid=4057013&highlight=%B3%D8
                ProtoCommon.activeTest(trackerServer.getSocket());
                StorageServer storageServer = null;
                StorageClient newClient = new StorageClient(trackerServer, storageServer);

                idleConnectionPool.add(newClient);
            } catch (IOException e) {
                logger.error("fastdfs池drop出错");
                logger.error(e.getMessage());
            } finally {
                if (trackerServer != null) {
                    try {
                        trackerServer.close();
                    } catch (IOException e) {
                        logger.error("fastdfs池drop出错");
                        logger.error(e.getMessage());
                    }
                }
            }
        }
    }


}
