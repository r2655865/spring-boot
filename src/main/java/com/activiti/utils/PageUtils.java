package com.activiti.utils;

import com.activiti.pojo.Page;

import java.util.HashMap;
import java.util.Map;

public class PageUtils {


    public static Map<String, Object> getFilterPage(Page page) {
        Map<String, Object> map = new HashMap<>();
        if(page.getPageNum() == null){
            map.put("pageNum",1);
        }else{
            map.put("pageNum",page.getPageNum());
        }

        if(page.getPageSize() == null){
            map.put("pageSize",10);
        }else{
            map.put("pageSize",page.getPageSize());
        }
        map.put("name",page.getSearch());
        return map;
    }

}
