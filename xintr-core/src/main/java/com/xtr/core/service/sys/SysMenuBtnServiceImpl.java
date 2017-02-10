package com.xtr.core.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBtnBean;
import com.xtr.api.service.sys.SysMenuBtnService;
import com.xtr.core.persistence.reader.sys.SysMenuBtnReaderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 17:34
 */
@Service("sysMenuBtnService")
public class SysMenuBtnServiceImpl implements SysMenuBtnService {

    @Resource
    private SysMenuBtnReaderMapper sysMenuBtnReaderMapper;

    /**
     * 根据指定主键获取一条数据库记录,sys_menu_btn
     *
     * @param id
     */
    public ResultResponse selectByPrimaryKey(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        return resultResponse;
    }

    /**
     * 根据用户id查询按钮
     *
     * @param userId
     */
    public ResultResponse getMenuBtnByUser(Long userId) {
        ResultResponse resultResponse = new ResultResponse();
        if (userId != null) {
            List<SysMenuBtnBean> list = sysMenuBtnReaderMapper.getMenuBtnByUser(userId);
            resultResponse.setData(list);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("根据用户id查询按钮失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据菜单id获取按钮
     *
     * @param menuId
     * @return
     */
    public List<SysMenuBtnBean> getMenuBtnByMenuId(Long menuId) {
        return sysMenuBtnReaderMapper.getMenuBtnByMenuId(menuId);
    }

    /**
     * 查询所有按钮
     *
     * @return
     */
    public List<SysMenuBtnBean> queryByAll(){
        return sysMenuBtnReaderMapper.queryByAll();
    }
}
