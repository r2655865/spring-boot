package com.activiti.mapper;

import com.activiti.pojo.WorkflowAudit;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;


public interface WorkflowAuditMapper extends Mapper<WorkflowAudit> {

    WorkflowAudit listWorkflowAuditByInstanceId(@Param("processInstanceId")String processInstanceId,@Param("taskName")String taskName);
}
