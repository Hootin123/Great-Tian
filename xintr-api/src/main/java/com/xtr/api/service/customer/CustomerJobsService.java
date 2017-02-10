package com.xtr.api.service.customer;

import java.util.List;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public interface CustomerJobsService {
/*
    */
/**
     * 查询
     * @param param
     * @return
     *//*

    public Map<String,Object> find(Map<String,Object> param);
*/

    /**
     * 添加
     * @param param
     * @return
     */
    public int add(Map<String,Object> param);

   /* *//**
     * 分页
     * @param param
     * @return
     *//*
    public List<Map<String,Object>> findListPage(Map<String,Object> param);

    *//**
     * 总数
     * @param param
     * @return
     *//*
    public int findListCount(Map<String,Object> param);

    *//**
     * 批量添加
     * @param param
     *//*
    public void addJobList(Map<String,Object> param);

    *//**
     * 分组分页
     * @param param
     * @return
     *//*
    public List<Map<String,Object>> findGroupPage(Map<String,Object> param);

    *//**
     * 分组总数
     * @param param
     * @return
     *//*
    public int findGroupCount(Map<String,Object> param);*/
}
