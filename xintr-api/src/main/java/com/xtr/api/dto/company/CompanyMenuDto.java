package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyMenuBean;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单目录
 * @author:zhangshuai
 * @date: 2016/11/3.
 */
public class CompanyMenuDto extends CompanyMenuBean implements Serializable {


    //二级菜单
    private List<CompanyMenuBean> childrenMenu;
    //该菜单是否有二级菜单（1：表明该菜单有二级菜单；0：表示该菜单没有二级菜单）
    private Integer hasSecondMenu;

    public Integer getHasSecondMenu() {
        return hasSecondMenu;
    }

    public void setHasSecondMenu(Integer hasSecondMenu) {
        this.hasSecondMenu = hasSecondMenu;
    }

    public List<CompanyMenuBean> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(List<CompanyMenuBean> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }
}
