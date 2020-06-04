package com.activiti.controller;

import com.activiti.pojo.Page;
import com.activiti.service.ActivitiModelService;
import com.activiti.utils.PageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/activitiModel")
public class ActivitiModelController {
    private static final Logger log = LogManager.getLogger(ActivitiModelController.class);
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ActivitiModelService activitiModelService;


    /**
     * 模型编辑页面
     * @return
     */
    @RequestMapping("/list")
    public String listModels(){
        return "activitiModels/list";
    }


    /**
     * 添加模型
     * @param map
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object addModel(@RequestParam Map<String, String> map){
        return activitiModelService.addModel(map);
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteModel/{id}")
    @ResponseBody
    public Object deleteModel(@PathVariable String id) {
        return activitiModelService.deleteModel(id);
    }


    /**
     * 部署模型
     * @param id
     * @return
     */
    @RequestMapping("/deployModel/{id}")
    @ResponseBody
    public Object deployModel(@PathVariable String id) {
        return activitiModelService.deployModel(id);
    }


    /**
     * 流程分页
     * @param page
     * @return
     */
    @RequestMapping("/getModelsByPage")
    @ResponseBody
    public Object getModelsByPage(Page page) {
        Map<String, Object> map = PageUtils.getFilterPage(page);
        return activitiModelService.listModelsByPage(map);
    }


    //上传模型
    @PostMapping("/models/uploadModel")
    @ResponseBody
    public Object uploadModel(@RequestParam Map<String, Object> map)  throws Exception {
        return activitiModelService.uploadModel(map);
    }



    /**
     * 进入流程定义管理页面
     * @return
     */
    @RequestMapping("/procdefs/list")
    public String procdefsList(){
        return "activitiModels/procdefs";
    }

    /**
     * 流程分页列表
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/procdefs/getProcdefsByPage")
    @ResponseBody
    public Object getProcdefsByPage(Page page)  throws Exception {
        Map<String, Object> map = PageUtils.getFilterPage(page);
        return activitiModelService.getProcdefsByPage(map);
    }


    /**
     * 查看流程图纸
     * @param processDefinitionId
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/procdefs/images/{processDefinitionId}")
    @ResponseBody
    public void getProcDefImage(@PathVariable String processDefinitionId,HttpServletResponse response) throws Exception {
        List<String> stringList = activitiModelService.getProcDefImage(processDefinitionId);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        byte[] bytes=null;
        for(String str : stringList){
            bytes=str.getBytes("ISO-8859-1");
            response.getOutputStream().write(bytes);
        }
    }


    /**
     * 下载流程图
     * @param id
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/procdefs/export-bpmm/{id}")
    @ResponseBody
    public void exportBPMN(@PathVariable String id,HttpServletResponse response) throws Exception {
        HashMap<String, String> data = activitiModelService.getBpmnXML(id);
        if(data!=null){
            byte[] bytes= data.get("bpmn").getBytes("ISO-8859-1");
            String filename = data.get("name");
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=\""+new String(filename.getBytes("GBK"),"iso-8859-1")+"\"");
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        }
    }

    @RequestMapping("/procdefs/configuser/query/{processDefinitionId}")
    @ResponseBody
    public Object getProcDefUserConfigById(@PathVariable String processDefinitionId) {
        return activitiModelService.getProcDefUserConfigById(processDefinitionId);
    }


    @RequestMapping("/procdefs/configuser/save")
    @ResponseBody
    public Object saveProcDefUserConfig(@RequestBody Object data) {
        return activitiModelService.saveProcDefUserConfig(data);
    }

}
