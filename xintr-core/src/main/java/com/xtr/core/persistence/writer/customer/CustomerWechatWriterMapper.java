package com.xtr.core.persistence.writer.customer;

import com.xtr.api.domain.customer.CustomerWechatBean;

/**
 * @author:zhangshuai
 * @date: 2016/9/7.
 */
public interface CustomerWechatWriterMapper {
    /**
     * 保存员工绑定信息
     * @param customerWechatBean
     * @return
     */
    int saveCustomerBind(CustomerWechatBean customerWechatBean);
}
