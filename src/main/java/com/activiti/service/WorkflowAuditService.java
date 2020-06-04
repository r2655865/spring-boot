package com.activiti.service;

import com.activiti.mapper.WorkflowAuditMapper;
import com.activiti.pojo.WorkflowAudit;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WorkflowAuditService extends BaseService<WorkflowAudit>{

    @Autowired
    private WorkflowAuditMapper workflowAuditMapper;

    @Override
    public Mapper<WorkflowAudit> getMapper() {
        return workflowAuditMapper;
    }

    @Override
    public PageInfo listByPage(Map<String, Object> params) {
        return null;
    }
}
