package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.company.CompanyActivityBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.company.CompanysDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanysReaderMapper {


    /**
     *  根据指定主键获取一条数据库记录,companys
     *
     * @param companyId
     */
    CompanysBean selectByPrimaryKey(Long companyId);

    /**
     *  根据指定主键获取一条数据库记录,companys
     *
     * @param companyNumber
     */
    CompanysBean selectCompanyByCompanyNumber(String companyNumber);

    /**
     * 根据公司名称验证该公司是否存在
     *
     * @param companyName
     * @return
     */
    List<CompanysBean> checkCompanyByName(String companyName);

    /**
     * 根据过滤条件查询企业信息(分页)
     * @param companysDto
     * @return
     */
    List<CompanysDto> selectCompanyInfoPageList(CompanysDto companysDto, PageBounds pageBounds);

    /**
     * 根据过滤条件获取员工信息(优化信息)
     * @param companysDto
     * @param pageBounds
     * @return
     */
    List<CompanysDto> selectCompanyInfoBetter(CompanysDto companysDto, PageBounds pageBounds);
    /**
     * 根据过滤条件查询企业信息
     * @param companysDto
     * @return
     */
    List<CompanysDto> selectCompanyInfoPageList(CompanysDto companysDto);

    /**
     * 根据企业编号获取企业信息
     * @param companyId
     * @return
     */
    List<CompanysDto> selectCompanyInfoDetail(long companyId);

    /**
     * 查询
     * @param param
     * @return
     */
    Map<String,Object> find(Map<String, Object> param);

    /**
     * 分页
     * @param param
     * @return
     */
    List<Map<String,Object>> findListPage(Map<String, Object> param);

    /**
     * 总数
     * @param param
     * @return
     */
    int findListPageCount(Map<String, Object> param);


    /**
     * 查询
     * @param map
     * @return
     */
    Map<String,Object> stationCollaborationFind(Map<String, Object> map);

    /**
     * 查询资源文件
     * @param map
     * @return
     */
    List<Map<String,Object>> getfileResources(Map<String, Object> map);

    /**
     * 增加新闻点击次数
     * @param nId
     * @param count
     * @return
     */
    int stationNewsClickAdd(@Param("nId") long nId, @Param("nCount") int count);

    /**
     * 查询资源文件类型
     * @param map
     * @return
     */
    List<Map<String,Object>> getfileResourcesByFileType(Map<String, Object> map);

    /**
     * 查询垫付列表
     * @param datumStatus
     * @param companyIsauth
     * @param kw
     * @return
     */
    PageList<Map<String,Object>> selectPrepaidPage(@Param("datumStatus") Integer datumStatus, @Param("companyIsauth") Integer companyIsauth, @Param("kw") String kw, PageBounds pageBounds);

    /**
     *查询当前领取红包的hr人数
     * @return
     */
    long selectActivityRedCounts();

    /**
     * 查询领取完善信息红包的人数
     * @return
     */
    long selectselectCountsOfPerfectInfo();

    /**
     * 查询当前登录账号已领取的红包金额
     * @param memberId
     * @return
     */
    long selectHasReceiveAccount(Long memberId);

    /**
     * 查询当前用户的领取的5元或10元红包
     * @param params
     * @return
     */
    CompanyActivityBean selectReds(Map<String, Object> params);

    /**
     * 根据红包类型获取红包订单量
     *
     * @return
     */
    Integer getTodayWechatOrderNum(@Param("activityReceive") Integer activityReceive);

    /**
     * 获取企业总数
     * @param companysDto
     * @return
     */
    Integer selectCountForCompanyList(CompanysDto companysDto);

    /**
     * 查询该公司的入职须知
     * @param wechatCompanyId
     * @return
     */
    CompanysBean selectCompanyEnterRequireById(Long wechatCompanyId);

    /**
     *根据企业ID批量获取信息
     * @param list
     * @return
     */
    List<Map<String,Object>> selectByPrimaryKeyBatch(@Param("list") List<Long> list);
}