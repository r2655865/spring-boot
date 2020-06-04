package com.activiti.service;


import com.activiti.mapper.HospitalsMapper;
import com.activiti.pojo.Hospitals;
import com.activiti.pojo.PageDataResult;
import com.activiti.pojo.Role;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class HospitalsService extends BaseService<Hospitals>{

    @Autowired
    HospitalsMapper hospitalsMapper;




    @Override
    public Mapper<Hospitals> getMapper() {
        return hospitalsMapper;
    }

    @Override
    public PageInfo listByPage(Map<String, Object> params) {
        return null;
    }


    public PageDataResult listQueryByPage(Map<String, Object> params) {
        List<Hospitals> roleList = hospitalsMapper.selectByParams(params);
        PageDataResult pageDataResult = new PageDataResult();
        pageDataResult.setList(roleList);
        pageDataResult.setTotals(roleList.size());
        return pageDataResult;
    }


    public Hospitals selectBySignCode(String signCode){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("signCode",signCode);
        map.put("status",1);
        List<Hospitals> hospitals = hospitalsMapper.selectByParams(map);
        Hospitals hospital  = null;
        if(hospitals.size() > 0){
            hospital = hospitals.get(0);
        }
        return hospital;
    }
}
