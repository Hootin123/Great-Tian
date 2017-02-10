package com.xtr.api.util;

import com.xtr.api.basic.TreeNode;
import com.xtr.api.basic.TreeState;
import com.xtr.api.domain.sys.SysMenuBean;
import com.xtr.api.domain.sys.SysMenuBtnBean;
import com.xtr.api.domain.sys.SysRoleBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>菜单树工具类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/20 16:31
 */
public class TreeUtil {

    /**
     * 根节点菜单对象
     */
    List<SysMenuBean> rootMenus;

    /**
     * 子节点菜单对象
     */
    List<SysMenuBean> childMenus;

    /**
     * 按钮对象
     */
    List<SysMenuBtnBean> childBtns;

    /**
     * 角色对应菜单权限
     */
    Map<Long, Long> menuMap;

    /**
     * 角色对应按钮权限
     */
    Map<Long, Long> btnMap;

    /**
     * 角色列表
     */
    List<SysRoleBean> roleList;

    /**
     * 用户角色
     */
    Map<Long, Long> roleMap;

    public TreeUtil(List<SysRoleBean> roleList, Map<Long, Long> roleMap) {
        this.roleList = roleList;
        this.roleMap = roleMap;
    }

    public TreeUtil(List<SysMenuBean> rootMenus, List<SysMenuBean> childMenus) {
        this.rootMenus = rootMenus;
        this.childMenus = childMenus;
    }

    public TreeUtil(List<SysMenuBean> rootMenus, List<SysMenuBean> childMenus, List<SysMenuBtnBean> childBtns) {
        this.rootMenus = rootMenus;
        this.childMenus = childMenus;
        this.childBtns = childBtns;
    }

    public TreeUtil(List<SysMenuBean> rootMenus, List<SysMenuBean> childMenus, List<SysMenuBtnBean> childBtns, Map<Long, Long> menuMap, Map<Long, Long> btnMap) {
        this.rootMenus = rootMenus;
        this.childMenus = childMenus;
        this.childBtns = childBtns;
        this.menuMap = menuMap;
        this.btnMap = btnMap;
    }

    public List<TreeNode> getTreeNode() {
        return getRootNodes();
    }


    /**
     * 将菜单转成树形节点
     *
     * @param sysMenuBean
     * @return
     */
    public TreeNode menuToNode(SysMenuBean sysMenuBean) {
        if (sysMenuBean == null)
            return null;
        TreeNode treeNode = new TreeNode();
        treeNode.setId(sysMenuBean.getId());
        treeNode.setText(sysMenuBean.getMenuName());
        TreeState treeState = new TreeState();
        if (menuMap != null && menuMap.size() > 0) {
            treeState.setChecked(menuMap.containsKey(sysMenuBean.getId()));
        }
        if (sysMenuBean.getParentId() == null) {
            //虚功能节点
            treeNode.setMenuType("0");
        } else {
            treeNode.setMenuType("1");
        }
        treeState.setExpanded(false);
        treeNode.setState(treeState);
        return treeNode;
    }

    /**
     * @param sysMenuBtnBean
     * @return
     */
    private TreeNode BtnToNode(SysMenuBtnBean sysMenuBtnBean) {
        if (sysMenuBtnBean == null) {
            return null;
        }
        TreeNode treeNode = new TreeNode();
        treeNode.setId(sysMenuBtnBean.getId());
        treeNode.setText(sysMenuBtnBean.getBtnName());
        TreeState treeState = new TreeState();
        if (btnMap != null && btnMap.size() > 0) {
            treeState.setChecked(btnMap.containsKey(sysMenuBtnBean.getId()));
        }
        treeNode.setMenuType("2");
        treeState.setExpanded(false);
        treeNode.setState(treeState);
        return treeNode;
    }

    /**
     *
     * @param sysRoleBean
     * @return
     */
    private TreeNode roleToNode(SysRoleBean sysRoleBean){
        if (sysRoleBean == null) {
            return null;
        }
        TreeNode treeNode = new TreeNode();
        treeNode.setId(sysRoleBean.getId());
        treeNode.setText(sysRoleBean.getRoleName());
        TreeState treeState = new TreeState();
        if (roleMap != null && roleMap.size() > 0) {
            treeState.setChecked(roleMap.containsKey(sysRoleBean.getId()));
        }
//        treeNode.setMenuType("2");
        treeState.setExpanded(false);
        treeNode.setState(treeState);
        return treeNode;
    }

    /**
     * @return
     */
    private List<TreeNode> getRootNodes() {
        List<TreeNode> rootNodes = new ArrayList<TreeNode>();
        for (SysMenuBean menu : rootMenus) {
            TreeNode node = menuToNode(menu);
            if (node != null) {
                addChlidNodes(node);
                rootNodes.add(node);
            }
        }
        return rootNodes;
    }


    /**
     * @param rootNode
     */
    private void addChlidNodes(TreeNode rootNode) {
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for (SysMenuBean menu : childMenus) {
            if (rootNode.getId().equals(menu.getParentId())) {
                TreeNode node = menuToNode(menu);
                if (childBtns != null && !childBtns.isEmpty()) {
                    addChlidBtn(node);
                }
                childNodes.add(node);
            }
        }
        rootNode.setNodes(childNodes);
    }

    /**
     * 设置菜单button
     *
     * @param treeNode
     * @return
     */
    private void addChlidBtn(TreeNode treeNode) {
        List<TreeNode> childNodes = new ArrayList<TreeNode>();
        for (SysMenuBtnBean btn : childBtns) {
            if (treeNode.getId().equals(btn.getMenuId())) {
                TreeNode node = BtnToNode(btn);
                childNodes.add(node);
            }
        }
        treeNode.setNodes(childNodes);
    }

    /**
     * 获取角色节点列表
     *
     * @return
     */
    public List<TreeNode> getRoleNodes() {
        List<TreeNode> roleNodes = new ArrayList<TreeNode>();
        for (SysRoleBean sysRoleBean : roleList) {
            TreeNode node = roleToNode(sysRoleBean);
            if (node != null) {
                roleNodes.add(node);
            }
        }
        return roleNodes;
    }

}
