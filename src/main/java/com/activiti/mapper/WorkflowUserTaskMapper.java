package com.activiti.mapper;

import com.activiti.pojo.ViewDto;
import com.activiti.pojo.WorkflowUserTask;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WorkflowUserTaskMapper extends Mapper<WorkflowUserTask> {


	List<WorkflowUserTask> listWorkflowUserTaskByDefKeyAndTaskKey(Map<String, Object> params);

	List<WorkflowUserTask> listWorkflowUserTaskByDefKey(Map<String, Object> params);

	WorkflowUserTask getOneByWorkflowUserTaskByDefKeyAndTaskKey(@Param("procDefKey") String procDefKey, @Param("taskDefKey") String taskDefKey);

	String getBpmnData(@Param("instanceId") String instanceId);

	List<ViewDto> userTaskHistoryList(@Param("instanceId") String instanceId);

	List<Map<String,Object>> getProcessList(Map<String, Object> params);
}

