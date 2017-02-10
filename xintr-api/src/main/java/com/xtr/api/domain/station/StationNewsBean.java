package com.xtr.api.domain.station;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class StationNewsBean extends BaseObject implements Serializable {
    /**
     *  id,所属表字段为station_news.news_id
     */
    private Long newsId;

    /**
     *  标题,所属表字段为station_news.news_title
     */
    private String newsTitle;

    /**
     *  创建时间,所属表字段为station_news.news_addtime
     */
    private Date newsAddtime;

    /**
     *  创建人,所属表字段为station_news.news_add_member
     */
    private Long newsAddMember;

    /**
     *  发布时间,所属表字段为station_news.news_releasetime
     */
    private Date newsReleasetime;

    /**
     *  发布人,所属表字段为station_news.news_release_member
     */
    private Long newsReleaseMember;

    /**
     *  点击次数,所属表字段为station_news.news_chick
     */
    private Integer newsChick;

    /**
     *  排序,所属表字段为station_news.news_sort
     */
    private Integer newsSort;

    /**
     *  发布类型(1.系统公告 2.系统通知单 3.媒体报道 4.平台活动 5.HR知识库 6.政策薪解 7.动态推送),所属表字段为station_news.news_type
     */
    private Integer newsType;

    /**
     *  ,所属表字段为station_news.news_class_id
     */
    private Integer newsClassId;

    /**
     *  ,所属表字段为station_news.news_class_name
     */
    private String newsClassName;

    /**
     *  简介,所属表字段为station_news.news_intro
     */
    private String newsIntro;

    /**
     *  新闻三要素关键字,所属表字段为station_news.news_keywords
     */
    private String newsKeywords;

    /**
     *  新闻三要素简介
     */
    private String newsDescription;

    /**
     *  来源,所属表字段为station_news.news_ly
     */
    private String newsLy;

    /**
     *  ,所属表字段为station_news.news_img
     */
    private String newsImg;

    /**
     *  ,所属表字段为station_news.news_mtname
     */
    private String newsMtname;

    /**
     *  ,所属表字段为station_news.news_url
     */
    private String newsUrl;

    /**
     *  0待发布 1已发布 2已删除,所属表字段为station_news.news_state
     */
    private Integer newsState;

    /**
     *  是否置顶(0.不置顶 1.置顶),所属表字段为station_news.news_istop
     */
    private Integer newsIstop;

    /**
     *  内容,所属表字段为station_news.news_content
     */
    private String newsContent;

    /**
     *  获取 id 字段:station_news.news_id 
     *
     *  @return station_news.news_id, id
     */
    public Long getNewsId() {
        return newsId;
    }

    /**
     *  设置 id 字段:station_news.news_id 
     *
     *  @param newsId station_news.news_id, id
     */
    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    /**
     *  获取 标题 字段:station_news.news_title 
     *
     *  @return station_news.news_title, 标题
     */
    public String getNewsTitle() {
        return newsTitle;
    }

    /**
     *  设置 标题 字段:station_news.news_title 
     *
     *  @param newsTitle station_news.news_title, 标题
     */
    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle == null ? null : newsTitle.trim();
    }

    /**
     *  获取 创建时间 字段:station_news.news_addtime 
     *
     *  @return station_news.news_addtime, 创建时间
     */
    public Date getNewsAddtime() {
        return newsAddtime;
    }

    /**
     *  设置 创建时间 字段:station_news.news_addtime 
     *
     *  @param newsAddtime station_news.news_addtime, 创建时间
     */
    public void setNewsAddtime(Date newsAddtime) {
        this.newsAddtime = newsAddtime;
    }

    /**
     *  获取 创建人 字段:station_news.news_add_member 
     *
     *  @return station_news.news_add_member, 创建人
     */
    public Long getNewsAddMember() {
        return newsAddMember;
    }

    /**
     *  设置 创建人 字段:station_news.news_add_member 
     *
     *  @param newsAddMember station_news.news_add_member, 创建人
     */
    public void setNewsAddMember(Long newsAddMember) {
        this.newsAddMember = newsAddMember;
    }

    /**
     *  获取 发布时间 字段:station_news.news_releasetime 
     *
     *  @return station_news.news_releasetime, 发布时间
     */
    public Date getNewsReleasetime() {
        return newsReleasetime;
    }

    /**
     *  设置 发布时间 字段:station_news.news_releasetime 
     *
     *  @param newsReleasetime station_news.news_releasetime, 发布时间
     */
    public void setNewsReleasetime(Date newsReleasetime) {
        this.newsReleasetime = newsReleasetime;
    }

    /**
     *  获取 发布人 字段:station_news.news_release_member 
     *
     *  @return station_news.news_release_member, 发布人
     */
    public Long getNewsReleaseMember() {
        return newsReleaseMember;
    }

    /**
     *  设置 发布人 字段:station_news.news_release_member 
     *
     *  @param newsReleaseMember station_news.news_release_member, 发布人
     */
    public void setNewsReleaseMember(Long newsReleaseMember) {
        this.newsReleaseMember = newsReleaseMember;
    }

    /**
     *  获取 点击次数 字段:station_news.news_chick 
     *
     *  @return station_news.news_chick, 点击次数
     */
    public Integer getNewsChick() {
        return newsChick;
    }

    /**
     *  设置 点击次数 字段:station_news.news_chick 
     *
     *  @param newsChick station_news.news_chick, 点击次数
     */
    public void setNewsChick(Integer newsChick) {
        this.newsChick = newsChick;
    }

    /**
     *  获取 排序 字段:station_news.news_sort 
     *
     *  @return station_news.news_sort, 排序
     */
    public Integer getNewsSort() {
        return newsSort;
    }

    /**
     *  设置 排序 字段:station_news.news_sort 
     *
     *  @param newsSort station_news.news_sort, 排序
     */
    public void setNewsSort(Integer newsSort) {
        this.newsSort = newsSort;
    }

    /**
     *  获取 发布类型(1.系统公告 2.系统通知单 3.媒体报道 4.平台活动 5.HR知识库 6.政策薪解 7.动态推送) 字段:station_news.news_type 
     *
     *  @return station_news.news_type, 发布类型(1.系统公告 2.系统通知单 3.媒体报道 4.平台活动 5.HR知识库 6.政策薪解 7.动态推送)
     */
    public Integer getNewsType() {
        return newsType;
    }

    /**
     *  设置 发布类型(1.系统公告 2.系统通知单 3.媒体报道 4.平台活动 5.HR知识库 6.政策薪解 7.动态推送) 字段:station_news.news_type 
     *
     *  @param newsType station_news.news_type, 发布类型(1.系统公告 2.系统通知单 3.媒体报道 4.平台活动 5.HR知识库 6.政策薪解 7.动态推送)
     */
    public void setNewsType(Integer newsType) {
        this.newsType = newsType;
    }

    /**
     *  获取  字段:station_news.news_class_id 
     *
     *  @return station_news.news_class_id, 
     */
    public Integer getNewsClassId() {
        return newsClassId;
    }

    /**
     *  设置  字段:station_news.news_class_id 
     *
     *  @param newsClassId station_news.news_class_id, 
     */
    public void setNewsClassId(Integer newsClassId) {
        this.newsClassId = newsClassId;
    }

    /**
     *  获取  字段:station_news.news_class_name 
     *
     *  @return station_news.news_class_name, 
     */
    public String getNewsClassName() {
        return newsClassName;
    }

    /**
     *  设置  字段:station_news.news_class_name 
     *
     *  @param newsClassName station_news.news_class_name, 
     */
    public void setNewsClassName(String newsClassName) {
        this.newsClassName = newsClassName == null ? null : newsClassName.trim();
    }

    /**
     *  获取 简介 字段:station_news.news_intro 
     *
     *  @return station_news.news_intro, 简介
     */
    public String getNewsIntro() {
        return newsIntro;
    }

    /**
     *  设置 简介 字段:station_news.news_intro 
     *
     *  @param newsIntro station_news.news_intro, 简介
     */
    public void setNewsIntro(String newsIntro) {
        this.newsIntro = newsIntro == null ? null : newsIntro.trim();
    }

    /**
     *  获取 关键词 字段:station_news.news_keywords 
     *
     *  @return station_news.news_keywords, 关键词
     */
    public String getNewsKeywords() {
        return newsKeywords;
    }

    /**
     *  设置 关键词 字段:station_news.news_keywords 
     *
     *  @param newsKeywords station_news.news_keywords, 关键词
     */
    public void setNewsKeywords(String newsKeywords) {
        this.newsKeywords = newsKeywords == null ? null : newsKeywords.trim();
    }

    /**
     *  获取 来源 字段:station_news.news_ly 
     *
     *  @return station_news.news_ly, 来源
     */
    public String getNewsLy() {
        return newsLy;
    }

    /**
     *  设置 来源 字段:station_news.news_ly 
     *
     *  @param newsLy station_news.news_ly, 来源
     */
    public void setNewsLy(String newsLy) {
        this.newsLy = newsLy == null ? null : newsLy.trim();
    }

    /**
     *  获取  字段:station_news.news_img 
     *
     *  @return station_news.news_img, 
     */
    public String getNewsImg() {
        return newsImg;
    }

    /**
     *  设置  字段:station_news.news_img 
     *
     *  @param newsImg station_news.news_img, 
     */
    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg == null ? null : newsImg.trim();
    }

    /**
     *  获取  字段:station_news.news_mtname 
     *
     *  @return station_news.news_mtname, 
     */
    public String getNewsMtname() {
        return newsMtname;
    }

    /**
     *  设置  字段:station_news.news_mtname 
     *
     *  @param newsMtname station_news.news_mtname, 
     */
    public void setNewsMtname(String newsMtname) {
        this.newsMtname = newsMtname == null ? null : newsMtname.trim();
    }

    /**
     *  获取  字段:station_news.news_url 
     *
     *  @return station_news.news_url, 
     */
    public String getNewsUrl() {
        return newsUrl;
    }

    /**
     *  设置  字段:station_news.news_url 
     *
     *  @param newsUrl station_news.news_url, 
     */
    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl == null ? null : newsUrl.trim();
    }

    /**
     *  获取 0待发布 1已发布 2已删除 字段:station_news.news_state 
     *
     *  @return station_news.news_state, 0待发布 1已发布 2已删除
     */
    public Integer getNewsState() {
        return newsState;
    }

    /**
     *  设置 0待发布 1已发布 2已删除 字段:station_news.news_state 
     *
     *  @param newsState station_news.news_state, 0待发布 1已发布 2已删除
     */
    public void setNewsState(Integer newsState) {
        this.newsState = newsState;
    }

    /**
     *  获取 是否置顶(0.不置顶 1.置顶) 字段:station_news.news_istop 
     *
     *  @return station_news.news_istop, 是否置顶(0.不置顶 1.置顶)
     */
    public Integer getNewsIstop() {
        return newsIstop;
    }

    /**
     *  设置 是否置顶(0.不置顶 1.置顶) 字段:station_news.news_istop 
     *
     *  @param newsIstop station_news.news_istop, 是否置顶(0.不置顶 1.置顶)
     */
    public void setNewsIstop(Integer newsIstop) {
        this.newsIstop = newsIstop;
    }

    /**
     *  获取 内容 字段:station_news.news_content 
     *
     *  @return station_news.news_content, 内容
     */
    public String getNewsContent() {
        return newsContent;
    }

    /**
     *  设置 内容 字段:station_news.news_content 
     *
     *  @param newsContent station_news.news_content, 内容
     */
    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent == null ? null : newsContent.trim();
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }
}