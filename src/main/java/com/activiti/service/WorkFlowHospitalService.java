package com.activiti.service;


import com.activiti.mapper.WorkFlowHospitalMapper;
import com.activiti.pojo.WorkFlowHospital;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WorkFlowHospitalService extends BaseService<WorkFlowHospital>{

    @Autowired
    WorkFlowHospitalMapper workFlowHospitalMapper;


    @Override
    public Mapper<WorkFlowHospital> getMapper() {
        return workFlowHospitalMapper;
    }

    @Override
    public PageInfo listByPage(Map<String, Object> params) {
        return null;
    }

}
