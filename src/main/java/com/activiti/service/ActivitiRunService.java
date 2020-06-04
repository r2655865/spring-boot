package com.activiti.service;

import com.activiti.pojo.*;
import com.activiti.utils.ResultUtil;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ActivitiRunService {


    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private WorkflowAuditService workflowAuditService;
    @Autowired
    private HospitalsService hospitalsService;

    @Autowired
    private WorkFlowHospitalService workFlowHospitalService;

    @Autowired
    private WorkflowUserTaskService workflowUserTaskService;



    /**
     * 开启流程
     * @param activitiApply
     * @return
     */
    public Result startProcess(String signCode , ActivitiApply activitiApply){

        if(!getHospital(signCode)){
            return ResultUtil.operatDataResult(0,null,"医院不存在！");
        }else if(StringUtils.isBlank(activitiApply.getKey())){
            return ResultUtil.operatDataResult(0,null,"流程定义不能为空！");
        }else if(StringUtils.isBlank(activitiApply.getApplyUserId())){
            return ResultUtil.operatDataResult(0,null,"发起人不能为空！");
        }else{

            String key = activitiApply.getKey();

            //查询流程是否部署
            ProcessDefinition processDefinition= repositoryService
                    .createProcessDefinitionQuery().processDefinitionKey(key)
                    .latestVersion().singleResult();

            if(processDefinition == null){
                return ResultUtil.operatDataResult(0,key,"流程未部署！");
            }else{
                //获取受理人节点
                Map<String, Object> variables = new HashMap<String, Object>();
                String applyUserId = activitiApply.getApplyUserId();
                //设置申请人
                variables.put("applyUserId",applyUserId);
                variables.put("flowType", "normal");
                //设置发请人
                identityService.setAuthenticatedUserId(applyUserId);
                //启动流程
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, variables);
                //返回流程id
                String processId = processInstance.getProcessInstanceId();

                //获取当下审批人
                WorkflowUserTask userTask = workflowUserTaskService.getOneByWorkflowUserTaskByDefKeyAndTaskKey(
                        processInstance.getProcessDefinitionKey(),processInstance.getActivityId());

                Map<String,Object> resultMap = new HashMap<String,Object>();
                if(userTask !=null){
                    resultMap.put("auditRoleCode",userTask.getCandidateIds());
                }
                resultMap.put("processInstanceId",processId);


                //插入关联明细
                WorkFlowHospital workFlowHospital = new WorkFlowHospital();
                workFlowHospital.setTaskDefkey(activitiApply.getKey());
                workFlowHospital.setProcessInstanceId(processId);
                workFlowHospital.setSignCode(signCode);
                workFlowHospitalService.add(workFlowHospital);

                return ResultUtil.operatDataResult(1,resultMap,"流程启动成功！");
            }
        }
    }



    /**
     * 获取审批列表
     * @param map
     * @return
     */
    public Result getProcessList(String signCode,Map<String,Object> map){

        Result result = null;

        if(!map.containsKey("roleCode")){
            result =  ResultUtil.operatDataResult(0,null,"审批人不存在！");
        }else if(!getHospital(signCode)){
            result = ResultUtil.operatDataResult(0,null,"医院不存在！");
        }else if(!map.containsKey("key")){
            result = ResultUtil.operatDataResult(0,null,"流程编码不存在！");
        }else{
            map.put("signCode",signCode);
            List<Map<String,Object>> list = workflowUserTaskService.getProcessList(map);
            result = new Result(true,"获取成功!",list);
        }

        return result;
    }




    @Transactional(propagation = Propagation.REQUIRED)
    public Object approvelProcess(String signCode,WorkflowAudit workflowAudit) {

        if(!getHospital(signCode)){
            return ResultUtil.operatDataResult(0,null,"医院不存在！");
        }

        //审批状态
        Integer approved = workflowAudit.getStatus();
        //获取任务instanceId
        String instanceId = "";
        if(StringUtils.isBlank(workflowAudit.getProcessInstanceId())){
            return ResultUtil.operatDataResult(0,null,"实例id为空！");
        }else{
            instanceId = workflowAudit.getProcessInstanceId();
        }

        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        String taskId = "";
        if(task != null){
            taskId = task.getId();
        }else{
            return ResultUtil.operatDataResult(0,instanceId,"流程不存在！");
        }

        taskService.complete(taskId);

        Map<String, Object> variables = new HashMap<>();

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(workflowAudit.getProcessInstanceId()).singleResult();

        //跳转条件
        if(approved == 1){
            if(processInstance == null){
                //流程已结束
                workflowAudit.setResultStatus(1);
            }else{
                //流程未结束
                workflowAudit.setResultStatus(0);

                //流程未结束获取下一审批节点
                WorkflowUserTask userTask = workflowUserTaskService.getOneByWorkflowUserTaskByDefKeyAndTaskKey(
                        processInstance.getProcessDefinitionKey(),processInstance.getActivityId());

                workflowAudit.setNextAuditRole(userTask.getCandidateIds());
            }

/*            variables.put("approved", "true");*/
        }else{
/*            variables.put("approved", "false");*/
            //驳回
            workflowAudit.setResultStatus(2);
        }


        //插入记录 WorkflowAudit
        workflowAudit.setTaskId(taskId);
        workflowAudit.setTaskName(task.getName());
        workflowAudit.setAuditRoleCode(task.getAssignee());
        //查询历史获取申请人
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).orderByProcessInstanceStartTime().asc().singleResult();
        workflowAudit.setApplyStaffId(hpi.getStartUserId());
        workflowAuditService.add(workflowAudit);

        return ResultUtil.operatDataResult(1 , workflowAudit , "审批成功!");
    }


    public Boolean getHospital(String signCode){

       return hospitalsService.selectBySignCode(signCode) == null ? false : true;
    }

}
