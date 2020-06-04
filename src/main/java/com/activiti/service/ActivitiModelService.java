package com.activiti.service;


import com.activiti.config.HMProcessDiagramGenerator;
import com.activiti.mapper.WorkflowUserTaskMapper;
import com.activiti.pojo.*;
import com.activiti.utils.Md5;
import com.activiti.utils.ResultUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivitiModelService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WorkflowUserTaskMapper workflowUserTaskMapper;

    @Transactional
    public Result addModel(Map<String, String> params) {

        try {
            //初始化一个空模型
            Model model = repositoryService.newModel();

            //设置一些默认信息
            String tempModelName = params.get("name");
            String tempKey = params.get("key");
            String tempDescription = params.get("description");
            String name = StringUtils.isEmpty(tempModelName) ? "new-model" : tempModelName;
            String description = StringUtils.isEmpty(tempDescription) ? "" : tempDescription;
            int vision = 1;
            String key = StringUtils.isEmpty(tempModelName) ? UUID.randomUUID().toString() : tempKey;

            ObjectNode modelNode = objectMapper.createObjectNode();
            modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelNode.put(ModelDataJsonConstants.MODEL_REVISION, vision);

            model.setName(name);
            model.setKey(key);
            model.setMetaInfo(modelNode.toString());

            repositoryService.saveModel(model);
            String id = model.getId();

            //完善ModelEditorSource
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace",
                    "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));


            Map<String, Object> reasons = new HashMap<>();
            reasons.put("data", params);
            return ResultUtil.operatDataResult(1,reasons,"添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 删除模型
     * @param modelId
     * @return
     */
    @Transactional
    public Result deleteModel(String modelId) {
        repositoryService.deleteModel(modelId);
        return ResultUtil.operatDataResult(1,modelId,"删除成功!");
    }


    //部署模型
    @Transactional
    public Result deployModel(String modelId) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            byte[] resource = repositoryService.getModelEditorSource(modelData.getId());
            ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                    .readTree(resource);
            byte[] bpmnBytes = null;

            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";

            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8"))
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            return ResultUtil.operatDataResult(1,null,"部署成功!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    /**
     * 查询模型分页
     *
     * @param params
     * @return
     */
    public PageDataResult listModelsByPage(Map<String, Object> params) {
        List<ActivitiModelDto> data = new ArrayList<ActivitiModelDto>();
        String modelName = params.get("name") == null ? "" : "%" + params.get("name") + "%";
        if (params != null) {
            Page page = PageHelper.startPage(params);
            //查询流程定义 .modelNameLike(modelName)
            List<Model> models = repositoryService
                    .createModelQuery()
                    .orderByLastUpdateTime()
                    .desc()
                    .listPage(page.getStartRow(), page.getPageSize());

            if (models != null && models.size() > 0) {
                for (Model pd : models) {
                    ActivitiModelDto modelDto = new ActivitiModelDto();
                    String deploymentId = pd.getDeploymentId();
                    if (!StringUtils.isEmpty(deploymentId)) {
                        Deployment deployment = repositoryService.createDeploymentQuery()
                                .deploymentId(deploymentId).singleResult();
                        modelDto.setDeploymentId(deploymentId);
                        modelDto.setDeployTime(deployment.getDeploymentTime());
                    }

                    modelDto.setId(pd.getId());
                    modelDto.setName(pd.getName());
                    modelDto.setKey(pd.getKey());
                    modelDto.setVersion(pd.getVersion());
                    modelDto.setCreateTime(pd.getCreateTime());
                    modelDto.setLastUpdateTime(pd.getLastUpdateTime());
                    data.add(modelDto);
                }
            }
        }
        long total = repositoryService.createModelQuery().modelNameLike(modelName).count();

        PageDataResult pageDataResult = new PageDataResult();
        pageDataResult.setList(data);
        pageDataResult.setTotals((int) total);
        return pageDataResult;
    }


    /**
     * 流程分页列表
     * @param params
     * @return
     */
    public PageDataResult getProcdefsByPage(Map<String, Object> params) {
        List<ProcessDefinitionDto> data = new ArrayList<ProcessDefinitionDto>();
        String defName = params.get("name") == null ? "" : "%" + params.get("name") + "%";
        if (params != null) {
            Page page = PageHelper.startPage(params);
            //查询流程定义
            List<ProcessDefinition> pdList = repositoryService
                    .createProcessDefinitionQuery()
                    .orderByProcessDefinitionVersion()
                    .latestVersion()
                    .desc()
                    .listPage(page.getStartRow(), page.getPageSize());

            data = new ArrayList<ProcessDefinitionDto>();
            if (pdList != null && pdList.size() > 0) {
                for (ProcessDefinition pd : pdList) {
                    ProcessDefinitionDto processDefinitionDto = new ProcessDefinitionDto();
                    String deploymentId = pd.getDeploymentId();
                    Deployment deployment = repositoryService.createDeploymentQuery()
                            .deploymentId(deploymentId).singleResult();
                    processDefinitionDto.setDeploymentId(deploymentId);
                    processDefinitionDto.setDeployTime(deployment.getDeploymentTime());
                    processDefinitionDto.setId(pd.getId());
                    processDefinitionDto.setName(pd.getName());
                    processDefinitionDto.setKey(pd.getKey());
                    processDefinitionDto.setVersion(pd.getVersion());
                    processDefinitionDto.setImageName(pd.getDiagramResourceName());
                    processDefinitionDto.setResourceName(pd.getResourceName());
                    processDefinitionDto.setSuspended(pd.isSuspended() == true ? "2" : "1");//挂起状态(1.未挂起 2.已挂起)
                    data.add(processDefinitionDto);
                }
            }
        }
        long total = repositoryService
                .createProcessDefinitionQuery().processDefinitionNameLike(defName).latestVersion().count();

        PageDataResult pageDataResult = new PageDataResult();
        pageDataResult.setList(data);
        pageDataResult.setTotals((int) total);
        return pageDataResult;
    }


    /**
     * 查看流程图纸
     * @param processDefinitionId
     * @return
     */
    public ArrayList<String> getProcDefImage(String processDefinitionId) {
        InputStream in = null;
        ArrayList<String> bytes = new ArrayList<String>();
        try {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            //修改activity Bug:使用activity部署生成的图片线条文字无法显示问题
            //现采用自定义生成图片
            //in = this.getProcessResource( "image", processDefinitionId );
            ProcessDiagramGenerator processDiagramGenerator = new HMProcessDiagramGenerator();
            in = processDiagramGenerator.generateDiagram(bpmnModel, "png", new ArrayList<String>(),
                    new ArrayList<String>(), "宋体", "宋体", "宋体", null, 1.0D);

            byte[] b = new byte[1024];
            while (in.available() > 1024 && in.read(b, 0, 1024) != -1) {
                bytes.add(new String(b, "ISO-8859-1"));
            }
            if (in.available() > 0) {
                int lastLen = in.available();
                byte[] lastByte = new byte[lastLen];
                in.read(lastByte);
                bytes.add(new String(lastByte, "ISO-8859-1"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }


    /**
     * 下载流程图
     * @param id
     * @return
     * @throws Exception
     */
    public HashMap<String, String> getBpmnXML(String id) throws Exception {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(id);
        if (bpmnModel.getMainProcess() == null
                || bpmnModel.getMainProcess().getId() == null) {
            return null;
        } else {
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
            HashMap<String, String> result = new HashMap<String, String>();
            result.put("bpmn", new String(bpmnBytes, "ISO-8859-1"));
            result.put("name", bpmnModel.getMainProcess().getName() + ".bpmn");
            return result;
        }
    }


    /**
     * 上传模型
     *
     * @param params
     */
    @Transactional
    public Result uploadModel(Map<String, Object> params) {

        try {
            String id = (String) params.get("id");
            String content = (String) params.get("content");
            InputStream in = new ByteArrayInputStream(content.getBytes("utf-8"));
            XMLInputFactory xif = XMLInputFactory.newInstance();
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            if (bpmnModel.getMainProcess() == null
                    || bpmnModel.getMainProcess().getId() == null) {

                return ResultUtil.operatDataResult(0,null,"XML格式不是BPMN文件格式，读取失败！");
            } else {
                org.activiti.engine.repository.Model modelData = repositoryService.getModel(id);
                if (modelData == null) {
                    throw new Exception("对应上传模型没找到！");
                }

                BpmnJsonConverter converter = new BpmnJsonConverter();
                ObjectNode modelNode = converter.convertToJson(bpmnModel);


                ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
                String subject = modelNode.get("properties").get("name").asText();
                String key = modelNode.get("properties").get("process_id").asText();


                modelObjectNode.put("name", subject);
                modelObjectNode.put("revision", 1);
                modelObjectNode.put("description", subject);

                modelData.setMetaInfo(modelObjectNode.toString());
                modelData.setName(subject);
                modelData.setKey(key);

                repositoryService.saveModel(modelData);
                repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));


                return ResultUtil.operatDataResult(1,null,"上传模型成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取当前节点
     * @param id
     * @return
     */
    @Transactional
    public List<WorkflowUserTask> getProcDefUserConfigById(String id) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(id).singleResult();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("procDefKey", pd.getKey());
        List<WorkflowUserTask> tasks = workflowUserTaskMapper.listWorkflowUserTaskByDefKey(params);
        Map<String, WorkflowUserTask> taskHashMap = null;

        try {
            if (tasks != null && tasks.size() > 0) {
                taskHashMap = tasks.stream().collect(Collectors.toMap(WorkflowUserTask::getTaskDefKey, workflowUserTask -> workflowUserTask));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(pd.getId());
        List<ActivityImpl> list = pde.getActivities();
        UserTaskActivityBehavior userTaskActivityBehavior = null;
        WorkflowUserTask workflowUserTask = null;
        for (ActivityImpl activity : list) {
            ActivityBehavior activityBehavior = activity.getActivityBehavior();

            //是否为用户任务
            if (activityBehavior instanceof UserTaskActivityBehavior) {
                userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            } else if (activityBehavior instanceof ParallelMultiInstanceBehavior) {
                userTaskActivityBehavior = (UserTaskActivityBehavior) ((MultiInstanceActivityBehavior) activityBehavior)
                        .getInnerActivityBehavior();
            } else {
                continue;
            }

            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            //删除Map中的数据
            if (taskHashMap != null && taskHashMap.size() > 0) {
                workflowUserTask = taskHashMap.get(taskDefinition.getKey());
                if (workflowUserTask != null) {
                    //更新任务节点
                    workflowUserTask.setProcDefKey(pd.getKey());
                    workflowUserTask.setProcDefName(pd.getName());
                    workflowUserTask.setTaskDefKey(taskDefinition.getKey());
                    workflowUserTask.setTaskName(taskDefinition.getNameExpression().toString());
                    workflowUserTaskMapper.updateByPrimaryKeySelective(workflowUserTask);
                    taskHashMap.remove(taskDefinition.getKey());
                    continue;
                }
            }

            if ("userRetry".equals(taskDefinition.getKey())) {
                continue;
            }


            workflowUserTask = new WorkflowUserTask();
            workflowUserTask.setProcDefKey(pd.getKey());
            workflowUserTask.setProcDefName(pd.getName());
            workflowUserTask.setTaskDefKey(taskDefinition.getKey());
            workflowUserTask.setTaskName(taskDefinition.getNameExpression().toString());
            workflowUserTaskMapper.insert(workflowUserTask);
        }

        //删除多余数据
        if (taskHashMap != null && taskHashMap.size() > 0) {
            Iterator<WorkflowUserTask> removeTasks = taskHashMap.values().iterator();
            while (removeTasks.hasNext()) {
                workflowUserTaskMapper.deleteByPrimaryKey(removeTasks.next());
            }
        }

        //返回查询数据
        tasks = workflowUserTaskMapper.listWorkflowUserTaskByDefKey(params);

        ArrayList<WorkflowUserTask> workFlowUserTaskDtos = new ArrayList<WorkflowUserTask>();
        if (tasks == null) {
            return workFlowUserTaskDtos;
        }
        WorkflowUserTask workFlowUserTaskDto = null;
        for (WorkflowUserTask userTask : tasks) {
            workFlowUserTaskDto = new WorkflowUserTask();
            BeanUtils.copyProperties(userTask, workFlowUserTaskDto);
            workFlowUserTaskDtos.add(workFlowUserTaskDto);
        }
        return workFlowUserTaskDtos;
    }


    @Transactional
    public Result saveProcDefUserConfig(Object data) {
        List<WorkflowUserTask> workflowUserTasks = JSONArray.parseArray(JSON.toJSON(data).toString(), WorkflowUserTask.class);
        for (WorkflowUserTask workflowUserTask : workflowUserTasks) {
            workflowUserTaskMapper.updateByPrimaryKey(workflowUserTask);
        }
        return ResultUtil.operatDataResult(1,data,"更新成功！");
    }

}
