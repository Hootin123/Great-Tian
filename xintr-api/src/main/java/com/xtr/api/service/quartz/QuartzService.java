package com.xtr.api.service.quartz;

/**
 * <p>定时任务接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/28 14:44
 */
public interface QuartzService {


    /**
     * @param jobName 任务名
     * @param cls     任务
     * @param time    时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     */
    void addJob(String jobName, Class cls, String time);

    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param time             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    void addJob(String jobName, String jobGroupName,
                String triggerName, String triggerGroupName, Class jobClass,
                String time);

    /**
     * @param jobName
     * @param time
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     */
    void modifyJobTime(String jobName, String time);

    /**
     * @param triggerName
     * @param triggerGroupName
     * @param time
     * @Description: 修改一个任务的触发时间
     */
    void modifyJobTime(String triggerName,
                       String triggerGroupName, String time);

    /**
     * @param jobName
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     */
    void removeJob(String jobName);

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    void removeJob(String jobName, String jobGroupName,
                   String triggerName, String triggerGroupName);

    /**
     * 停止
     *
     * @param triggerName
     * @param group
     */
    void pauseTrigger(String triggerName, String group);

    /**
     * 恢复、重启
     *
     * @param triggerName
     * @param group
     */
    void resumeTrigger(String triggerName, String group);

    /**
     * 删除
     *
     * @param triggerName
     * @param group
     * @return
     */
    boolean removeTrigdger(String triggerName, String group);

    /**
     * 启动所有定时任务
     */
    void startJobs();

    /**
     * 关闭所有定时任务
     */
    void shutdownJobs();
}
