package com.activiti.listener;


import com.activiti.pojo.WorkflowUserTask;
import com.activiti.service.WorkflowUserTaskService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("taskCreateListener")
public class TaskCreateListener implements TaskListener {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WorkflowUserTaskService workflowUserTaskService;

    @Override
    public void notify(DelegateTask delegateTask) {

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(delegateTask.getProcessDefinitionId())
                .singleResult();

        String processDefinitionKey = processDefinition.getKey();
        //获取任务KEY
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();

        WorkflowUserTask userTask = workflowUserTaskService.getOneByWorkflowUserTaskByDefKeyAndTaskKey(processDefinitionKey, taskDefinitionKey);
        try {
            //获取指定的审批角色编码
            String code = userTask.getCandidateIds();
            delegateTask.setAssignee(code);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
