package com.activiti.controller;


import com.activiti.pojo.ActivitiApply;
import com.activiti.pojo.WorkflowAudit;
import com.activiti.service.ActivitiRunService;
import com.activiti.service.WorkflowUserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/activiti")
public class ActivitiRunController {


    @Autowired
    private ActivitiRunService activitiRunService;
    @Autowired
    private WorkflowUserTaskService workflowUserTaskService;


    /**
     * keyName:流程标识code,
     * applyUserId:发起人id,
     * 人员角色编码 code
     * 请求格式
     * {
     * 	"key": "staff_awards_process",
     * 	"applyUserId": "s444444",
     * }
     * @return
     */
    @PostMapping(value = "/start",produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object startProcess(@RequestHeader("signCode")String signCode,
                               @RequestBody ActivitiApply activitiApply) {

        return  activitiRunService.startProcess(signCode,activitiApply);
    }


    /**
     * 根据角色 获取任务审批列表
     * @param map /roleCode
     * @return
     */
    @PostMapping(value = "/getProcessList",produces="application/json;charset=UTF-8")
    @ResponseBody
    public Object getProcessList(@RequestHeader("signCode")String signCode,
                                 @RequestBody Map<String,Object> map) {
        return activitiRunService.getProcessList(signCode,map);
    }



    /**
     * 任务审批
     * @param workflowAudit
     * @return
     */
    @PostMapping(value = "/approvalProcess")
    @ResponseBody
    public Object approvalProcess(@RequestHeader("signCode")String signCode,
                                  @RequestBody WorkflowAudit workflowAudit) {
        return activitiRunService.approvelProcess(signCode,workflowAudit);
    }


    /**
     * 查看流程图
     * @param params
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping(value = "/getBpmnData",produces="application/json;charset=UTF-8")
    public Object getBpmnData(@RequestBody Map<String, Object> params) {
        return workflowUserTaskService.getBpmnData( params );
    }

}
