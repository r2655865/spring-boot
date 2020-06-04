package com.activiti.pojo;

import com.activiti.utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "kb_workflow_user_task")
public class WorkflowUserTask implements Serializable {
    /**
     * 主键
     */
    @Id
    @KeySql(genId = IDGenerator.class)
    @Column(name = "id")
    private String id;
    /**
     * 流程定义KEY
     */
    @Column(name="proc_def_key")
    private String procDefKey;
    /**
     * 流程定义名称
     */
    @Column(name="proc_def_name")
    private String procDefName;
    /**
     * 任务定义KEY
     */
    @Column(name="task_def_key")
    private String taskDefKey;
    /**
     * 任务名称
     */
    @Column(name="task_name")
    private String taskName;
    /**
     * 任务类型
     */
    @Column(name="task_type")
    private String taskType;
    /**
     * 候选人名称(多个)
     */
    @Column(name="candidate_name")
    private String candidateName;
    /**
     * 候选人ID(多个)
     */
    @Column(name="candidate_ids")
    private String candidateIds;


    /**
     * 状态
     */
    @Column(name="status")
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefName() {
        return procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateIds() {
        return candidateIds;
    }

    public void setCandidateIds(String candidateIds) {
        this.candidateIds = candidateIds;
    }

    public Integer getStatus() { return status;}

    public void setStatus(Integer status) { this.status = status; }

    public Date getCreated() { return created; }

    public void setCreated(Date created) { this.created = created; }
}

