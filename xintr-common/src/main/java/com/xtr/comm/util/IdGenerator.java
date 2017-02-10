package com.xtr.comm.util;

import com.xtr.comm.enums.BusinessEnum;

import java.util.Random;

/**
 * <p>订单id生成</p>
 *
 * @author 任齐
 * @createTime: 2016/6/30 18:45
 */
public class IdGenerator {

	private static final Random R = new Random();

	/**
	 * 获取一个订单ID
	 *
	 * @param type   业务类型
	 * @param seq    五位订单序号
	 *
     * @return
     */
	/*public static long getOrderId(BusinessEnum type, int seq){
		return Long.valueOf(getOrderIdString(type, seq));
	}*/

	/**
	 * 获取一个订单ID
	 *
	 * @param type   业务类型
	 * @param seq    五位订单序号
	 *
	 * @return
	 */
	/*public synchronized static String getOrderIdString(BusinessEnum type, int seq){
		String seqStr = String.format("%05d", seq);
		int random = R.nextInt(10)%(10);
		return DateUtil.getCurrDate("yyyyMMdd") + seqStr + random + type.getCode();
	}*/

}