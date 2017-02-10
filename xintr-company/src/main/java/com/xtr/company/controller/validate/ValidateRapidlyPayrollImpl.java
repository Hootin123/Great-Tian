package com.xtr.company.controller.validate;

import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.validate.ValidateService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>快速发工资验证</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/11/1 9:40
 */
@Service("rapidlyPayrollValidate")
public class ValidateRapidlyPayrollImpl implements ValidateService {

    @Resource
    private BankCodeService bankCodeService;


    /**
     * 非空验证
     *
     * @param obj
     * @throws Exception
     */
    public void validate(Object obj) throws Exception {
        List<Map<String, String>> list = (List<Map<String, String>>) obj;
        if (list != null && !list.isEmpty()) {
            //文件读取开始行
            int startLine = Integer.valueOf(PropertyUtils.getString("company.readLine"));
            StringBuffer strError = new StringBuffer();
            StringBuffer rowError = new StringBuffer();
            for (int i = 1; i <= list.size(); i++) {
                Map<String, String> map = list.get(i - 1);
                //姓名
                if (StringUtils.isBlank(map.get("A"))) {
                    rowError.append("姓名不能为空");
                }
                //身份证号
                if (StringUtils.isBlank(map.get("B"))) {
                    rowError.append(rowError.length() > 0 ? ",身份证号不能为空" : "身份证号不能为空");
                } else if (!RegexUtil.checkIdCard(map.get("B"))) {
                    rowError.append(rowError.length() > 0 ? ",身份证号【" + map.get("B") + "】格式不正确" : "身份证号【" + map.get("B") + "】格式不正确");
                }
                //工资卡开户行
                if (StringUtils.isBlank(map.get("C"))) {
                    rowError.append(rowError.length() > 0 ? ",工资卡开户行不能为空" : "工资卡开户行不能为空");
                } else {
                    if (bankCodeService.selectByBankName(map.get("C")) == null) {
                        rowError.append(rowError.length() > 0 ? ",工资卡开户行【" + map.get("C") + "】不存在" : "工资卡开户行【" + map.get("C") + "】不存在");
                    }
                }
                //工资卡号
                if (StringUtils.isBlank(map.get("D"))) {
                    rowError.append(rowError.length() > 0 ? ",工资卡号不能为空" : "工资卡号不能为空");
                } else if (!RegexUtil.checkBankNumber(map.get("D"))) {
                    rowError.append(rowError.length() > 0 ? ",工资卡号【" + map.get("D") + "】格式不正确" : "工资卡号【" + map.get("D") + "】格式不正确");
                }
                //实发金额(元)
                if (StringUtils.isBlank(map.get("E"))) {
                    rowError.append(rowError.length() > 0 ? ",实发金额不能为空" : "实发金额不能为空");
                } else if (!com.xtr.comm.util.StringUtils.isNum(map.get("E"))) {
                    rowError.append(rowError.length() > 0 ? ",实发金额【" + map.get("E") + "】格式不正确" : "实发金额【" + map.get("E") + "】格式不正确");
                }

                if (rowError.length() > 0) {
                    rowError.insert(0, "第" + (startLine + i) + "行");
                    strError.append(rowError + "<br/>");
                    rowError.delete(0, rowError.length());
                }
            }

            if (strError.length() > 0) {
                throw new BusinessException(strError.toString());
            }
        } else {
            throw new BusinessException("请添加工资单数据");
        }
    }
}
