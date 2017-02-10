package com.xtr.api.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBtnBean;

import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 17:33
 */

public interface SysMenuBtnService {

    /**
     * 根据指定主键获取一条数据库记录,sys_menu_btn
     *
     * @param id
     */
    ResultResponse selectByPrimaryKey(Long id);

    /**
     * 根据用户id查询按钮
     *
     * @param userId
     */
    ResultResponse getMenuBtnByUser(Long userId);

    /**
     * 查询所有按钮
     *
     * @return
     */
    List<SysMenuBtnBean> queryByAll();
}
