package com.xtr.api.dto.sys;

import com.xtr.api.domain.sys.SysMenuBean;

import java.io.Serializable;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/7 16:16
 */

public class SysMenuDto extends SysMenuBean implements Serializable {

    /**
     * 子菜单
     */
    private List<SysMenuBean> childMenuList;


    public List<SysMenuBean> getChildMenuList() {
        return childMenuList;
    }

    public void setChildMenuList(List<SysMenuBean> childMenuList) {
        this.childMenuList = childMenuList;
    }
}
