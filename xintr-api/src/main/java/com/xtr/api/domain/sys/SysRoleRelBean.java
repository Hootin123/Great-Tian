package com.xtr.api.domain.sys;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;

public class SysRoleRelBean extends BaseObject implements Serializable{
    /**
     *  角色主键 sys_role.id,所属表字段为sys_role_rel.role_id
     */
    private Long roleId;

    /**
     *  关联主键 type=0管理sys_menu.id, type=1关联sys_user.id,所属表字段为sys_role_rel.obj_id
     */
    private Long objId;

    /**
     *  关联类型 0=菜单,1=用户,所属表字段为sys_role_rel.rel_type
     */
    private Integer relType;

    /**
     * 枚举
     */
    public enum RelType {
        MENU(0, "菜单"), USER(1,"用户"),BTN(2,"按钮");
        public int key;
        public String value;
        RelType(int key, String value) {
            this.key = key;
            this.value = value;
        }
        public static RelType get(int key) {
            RelType[] values = RelType.values();
            for (RelType object : values) {
                if (object.key == key) {
                    return object;
                }
            }
            return null;
        }
    }

    /**
     * 获取 角色主键 sys_role.id 字段:sys_role_rel.role_id
     *
     * @return sys_role_rel.role_id, 角色主键 sys_role.id
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置 角色主键 sys_role.id 字段:sys_role_rel.role_id
     *
     * @param roleId sys_role_rel.role_id, 角色主键 sys_role.id
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 获取 关联主键 type=0管理sys_menu.id, type=1关联sys_user.id 字段:sys_role_rel.obj_id
     *
     * @return sys_role_rel.obj_id, 关联主键 type=0管理sys_menu.id, type=1关联sys_user.id
     */
    public Long getObjId() {
        return objId;
    }

    /**
     * 设置 关联主键 type=0管理sys_menu.id, type=1关联sys_user.id 字段:sys_role_rel.obj_id
     *
     * @param objId sys_role_rel.obj_id, 关联主键 type=0管理sys_menu.id, type=1关联sys_user.id
     */
    public void setObjId(Long objId) {
        this.objId = objId;
    }

    /**
     * 获取 关联类型 0=菜单,1=用户 字段:sys_role_rel.rel_type
     *
     * @return sys_role_rel.rel_type, 关联类型 0=菜单,1=用户
     */
    public Integer getRelType() {
        return relType;
    }

    /**
     * 设置 关联类型 0=菜单,1=用户 字段:sys_role_rel.rel_type
     *
     * @param relType sys_role_rel.rel_type, 关联类型 0=菜单,1=用户
     */
    public void setRelType(Integer relType) {
        this.relType = relType;
    }
}