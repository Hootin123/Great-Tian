package com.xtr.core.service.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysRoleRelBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.sys.SysUserService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.MethodUtil;
import com.xtr.comm.util.RandomDataUtil;
import com.xtr.comm.util.RegexUtil;
import com.xtr.core.persistence.reader.sys.SysUserReaderMapper;
import com.xtr.core.persistence.writer.sys.SysRoleRelWriterMapper;
import com.xtr.core.persistence.writer.sys.SysUserWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>用户服务实现类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/13 14:14
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    private SysUserWriterMapper sysUserWriterMapper;

    @Resource
    private SysUserReaderMapper sysUserReaderMapper;

    @Resource
    private SysRoleRelWriterMapper sysRoleRelWriterMapper;

    /**
     * 根据指定主键获取一条数据库记录,sys_user
     *
     * @param id
     */
    public SysUserBean selectByPrimaryKey(Long id) {
        if (id != null) {
            return sysUserReaderMapper.selectByPrimaryKey(id);
        } else {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 新增用户
     */
    public int addSysUser(SysUserBean record) {
        ResultResponse resultResponse = new ResultResponse();
        if (record != null) {
            return sysUserWriterMapper.insert(record);
        } else {
            throw new BusinessException("用户信息插入失败，参数不能为空");
        }
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_user
     *
     * @param record
     */
    @SystemServiceLog(operation = "修改操作员信息", modelName = "操作员管理")
    public int updateByPrimaryKeySelective(SysUserBean record) {
        if (record != null) {
            return sysUserWriterMapper.updateByPrimaryKeySelective(record);
        } else {
            throw new BusinessException("用户信息更新失败，参数不能为空");
        }
    }

    /**
     * 根据主键删除数据库的记录,sys_user
     *
     * @param id
     */
    @SystemServiceLog(operation = "删除操作员", modelName = "操作员管理")
    public ResultResponse deleteByPrimaryKey(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        if (id != null) {
            int result = sysUserWriterMapper.deleteByPrimaryKey(id);
            if (result > 0) {
                resultResponse.setSuccess(true);
            }
        } else {
            resultResponse.setMessage("用户信息删除失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 新增用户授权
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @SystemServiceLog(operation = "操作员授权", modelName = "操作员管理")
    public ResultResponse addUserRole(Long userId, Long[] roleIds) {
        ResultResponse resultResponse = new ResultResponse();
        if (userId == null) {
            resultResponse.setMessage("用户授权失败，参数不能为空");
            return resultResponse;
        }
        //清除关联关系
        sysRoleRelWriterMapper.deleteByObjId(userId, SysRoleRelBean.RelType.USER.key);

        if (roleIds != null && roleIds.length > 0) {
            for (Long roleId : roleIds) {
                SysRoleRelBean sysRoleRelBean = new SysRoleRelBean();
                sysRoleRelBean.setRoleId(roleId);
                sysRoleRelBean.setObjId(userId);
                sysRoleRelBean.setRelType(SysRoleRelBean.RelType.USER.key);
                sysRoleRelWriterMapper.insert(sysRoleRelBean);
            }
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 分页查询用户信息
     *
     * @param sysUserBean
     * @return
     */
    public ResultResponse selectPageList(SysUserBean sysUserBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(sysUserBean.getPageIndex(), sysUserBean.getPageSize());
        PageList<SysUserBean> list = sysUserReaderMapper.listPage(sysUserBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 根据手机号或邮箱地址查询用户信息
     *
     * @param userName
     * @return
     */
    public SysUserBean selectByEmailOrPhone(String userName, Long userId) {
        if (StringUtils.isNotBlank(userName)) {
            return sysUserReaderMapper.selectByEmailOrPhone(userName, userId);
        } else {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 新增操作员
     *
     * @param userName
     * @param pwd
     * @return
     */
    @SystemServiceLog(operation = "新增操作员", modelName = "操作员管理")
    public ResultResponse insert(String userName, String pwd) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean = new SysUserBean();

            if (RegexUtil.checkMobile(userName))
                sysUserBean.setMobilePhone(userName);
            else
                sysUserBean.setEmail(userName);

            //检查是否已注册
            resultResponse = checkUser(sysUserBean);
            if (!resultResponse.isSuccess()) {
                return resultResponse;
            }
            resultResponse.setSuccess(false);
            sysUserBean.setSalt(RandomDataUtil.getRandomID());
//            sysUserBean.setPwd(new Md5PasswordEncoder().encodePassword(pwd, sysUserBean.getSalt()));
            sysUserBean.setPwd(MethodUtil.MD5(pwd + sysUserBean.getSalt()));
            sysUserBean.setNickName(userName);//昵称默认为用户名
            sysUserBean.setUserName(userName);//默认为用户名
            sysUserBean.setCreateTime(new Date());
            int count = sysUserWriterMapper.insertSelective(sysUserBean);
            if (count > 0) {
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("新增用户失败,请重试");
                return resultResponse;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("系统错误");
        }
        return resultResponse;
    }


    /**
     * 检查手机号或邮箱地址
     *
     * @param sysUserBean
     * @return
     */
    public ResultResponse checkUser(SysUserBean sysUserBean) {
        ResultResponse resultResponse = new ResultResponse();
        //非空检查
        if (StringUtils.isBlank(sysUserBean.getMobilePhone()) && StringUtils.isBlank(sysUserBean.getEmail())) {
            resultResponse.setMessage("手机号或邮箱地址至少一个不为空");
            LOGGER.error("手机号或邮箱地址至少一个不为空");
            return resultResponse;
        }
        //检查是否合法
        if (StringUtils.isNotBlank(sysUserBean.getMobilePhone()) && !RegexUtil.checkMobile(sysUserBean.getMobilePhone())) {
            resultResponse.setComment("请输入正确的手机号码");
            LOGGER.error("请输入正确的手机号码");
            return resultResponse;
        } else if (StringUtils.isNotBlank(sysUserBean.getEmail()) && !RegexUtil.checkEmail(sysUserBean.getEmail())) {
            resultResponse.setComment("请输入正确的邮箱地址");
            LOGGER.error("请输入正确的邮箱地址");
            return resultResponse;
        }
        //检查用户是否已存在
        SysUserBean sysUserBean1;
        if (StringUtils.isNotBlank(sysUserBean.getMobilePhone())) {
            sysUserBean1 = sysUserReaderMapper.selectByEmailOrPhone(sysUserBean.getMobilePhone(), sysUserBean.getId());
            if (sysUserBean1 != null) {
                resultResponse.setMessage("该手机号已被注册");
                LOGGER.error("[" + sysUserBean.getMobilePhone() + "]该手机号已被注册");
                return resultResponse;
            }
        } else if (StringUtils.isNotBlank(sysUserBean.getEmail())) {
            sysUserBean1 = sysUserReaderMapper.selectByEmailOrPhone(sysUserBean.getEmail(), sysUserBean.getId());
            if (sysUserBean1 != null) {
                resultResponse.setMessage("该邮箱已被注册");
                LOGGER.error("[" + sysUserBean.getEmail() + "]该邮箱已被注册");
                return resultResponse;
            }
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @param configPwd
     * @return
     */
    public ResultResponse updatePwd(Long userId, String oldPwd,
                                    String newPwd, String configPwd) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isBlank(oldPwd)) {
            throw new BusinessException("请输入原密码");
        } else if (StringUtils.isBlank(newPwd)) {
            throw new BusinessException("请输入新密码");
        } else if (StringUtils.isBlank(configPwd)) {
            throw new BusinessException("请输入确认密码");
        } else if (!StringUtils.equals(newPwd, configPwd)) {
            throw new BusinessException("新密码和确认密码不匹配");
        }
        //根据用户Id获取用户信息
        SysUserBean sysUserBean = sysUserReaderMapper.selectByPrimaryKey(userId);
        if (sysUserBean != null) {
            if (StringUtils.equals(MethodUtil.MD5(oldPwd + sysUserBean.getSalt()), sysUserBean.getPwd())) {
                sysUserBean.setSalt(RandomDataUtil.getRandomID());
                sysUserBean.setPwd(MethodUtil.MD5(newPwd + sysUserBean.getSalt()));
                int result = sysUserWriterMapper.updateByPrimaryKeySelective(sysUserBean);
                if (result > 0) {
                    resultResponse.setSuccess(true);
                } else {
                    throw new BusinessException("密码修改失败");
                }
            } else {
                throw new BusinessException("原密码输入错误");
            }
        } else {
            throw new BusinessException("用户不存在");
        }
        return resultResponse;
    }


}
