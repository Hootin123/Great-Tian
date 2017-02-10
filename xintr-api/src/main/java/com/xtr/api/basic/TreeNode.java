package com.xtr.api.basic;

import java.io.Serializable;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/20 15:54
 */

public class TreeNode implements Serializable {

    /**
     * 菜单主键
     */
    private Long id;

    /**
     * 菜单名
     */
    private String text;

    /**
     * 按钮类型 0：虚功能节点  1：功能节点  2：菜单按钮
     */
    private String menuType;

    /**
     * 值
     */
    private String value;

    /**
     * 子菜单
     */
    private List<TreeNode> nodes;

    private TreeState state;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public TreeState getState() {
        return state;
    }

    public void setState(TreeState state) {
        this.state = state;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
