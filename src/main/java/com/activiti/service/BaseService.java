package com.activiti.service;

import com.activiti.pojo.Result;
import com.activiti.utils.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseService<T> {

    public abstract Mapper<T> getMapper();


    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(T obj) {
        int insertCount = getMapper().insert(obj);
        return ResultUtil.operatDataResult(insertCount , obj , "添加成功!");
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateByPrimaryKeySelective(T obj) {
        int updateCount = getMapper().updateByPrimaryKeySelective(obj);
        return ResultUtil.operatDataResult(updateCount , obj,"修改成功!");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Result deleteById(String id) {
        int deleteCount = getMapper().deleteByPrimaryKey(id);
        return ResultUtil.operatDataResult(deleteCount , null ,"删除成功!");
    }

    public T getById(String hId) {
        return (T)getMapper().selectByPrimaryKey(hId);
    }


    public abstract PageInfo listByPage(Map<String, Object> params);


    public List<T> getAll(Map<String, Object> params) {
        List<T> data;
        if (params != null) {
            PageHelper.startPage(params);
            data = getMapper().selectAll();
        } else {
            data = new ArrayList<T>();
        }
        return data;
    }


    public List<T> listByObj(T t) {
        return getMapper().select(t);
    }

}
