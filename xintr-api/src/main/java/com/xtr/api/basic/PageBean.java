package com.xtr.api.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>分页组件</p>
 *
 * @author 任齐
 * @createTime: 2016/7/1 10:29
 */
public class PageBean<T> {

    /**
     * 当前页码
     */
    private int curPage;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 显示数字分页数量
     */
    private int numSize = 10;

    /**
     * 总记录数
     */
    private int totalRecord;

    /**
     * 页面url
     */
    private String pageUrl;

    /**
     * 数据
     */
    private List<T> results = new ArrayList<T>();

    public PageBean() {
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNumSize() {
        return numSize;
    }

    public void setNumSize(int numSize) {
        this.numSize = numSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
