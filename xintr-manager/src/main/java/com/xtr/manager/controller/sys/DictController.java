package com.xtr.manager.controller.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.TreeNode;
import com.xtr.api.basic.TreeState;
import com.xtr.api.domain.sys.DictionaryBean;
import com.xtr.api.domain.sys.DictionaryDataBean;
import com.xtr.api.service.sys.DictionaryService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.manager.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/1 18:37
 */
@Controller
@RequestMapping("dict")
public class DictController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictController.class);

    @Resource
    private DictionaryService dictionaryService;

    /**
     * 数据字典页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("dict.htm")
    public ModelAndView dict(ModelAndView mav) {
        mav.setViewName("xtr/sys/dict/dict");
        return mav;
    }

    /**
     * 获取字典树
     *
     * @return
     */
    @RequestMapping("getDictTree.htm")
    @ResponseBody
    public void getDictTree(HttpServletResponse response) {
        //获取顶级节点
        DictionaryBean dictionaryBean = dictionaryService.getParentDict();
        //获取所有节点
        List<DictionaryBean> list = dictionaryService.getDictionaryData();

        List<TreeNode> treeNodes = new ArrayList<>();
        TreeNode treeNode = new TreeNode();
        treeNode.setId(dictionaryBean.getId());
        treeNode.setText(dictionaryBean.getDictName());
        treeNode.setValue(dictionaryBean.getDictValue());
        TreeState treeState = new TreeState();
        treeState.setExpanded(false);
        treeNode.setState(treeState);
        treeNode = initTree(treeNode, dictionaryBean, list);
        treeNodes.add(treeNode);

        HtmlUtil.writerJson(response, JSON.toJSONString(treeNodes));
    }

    /**
     * 初始化树
     *
     * @return
     */
    private TreeNode initTree(TreeNode treeNode, DictionaryBean dictionaryBean, List<DictionaryBean> list) {
        if (!list.isEmpty()) {
            List<TreeNode> treeNodes = new ArrayList<>();
            for (DictionaryBean dictionaryBean1 : list) {
                if (dictionaryBean1.getParentId() == dictionaryBean.getId()) {
                    TreeNode treeNode1 = new TreeNode();
                    treeNode1.setId(dictionaryBean1.getId());
                    treeNode1.setText(dictionaryBean1.getDictName());
                    treeNode1.setValue(dictionaryBean1.getDictValue());
                    TreeState treeState = new TreeState();
                    treeState.setExpanded(false);
                    treeNode1.setState(treeState);
                    treeNodes.add(treeNode1);
                    initTree(treeNode1, dictionaryBean1, list);
                }
            }
            if (!treeNodes.isEmpty())
                treeNode.setNodes(treeNodes);
        }
        return treeNode;
    }

    /**
     * 加载列表数据
     *
     * @param dictionaryDataBean
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(DictionaryDataBean dictionaryDataBean) {
        if (dictionaryDataBean.getDictValue() == null) {
            //获取顶级节点
            DictionaryBean dictionaryBean = dictionaryService.getParentDict();
            dictionaryDataBean.setDictValue(dictionaryBean.getDictValue());
        }
        return dictionaryService.selectPageList(dictionaryDataBean);
    }

    /**
     * 新增/修改字典页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("add.htm")
    public ModelAndView add(ModelAndView mav, Long id, String dictValue) {
        if (id != null) {
            mav.addObject("dict", dictionaryService.selectByPrimaryKey(id));
        }
        mav.addObject("dictValue", dictValue);
        mav.setViewName("xtr/sys/dict/add");
        return mav;
    }

    /**
     * 新增/修改字典分类页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("addDictType.htm")
    public ModelAndView addDictType(ModelAndView mav, @RequestParam("id") Long id,
                                    @RequestParam("parentId") Long parentId) {
        if (id != null) {
            mav.addObject("dict", dictionaryService.selectDictByPrimaryKey(id));
        }
        mav.addObject("parentId", parentId);
        mav.setViewName("xtr/sys/dict/addDictType");
        return mav;
    }

    /**
     * 保存字典
     *
     * @param dictionaryDataBean
     * @return
     */
    @RequestMapping("save.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增/修改字典", modelName = "数据字典")
    public ResultResponse save(DictionaryDataBean dictionaryDataBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (dictionaryDataBean != null) {
            if (dictionaryDataBean.getId() == null) {
                resultResponse = dictionaryService.addDictData(dictionaryDataBean);
            } else {
                resultResponse = dictionaryService.updateDictData(dictionaryDataBean);
            }
        }
        return resultResponse;
    }

    /**
     * 新增字典分类
     *
     * @param dictionaryBean
     * @return
     */
    @RequestMapping("addDict.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增/修改字典分类", modelName = "数据字典")
    public ResultResponse addDict(DictionaryBean dictionaryBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (dictionaryBean != null) {
            if (dictionaryBean.getId() == null) {
                resultResponse = dictionaryService.addDict(dictionaryBean);
            } else {
                resultResponse = dictionaryService.updateDict(dictionaryBean);
            }
        }
        return resultResponse;
    }

    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    @RequestMapping("delete.htm")
    @ResponseBody
    @SystemControllerLog(operation = "删除字典", modelName = "数据字典")
    public ResultResponse delete(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        dictionaryService.deleteDictData(id);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 删除字典分类
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteDict.htm")
    @ResponseBody
    @SystemControllerLog(operation = "删除字典分类", modelName = "数据字典")
    public ResultResponse deleteDict(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        dictionaryService.deleteDict(id);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 根据关键字获取数据字典
     *
     * @param dictValue
     * @return
     */
    @RequestMapping("getDictData.htm")
    @ResponseBody
    public ResultResponse getDictData(String dictValue) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isNotBlank(dictValue)) {
            resultResponse.setData(dictionaryService.selectByDictValue(dictValue));
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 根据字典关键字获取值
     *
     * @param dictDataValue
     * @return
     */
    @RequestMapping("getDictDataName.htm")
    @ResponseBody
    public ResultResponse getDictDataName(@RequestParam("dictValue") String dictValue, @RequestParam("dictDataValue") String dictDataValue) {
        if (StringUtils.isNotBlank(dictDataValue) && StringUtils.isNotBlank(dictValue)) {
            DictionaryDataBean dictionaryDataBean = new DictionaryDataBean();
            dictionaryDataBean.setDictValue(dictValue);
            dictionaryDataBean.setDictdataValue(dictDataValue);
            return dictionaryService.selectPageList(dictionaryDataBean);
        }
        return new ResultResponse();
    }
}
