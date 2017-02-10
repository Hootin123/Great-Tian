package com.xtr.api.dto.salary;

import com.alibaba.fastjson.JSONObject;
import com.xtr.api.domain.salary.AllowanceSettingBean;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/24 9:30.
 */
public class AllowanceSettingDto extends AllowanceSettingBean implements Serializable {

    // 适用人员
    private JSONObject members;
    //适用类型 1:全体职员 2:部门 3:员工
    private Integer type;
    //适用人员数据
    private String userData;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public JSONObject getMembers() {
        return members;
    }

    public void setMembers(JSONObject members) {
        this.members = members;
    }
}
