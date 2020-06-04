package com.activiti.service;


import com.activiti.mapper.RoleMapper;
import com.activiti.pojo.PageDataResult;
import com.activiti.pojo.Role;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class RoleService extends BaseService<Role>{

    @Autowired
    RoleMapper roleMapper;


    @Override
    public Mapper<Role> getMapper() {
        return roleMapper;
    }

    @Override
    public PageInfo listByPage(Map<String, Object> params) {
        return null;
    }


    public PageDataResult listQueryByPage(Map<String, Object> params) {
        List<Role> roleList = roleMapper.listQueryByPage(params);
        PageDataResult pageDataResult = new PageDataResult();
        pageDataResult.setList(roleList);
        pageDataResult.setTotals(roleList.size());
        return pageDataResult;
    }



    public List<Role> listSelectQuery(Map<String, Object> params) {
        return  roleMapper.listQueryByPage(params);
    }
}
