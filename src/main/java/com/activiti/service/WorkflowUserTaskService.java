package com.activiti.service;

import com.activiti.mapper.WorkflowAuditMapper;
import com.activiti.mapper.WorkflowUserTaskMapper;
import com.activiti.pojo.ViewDto;
import com.activiti.pojo.WorkflowUserTask;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class WorkflowUserTaskService {
	
	@Resource
	WorkflowUserTaskMapper workflowUserTaskMapper;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;

	
	public List<WorkflowUserTask> listWorkflowUserTaskByDefKeyAndTaskKey(String procDefcKey, String taskKey){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("procDefKey",procDefcKey);
		params.put("taskDefKey",taskKey);

		return workflowUserTaskMapper.listWorkflowUserTaskByDefKeyAndTaskKey(params);
	}


	public WorkflowUserTask getOneByWorkflowUserTaskByDefKeyAndTaskKey(String procDefcKey, String taskKey){
		return workflowUserTaskMapper.getOneByWorkflowUserTaskByDefKeyAndTaskKey(procDefcKey,taskKey);
	}


	/**
	 * 获取流程图 审批时
	 * @param params
	 * @return
	 */
	public List<ViewDto> getBpmnData(Map<String, Object> params){

		String instanceId = params.get("instanceId").toString();

		List<ViewDto> viewDtos = new ArrayList<ViewDto>();
		//获取流程所有节点
		if (!StringUtils.isEmpty(instanceId)) {
			//获取历史流程实例
			HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().
					processInstanceId(instanceId).singleResult();
			//获取流程图
			BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());

			List<Process> processes = bpmnModel.getProcesses();
			for(Process process : processes){
				Collection<FlowElement> collection = process.getFlowElements();
				for (FlowElement c : collection) {
					ViewDto viewDto = new ViewDto();
					if(c instanceof org.activiti.bpmn.model.StartEvent) {
						viewDto.setTaskName("开始");
						viewDto.setActivitiId(c.getId());
						viewDtos.add(viewDto);
					} else if (c instanceof org.activiti.bpmn.model.EndEvent) {
						viewDto.setTaskName("结束");
						viewDto.setActivitiId(c.getId());
						viewDtos.add(viewDto);
					}else if(!StringUtils.isEmpty(c.getName())){
						viewDto.setTaskName(c.getName());
						viewDto.setActivitiId(c.getId());
						viewDtos.add(viewDto);
					}
				}
			}
		}

		List<ViewDto> selectViewDtos = workflowUserTaskMapper.userTaskHistoryList(instanceId);
		Map<String,ViewDto> map = new HashMap<String,ViewDto>();
		for(ViewDto v : selectViewDtos){
			map.put(v.getActivitiId(),v);
		}


		List<ViewDto> reslut = new ArrayList<ViewDto>();
		for(ViewDto viewDto :viewDtos){
			if(map.containsKey(viewDto.getActivitiId())){
				ViewDto viewDto1 = map.get(viewDto.getActivitiId());
				if(viewDto1.getStatus() != null){
				    if(viewDto1.getStatus() != 1){
				        //驳回流程结束
                        viewDto1.setIsOver(1);
                    }else{
                        //当前流程
                        viewDto1.setNoteStatus(1);
                    }
				}else{
				    if("开始".equals(viewDto.getTaskName())){
                        viewDto1.setNoteStatus(1);
                    }else if("结束".equals(viewDto.getTaskName())){
                        if(selectViewDtos.size() == viewDtos.size()){
                            viewDto1.setIsOver(1);
                            viewDto1.setNoteStatus(2);
                        }
                    }
                }
                viewDto1.setTaskName(viewDto.getTaskName());
				reslut.add(viewDto1);
			}else{
				reslut.add(viewDto);
			}
		}


		//流程是否结束
/*		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(instanceId).singleResult();*/
		return reslut;

/*		Map<String, Object> map = new HashMap<>();
		map.put("content", workflowUserTaskMapper.getBpmnData(instanceId));
		map.put("finish", historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).finished().list());
		return map;*/
	}


	/**
	 * 获取审批数据
	 * @param params
	 * @return
	 */
	public List<Map<String,Object>> getProcessList(Map<String, Object> params){
		return workflowUserTaskMapper.getProcessList(params);
	}


}
