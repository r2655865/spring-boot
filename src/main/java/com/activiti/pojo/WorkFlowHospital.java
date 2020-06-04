package com.activiti.pojo;

import com.activiti.utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="kb_workflow_hospital")
public class WorkFlowHospital implements Serializable {

    @Id
    @KeySql(genId = IDGenerator.class)
    @Column(name = "id")
    private String id;

    @Column(name = "sign_code")
    private String signCode;

    @Column(name = "process_instance_id")
    private String processInstanceId;

    @Column(name = "task_def_key")
    private String taskDefkey;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskDefkey() {
        return taskDefkey;
    }

    public void setTaskDefkey(String taskDefkey) {
        this.taskDefkey = taskDefkey;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
