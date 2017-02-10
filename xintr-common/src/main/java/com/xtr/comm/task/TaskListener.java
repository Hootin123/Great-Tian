package com.xtr.comm.task;


/**
 * <p>描述</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 13:02
 */
public interface TaskListener {

	void beforeTaskStart(TaskContext taskContext);
	
	void onTaskStart(TaskContext taskContext);
	
	void onTaskSuccess(TaskContext taskContext);
	
	void onTaskFail(TaskContext taskContext);

}
