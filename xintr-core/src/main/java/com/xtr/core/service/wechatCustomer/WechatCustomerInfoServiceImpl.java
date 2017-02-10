package com.xtr.core.service.wechatCustomer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersPersonalBean;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.customer.CustomersPersonalService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.wechatCustomer.WechatCustomerInfoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/9/6 9:14
 */
@Service("wechatCustomerInfoService")
public class WechatCustomerInfoServiceImpl implements WechatCustomerInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatCustomerInfoServiceImpl.class);

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomersPersonalService customersPersonalService;

    @Resource
    private BankCodeService bankCodeService;

    /**
     * 员工修改基本信息
     * @param customersBean
     * @param customersPersonalBean
     * @throws BusinessException
     */
    public void modifyMainInfo(CustomersBean customersBean, CustomersPersonalBean customersPersonalBean)throws BusinessException{
        //修改员工基本信息相关验证
        validatorBaseInfo( customersBean, customersPersonalBean);

        //更新员工基本信息
        //处理更新内容由有到无
        if(StringUtils.isStrNull(customersBean.getCustomerEnglishName())){//英文名
            customersBean.setCustomerEnglishName("");
        }
        if(StringUtils.isStrNull(customersBean.getCustomerMarriage())){//婚姻状况
            customersBean.setCustomerMarriage("");
        }
        //员工补全资料
        customersBean.setCustomerIsComplement(CustomerConstants.CUSTOMER_ISCOMPLEMENT_YES);
        //工资卡卡号去掉空格
        customersBean.setCustomerBanknumber(customersBean.getCustomerBanknumber().replaceAll(" ",""));
        int baseCount=customersService.updateByPrimaryKeySelective(customersBean);
        if(baseCount<=0){
            throw new BusinessException("修改基本信息异常");
        }else{
            //更新员工个人信息,如果无员工个人信息,则新增员工个人信息
            if(customersPersonalBean.getPersonalId()!=null){
                customersPersonalBean.setPersonalUpdateTime(new Date());
                int personalCount=customersPersonalService.updateByPrimaryKeySelective(customersPersonalBean);
                if(personalCount<=0){
                    throw new BusinessException("修改个人信息异常");
                }
            }else{
                customersPersonalBean.setPersonalCustomerId(customersBean.getCustomerId());
                customersPersonalBean.setPersonalCreateTime(new Date());
                int countPersonal = customersPersonalService.insert(customersPersonalBean);
                if (countPersonal <= 0) {
                    throw new BusinessException("新增个人信息异常");
                }
            }

        }
    }

    /**
     * 修改员工基本信息数据验证
     * @param customersBean
     * @param customersPersonalBean
     */
    private void validatorBaseInfo(CustomersBean customersBean,CustomersPersonalBean customersPersonalBean)throws BusinessException{
        //非空验证
        validatorNull(customersBean,customersPersonalBean);
        //其它各自验证
        validatorOther(customersBean,customersPersonalBean);
    }

    /**
     * 修改员工基本信息非空验证
     * @param customersBean
     * @param customersPersonalBean
     */
    private void validatorNull(CustomersBean customersBean, CustomersPersonalBean customersPersonalBean)throws BusinessException{
        if(customersBean!=null && customersPersonalBean!=null && customersBean.getCustomerId()!=null){
            if(customersBean.getCustomerSex()==null){
                throw new BusinessException("请选择性别");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerBirthdayMonth())){
                throw new BusinessException("请选择生日");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerBank())){
                throw new BusinessException("请输入工资卡开户行");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerBanknumber())){
                throw new BusinessException("请输入工资卡卡号");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerBirthdayMonth())){
                throw new BusinessException("请选择性别");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerIdcard())){
                throw new BusinessException("请输入身份证号");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerIdcardImgFront())){
                throw new BusinessException("请选择身份证正面图片");
            }
            if(StringUtils.isStrNull(customersBean.getCustomerIdcardImgBack())){
                throw new BusinessException("请选择身份证反面图片");
            }
            if(StringUtils.isStrNull(customersPersonalBean.getPersonalDomicilePlace())){
                throw new BusinessException("请输入户籍所在地");
            }
            if(StringUtils.isStrNull(customersPersonalBean.getPersonalRelationerName())){
                throw new BusinessException("请输入紧急联系人");
            }
            if(StringUtils.isStrNull(customersPersonalBean.getPersonalRelationerPhone())){
                throw new BusinessException("请输入紧急联系人电话");
            }
        }else{
            throw new BusinessException("请输入相关内容");
        }
    }

    /**
     * 员工基本信息修改其它验证
     * @param customersBean
     * @param customersPersonalBean
     */
    private void validatorOther(CustomersBean customersBean, CustomersPersonalBean customersPersonalBean)throws BusinessException{
        //英文名不可输入中文,最多50个字符
        if(!StringUtils.isStrNull(customersBean.getCustomerEnglishName())){
            if(customersBean.getCustomerEnglishName().length()>50){
                throw new BusinessException("英文名最多50个字符");
            }
            String regexEnglistName = "^[\\u4E00-\\u9FA5]+$";
            if(Pattern.matches(regexEnglistName, customersBean.getCustomerEnglishName())){
                throw new BusinessException("英文名不可输入中文");
            }
        }
        //生日,格式为YYYY-MM-DD
        String regexBirthdayTime = "^([0-9]{4}[-]{1}([0-9]){1,2}[-]{1}([0-9]){1,2})$";
        if(!Pattern.matches(regexBirthdayTime, customersBean.getCustomerBirthdayMonth())){
            throw new BusinessException("生日格式不正确");
        }
        //工资卡卡号,去掉空格(包括中间的空格),16~19位数字
        String bankNumber=customersBean.getCustomerBanknumber().replaceAll(" ","");
        if(bankNumber.length()>19 || bankNumber.length()<16){
            throw new BusinessException("工资卡卡号请输入16~19位数字");
        }
        //工资卡开户行,最多30个字符,并符合京东bankcode规范
        if(customersBean.getCustomerBank().length()>30){
            throw new BusinessException("工资卡开户行最多30个字符");
        }
        BankCodeBean bankCodeBean= bankCodeService.selectByBankName(customersBean.getCustomerBank());
        if(bankCodeBean==null){
            throw new BusinessException("银行名称不符合规范");
        }
        //户籍所在地,最多50个字符
        if(customersPersonalBean.getPersonalDomicilePlace().length()>50){
            throw new BusinessException("请输入正确的户籍所在地");
        }
        //紧急联系人,最多30个字符
        if(customersPersonalBean.getPersonalRelationerName().length()>30){
            throw new BusinessException("请输入正确的紧急联系人");
        }
        //紧急联系人电话,7~20个数字
        if(customersPersonalBean.getPersonalRelationerPhone().length()>20 || customersPersonalBean.getPersonalRelationerPhone().length()<7){
            throw new BusinessException("请输入正确的紧急联系人电话");
        }
        //身份证号,15或18个字符
        if(customersBean.getCustomerIdcard().length()!=15 && customersBean.getCustomerIdcard().length()!=18){
            throw new BusinessException("请输入正确的身份证号");
        }

    }

    /**
     * 员工编辑三大信息中的基本信息
     * @param customersBean
     * @throws BusinessException
     */
    public void modifyEditBaseInfo(CustomersBean customersBean)throws BusinessException{
        //员工编辑三大信息中的基本信息相关验证
        validatorEditBaseInfo(customersBean);

        //更新员工基本信息
        //处理更新内容由有到无
        if(StringUtils.isStrNull(customersBean.getCustomerEnglishName())){//英文名
            customersBean.setCustomerEnglishName("");
        }
        if(StringUtils.isStrNull(customersBean.getCustomerMarriage())){//婚姻状况
            customersBean.setCustomerMarriage("");
        }
        if (customersBean.getCustomerAge() == null) {//年龄
            customersBean.setCustomerAge(0);
        }
        int baseCount=customersService.updateByPrimaryKeySelective(customersBean);
        if(baseCount<=0){
            throw new BusinessException("修改基本信息异常");
        }
    }

    /**
     * 员工编辑三大信息中的基本信息数据验证
     * @param customersBean
     */
    private void validatorEditBaseInfo(CustomersBean customersBean)throws BusinessException{
        if(customersBean!=null && customersBean.getCustomerId()!=null){
            if(customersBean.getCustomerSex()==null){
                throw new BusinessException("请选择性别");
            }
            //英文名不可输入中文,最多50个字符
            if(!StringUtils.isStrNull(customersBean.getCustomerEnglishName())){
                if(customersBean.getCustomerEnglishName().length()>50){
                    throw new BusinessException("英文名最多50个字符");
                }
                String regexEnglistName = "^[\\u4E00-\\u9FA5]+$";
                if(Pattern.matches(regexEnglistName, customersBean.getCustomerEnglishName())){
                    throw new BusinessException("英文名不可输入中文");
                }
            }
//            //生日,格式为YYYY-MM-DD
//            String regexBirthdayTime = "^([0-9]{4}[-]{1}([0-9]){1,2}[-]{1}([0-9]){1,2})$";
//            if(!Pattern.matches(regexBirthdayTime, customersBean.getCustomerBirthdayMonth())){
//                throw new BusinessException("生日格式不正确");
//            }
        }else{
            throw new BusinessException("请输入相关内容");
        }
    }

    /**
     * 员工编辑三大信息中的个人信息
     * @param customersPersonalBean
     * @throws BusinessException
     */
    public void modifyEditPersonalInfo(CustomersPersonalBean customersPersonalBean)throws BusinessException{
        //员工编辑三大信息中的基本信息相关验证
        validatorEditPersonalInfo(customersPersonalBean);

        if(customersPersonalBean!=null){
            if(customersPersonalBean.getPersonalId()!=null) {//更改个人信息
                customersPersonalBean.setPersonalUpdateTime(new Date());
                //处理更新内容由有到无
                if (StringUtils.isStrNull(customersPersonalBean.getPersonalNation())) {
                    customersPersonalBean.setPersonalNation("");
                }
                if (StringUtils.isStrNull(customersPersonalBean.getPersonalPoliticsStatus())) {
                    customersPersonalBean.setPersonalPoliticsStatus("");
                }
                if (StringUtils.isStrNull(customersPersonalBean.getPersonalCurrentPlace())) {
                    customersPersonalBean.setPersonalCurrentPlace("");
                }
                if (StringUtils.isStrNull(customersPersonalBean.getPersonalGraduateSchool())) {
                    customersPersonalBean.setPersonalGraduateSchool("");
                }
                if (StringUtils.isStrNull(customersPersonalBean.getPersonalMajor())) {
                    customersPersonalBean.setPersonalMajor("");
                }
                if (customersPersonalBean.getPersonalEducationHigh() == null) {
                    customersPersonalBean.setPersonalEducationHigh(0);
                }
                int countPersonal = customersPersonalService.updateByPrimaryKeySelective(customersPersonalBean);
                if (countPersonal <= 0) {
                    throw new BusinessException("更新用户个人信息失败");
                }
            }else{//新增个人信息
                customersPersonalBean.setPersonalCreateTime(new Date());
                int countPersonal = customersPersonalService.insert(customersPersonalBean);
                if (countPersonal <= 0) {
                    throw new BusinessException("新增用户个人信息失败");
                }
            }
        }

    }

    /**
     * 员工编辑三大信息中的个人信息数据验证
     * @param customersPersonalBean
     * @throws BusinessException
     */
    private void validatorEditPersonalInfo(CustomersPersonalBean customersPersonalBean)throws BusinessException {

        //非空验证
        if (customersPersonalBean == null) {
            throw new BusinessException("请输入个人信息");
        }

        if (StringUtils.isStrNull(customersPersonalBean.getPersonalDomicilePlace())) {
            throw new BusinessException("请输入户籍所在地");
        }
        if (StringUtils.isStrNull(customersPersonalBean.getPersonalRelationerName())) {
            throw new BusinessException("请输入紧急联系人");
        }
        if (StringUtils.isStrNull(customersPersonalBean.getPersonalRelationerPhone())) {
            throw new BusinessException("请输入紧急联系人电话");
        }
        //其它数据验证
        //户籍所在地,最多50个字符
        if (customersPersonalBean.getPersonalDomicilePlace().length() > 50) {
            throw new BusinessException("请输入正确的户籍所在地");
        }
        //紧急联系人,最多30个字符
        if (customersPersonalBean.getPersonalRelationerName().length() > 30) {
            throw new BusinessException("请输入正确的紧急联系人");
        }
        //紧急联系人电话,7~20个数字
        if (customersPersonalBean.getPersonalRelationerPhone().length() > 20 || customersPersonalBean.getPersonalRelationerPhone().length() < 7) {
            throw new BusinessException("请输入正确的紧急联系人电话");
        }
        //民族最多20个字符
        if (!StringUtils.isStrNull(customersPersonalBean.getPersonalNation()) && customersPersonalBean.getPersonalNation().length() > 20) {
            throw new BusinessException("民族最多20个字符");
        }
        //政治面貌最多20个字符
        if (!StringUtils.isStrNull(customersPersonalBean.getPersonalPoliticsStatus()) && customersPersonalBean.getPersonalPoliticsStatus().length() > 20) {
            throw new BusinessException("政治面貌最多20个字符");
        }
        //现居住地最多50个字符
        if (!StringUtils.isStrNull(customersPersonalBean.getPersonalCurrentPlace()) && customersPersonalBean.getPersonalCurrentPlace().length() > 50) {
            throw new BusinessException("现居住地最多50个字符");
        }
        //毕业学校最多50个字符
        if (!StringUtils.isStrNull(customersPersonalBean.getPersonalGraduateSchool()) && customersPersonalBean.getPersonalGraduateSchool().length() > 50) {
            throw new BusinessException("毕业学校最多50个字符");
        }
        //专业最多50个字符
        if (!StringUtils.isStrNull(customersPersonalBean.getPersonalMajor()) && customersPersonalBean.getPersonalMajor().length() > 50) {
            throw new BusinessException("专业最多50个字符");
        }
    }
}
