package com.xtr.manager.controller.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysRoleBean;
import com.xtr.api.domain.sys.SysRoleRelBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.sys.SysRoleRelService;
import com.xtr.api.service.sys.SysRoleService;
import com.xtr.api.service.sys.SysUserService;
import com.xtr.api.util.TreeUtil;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.MethodUtil;
import com.xtr.comm.util.RandomDataUtil;
import com.xtr.comm.util.RegexUtil;
import com.xtr.manager.util.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/15 17:45
 */
@Controller
public class SysUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleRelService sysRoleRelService;

    /**
     * 操作员管理界面
     *
     * @param mav
     * @return
     */
    @RequestMapping("sysUser/user.htm")
    public ModelAndView list(ModelAndView mav) {
        mav.setViewName("xtr/sys/user/user");
        return mav;
    }

    @RequestMapping("sysUser/dataList.htm")
    @ResponseBody
    public ResultResponse dataList(SysUserBean sysUserBean,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   @RequestParam(value = "order", required = false) String order,
                                   @RequestParam(value = "search", required = false) String search) {
        if (StringUtils.isNotBlank(search)) {
            if (RegexUtil.checkMobile(search)) {
                sysUserBean.setMobilePhone(search);
            } else if (RegexUtil.checkEmail(search)) {
                sysUserBean.setEmail(search);
            }
        }
        sysUserBean.setPageIndex(pageIndex);
        sysUserBean.setPageSize(pageSize);
        ResultResponse resultResponse = sysUserService.selectPageList(sysUserBean);
//        HtmlUtil.writerJson(response, sysUserService.selectPageList(sysUserBean));
        return resultResponse;
    }

    /**
     * 新增/修改用户页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("sysUser/add.htm")
    public ModelAndView add(ModelAndView mav, Long id) {
        if (id != null) {
            mav.addObject("user", sysUserService.selectByPrimaryKey(id));
        }
        mav.addObject("id", id);
        mav.setViewName("xtr/sys/user/add");
        return mav;
    }

    /**
     * 新增/修改操作员
     *
     * @param sysUserBean
     */
    @RequestMapping("sysUser/save.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增/修改操作员", modelName = "操作员管理")
    public ResultResponse addUser(HttpServletRequest request, SysUserBean sysUserBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (sysUserBean != null) {
            int result = 0;
            if (sysUserBean.getId() != null) {
                sysUserBean.setUpdateTime(new Date());
                result = sysUserService.updateByPrimaryKeySelective(sysUserBean);
            } else {
//                sysUserBean.setCreateTime(new Date());
//                sysUserBean.setCreateUser(SessionUtils.getUser(request).getId());
                resultResponse = sysUserService.insert(sysUserBean.getMobilePhone(), sysUserBean.getPwd());
            }
            if (result > 0) {
                resultResponse.setSuccess(true);
            }
        }
        return resultResponse;
    }

    /**
     * 删除用户
     *
     * @return
     */
    @RequestMapping("sysUser/delete.htm")
    @ResponseBody
    @SystemControllerLog(operation = "删除操作员", modelName = "操作员管理")
    public ResultResponse delete(Long id) {
        return sysUserService.deleteByPrimaryKey(id);
    }


    /**
     * 进入注册界面(index)
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "register.htm")
    @ResponseBody
    public ModelAndView toRegister(ModelAndView mav) {
        mav.setViewName("xtr/sys/user/register");
        return mav;
    }

    /**
     * 校验手机号或邮箱地址是否被注册
     *
     * @return
     */
    @RequestMapping(value = "isRegister.htm")
    public void isRegister(HttpServletResponse response, @RequestParam("js_phone") String js_phone) throws IOException {
        if (StringUtils.isNotBlank(js_phone)) {
            SysUserBean sysUserBean = sysUserService.selectByEmailOrPhone(js_phone, null);
            response.getWriter().print(sysUserBean == null);
        }
    }


    /**
     * 用户注册
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "registerUser.htm")
    @ResponseBody
    public ResultResponse register(HttpServletRequest request,
                                   @RequestParam("js_phone") String js_phone,
                                   @RequestParam("js_pwd1") String js_pwd1,
                                   @RequestParam("js_pwd2") String js_pwd2) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isNotBlank(js_phone) && StringUtils.equals(js_pwd1, js_pwd2)) {
            resultResponse = sysUserService.insert(js_phone, js_pwd1);
        } else {
            resultResponse.setMessage("用户名或密码不能为空");
        }
        return resultResponse;
    }

    /**
     * 用户授权界面
     *
     * @param mav
     * @return
     */
    @RequestMapping("sysUser/userAuth.htm")
    public ModelAndView userAuth(ModelAndView mav) {
        mav.setViewName("xtr/sys/user/userAuth");
        return mav;
    }

    /**
     * 角色数据列表
     *
     * @param userId
     * @return
     */
    @RequestMapping("sysUser/roleList.htm")
    @ResponseBody
    public void roleList(HttpServletResponse response, Long userId) {
        ResultResponse resultResponse = new ResultResponse();
        //获取所有角色列表
        List<SysRoleBean> roleList = sysRoleService.queryAll();
        //获取用户角色列表
        List<SysRoleRelBean> roleRelList = sysRoleRelService.queryByObjId(userId, SysRoleRelBean.RelType.USER.key);
        TreeUtil treeUtil = new TreeUtil(roleList, getRoleMap(roleRelList));
        HtmlUtil.writerJson(response, JSON.toJSONString(treeUtil.getRoleNodes()));
    }

    /**
     * 获取角色map
     *
     * @param list
     * @return
     */
    private Map<Long, Long> getRoleMap(List<SysRoleRelBean> list) {
        Map<Long, Long> map = new HashMap();
        if (!list.isEmpty()) {
            for (SysRoleRelBean sysRoleRelBean : list) {
                map.put(sysRoleRelBean.getRoleId(), sysRoleRelBean.getRoleId());
            }
        }
        return map;
    }

    /**
     * 新增用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @RequestMapping("sysUser/addUserRole.htm")
    @ResponseBody
    @SystemControllerLog(operation = "用户授权", modelName = "操作员管理")
    public ResultResponse addUserRole(Long userId, Long[] roleIds) {
        return sysUserService.addUserRole(userId, roleIds);
    }

    /**
     * 密码修改页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("sysUser/updatePwdPage.htm")
    @ResponseBody
    public ModelAndView updatePwdPage(ModelAndView mav) {
        mav.setViewName("xtr/sys/user/updatePwd");
        return mav;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping("sysUser/updatePwd.htm")
    @ResponseBody
    @SystemControllerLog(operation = "修改密码", modelName = "操作员管理")
    public ResultResponse updatePwd(HttpServletRequest request, @RequestParam("oldPwd") String oldPwd,
                                    @RequestParam("newPwd") String newPwd,
                                    @RequestParam("configPwd") String configPwd) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            resultResponse = sysUserService.updatePwd(sysUserBean.getId(), oldPwd, newPwd, configPwd);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("修改密码出现异常，请刷新界面重试");
        }
        return resultResponse;
    }

    /**
     * 密码重置
     *
     * @param userId
     * @return
     */
    @RequestMapping("sysUser/resetPwd.htm")
    @ResponseBody
    @SystemControllerLog(operation = "密码重置", modelName = "操作员管理")
    public ResultResponse resetPwd(Long userId) {
        ResultResponse resultResponse = new ResultResponse();
        if (userId != null) {
            try {
                SysUserBean sysUserBean = sysUserService.selectByPrimaryKey(userId);
                if (sysUserBean != null) {
                    String pwd = RandomDataUtil.getRandomID(6).toLowerCase();
                    sysUserBean.setSalt(RandomDataUtil.getRandomID());
                    sysUserBean.setPwd(MethodUtil.MD5(pwd + sysUserBean.getSalt()));
                    int result = sysUserService.updateByPrimaryKeySelective(sysUserBean);
                    if (result > 0) {
                        resultResponse.setData(pwd);
                        resultResponse.setSuccess(true);
                    } else {
                        resultResponse.setMessage("重置密码失败");
                    }
                } else {
                    resultResponse.setMessage("用户不存在");
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                resultResponse.setMessage("重置密码失败");
            }
        } else {
            resultResponse.setMessage("请选择需要重置密码的用户");
        }
        return resultResponse;
    }

}
