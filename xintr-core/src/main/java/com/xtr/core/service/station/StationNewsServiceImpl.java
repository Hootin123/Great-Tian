package com.xtr.core.service.station;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.*;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.station.StationNewsBean;
import com.xtr.api.service.station.StationNewsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.station.StationNewsReaderMapper;
import com.xtr.core.persistence.writer.station.StationNewsWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/18 11:32
 */
@Service("stationNewsService")
public class StationNewsServiceImpl implements StationNewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationNewsService.class);

    @Resource
    private StationNewsWriterMapper stationNewsWriterMapper;

    @Resource
    private StationNewsReaderMapper stationNewsReaderMapper;

    /**
     * 根据新闻id查询新闻
     *
     * @param newId
     * @return
     */
    @Override
    public StationNewsBean selecyByNewId(Long newId) {
        if(null != newId){
            return stationNewsReaderMapper.selectByPrimaryKey(newId);
        }
        return null;
    }

    /**
     * 保存新闻
     *
     * @param stationNewsBean
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStationNews(StationNewsBean stationNewsBean) throws BusinessException {

        LOGGER.info("接受参数：" + JSON.toJSONString(stationNewsBean));

        try {
            if(null == stationNewsBean){
                throw new BusinessException("新闻参数为空");
            }

            if(StringUtils.isBlank(stationNewsBean.getNewsTitle())){
                throw new BusinessException("新闻标题为空");
            }

            if(StringUtils.isBlank(stationNewsBean.getNewsClassName())){
                throw new BusinessException("子类型名称为空");
            }

            if(StringUtils.isBlank(stationNewsBean.getNewsImg())){
                throw new BusinessException("新闻缩略图为空");
            }

            if(StringUtils.isBlank(stationNewsBean.getNewsContent())){
                throw new BusinessException("新闻内容为空");
            }

            if(null == stationNewsBean.getNewsType()){
                throw new BusinessException("新闻类型为空");
            }

            if(null == stationNewsBean.getNewsReleasetime()){
                throw new BusinessException("新闻发布时间为空");
            }

            if(null == stationNewsBean.getNewsAddMember()){
                throw new BusinessException("新闻发布人为空");
            }

            // 添加时间
            stationNewsBean.setNewsAddtime(new Date());
            // 点击次数
            stationNewsBean.setNewsChick(0);
            stationNewsBean.setNewsClassId(0);

            String content = stationNewsBean.getNewsContent();

            // 抓取120个字符作为摘要
            if(StringUtils.isBlank(stationNewsBean.getNewsIntro())){
                if(content.length() > 120){
                    stationNewsBean.setNewsIntro(content.substring(0, 120));
                } else {
                    stationNewsBean.setNewsIntro(content);
                }
            }

            stationNewsBean.setNewsState(0);

            stationNewsWriterMapper.insert(stationNewsBean);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 修改新闻
     *
     * @param stationNewsBean
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStationNews(StationNewsBean stationNewsBean) throws BusinessException {
        if(null == stationNewsBean){
            throw new BusinessException("新闻参数为空");
        }

        if(null == stationNewsBean.getNewsId()){
            throw new BusinessException("新闻id为空");
        }

        if(StringUtils.isBlank(stationNewsBean.getNewsTitle())){
            throw new BusinessException("新闻标题为空");
        }

        if(StringUtils.isBlank(stationNewsBean.getNewsImg())){
            throw new BusinessException("新闻缩略图为空");
        }

        if(StringUtils.isBlank(stationNewsBean.getNewsContent())){
            throw new BusinessException("新闻内容为空");
        }

        if(null == stationNewsBean.getNewsType()){
            throw new BusinessException("新闻类型为空");
        }

        if(null == stationNewsBean.getNewsReleasetime()){
            throw new BusinessException("新闻发布时间为空");
        }

        String content = stationNewsBean.getNewsContent();

        // 抓取120个字符作为摘要
        if(StringUtils.isBlank(stationNewsBean.getNewsIntro())){
            if(content.length() > 120){
                stationNewsBean.setNewsIntro(content.substring(0, 120));
            } else {
                stationNewsBean.setNewsIntro(content);
            }
        }

        stationNewsWriterMapper.updateByPrimaryKeySelective(stationNewsBean);
    }

    /**
     * 查询新闻列表
     *
     * @param newsType
     * @param newsState
     * @return
     */
    @Override
    public List<StationNewsBean> selectList(Integer newsType, Integer newsState) {
        StationNewsBean stationNewsBean = new StationNewsBean();
        stationNewsBean.setNewsType(newsType);
        stationNewsBean.setNewsState(newsState);
        return stationNewsReaderMapper.selectList(stationNewsBean);
    }

    /**
     * 分页查询新闻列表
     *
     * @param newsType
     * @param newsState
     * @param newsTitle
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultResponse selectPageList(Integer newsType, Integer newsState, String newsTitle, int pageIndex, int pageSize) {

        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(pageIndex, pageSize, Order.formString("news_addtime.desc"));

        StationNewsBean stationNewsBean = new StationNewsBean();
        stationNewsBean.setNewsType(newsType);
        stationNewsBean.setNewsState(newsState);
        stationNewsBean.setNewsTitle(newsTitle);

        PageList<StationNewsBean> list = stationNewsReaderMapper.selectPageList(stationNewsBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 修改新闻发布状态
     *
     * @param newsId
     * @param state
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void republish(Long newsId, Integer state) throws BusinessException {
        if(null == newsId){
            throw new BusinessException("新闻id为空");
        }
        if(null == state){
            throw new BusinessException("新闻发布状态为空");
        }

        StationNewsBean stationNewsBean = new StationNewsBean();
        stationNewsBean.setNewsId(newsId);
        stationNewsBean.setNewsState(state);
        try {
            stationNewsWriterMapper.updateByPrimaryKeySelective(stationNewsBean);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private String getIntroImage(String content){
        String newsImg = "";
        // 有图片
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img,Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(content);
        if(m_image.find()){
            String img = m_image.group(0);
            Matcher m  = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            if(m.find()){
                if(m.group(0)!=null){
                    newsImg = m.group(0).substring(5, m.group(0).length()-1);
                }
            }
        }
        return newsImg;
    }
}
