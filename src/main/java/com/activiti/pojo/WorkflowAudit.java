package com.activiti.pojo;

import com.activiti.utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;


@Table(name = "kb_workflow_audit")
public class WorkflowAudit implements Serializable {

    @Id
    @KeySql(genId = IDGenerator.class)
    @Column(name = "id")
    private String id;

    @Column(name="process_instance_id")
    private String processInstanceId;

    @Column(name="taskId")
    private String taskId;

    @Column(name="task_name")
    private String taskName;

    @Column(name="audit_role_code")
    private String auditRoleCode;

    @Column(name="audit_staffId")
    private String auditStaffId;


    @Column(name="opinion")
    private String opinion;

    private HashMap<String, Object> variables;
    @Column(name="status")
    private Integer status;

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    @Column(name="created")
    private Date created;

    @Column(name="apply_staffId")
    private String applyStaffId;
    @Transient
    private Integer resultStatus;


    @Transient
    private String nextAuditRole;

    public String getNextAuditRole() {
        return nextAuditRole;
    }

    public void setNextAuditRole(String nextAuditRole) {
        this.nextAuditRole = nextAuditRole;
    }

    public Integer getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Integer resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAuditRoleCode() {
        return auditRoleCode;
    }

    public void setAuditRoleCode(String auditRoleCode) {
        this.auditRoleCode = auditRoleCode;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public HashMap<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, Object> variables) {
        this.variables = variables;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getApplyStaffId() {
        return applyStaffId;
    }

    public void setApplyStaffId(String applyStaffId) {
        this.applyStaffId = applyStaffId;
    }

    public String getAuditStaffId() {
        return auditStaffId;
    }

    public void setAuditStaffId(String auditStaffId) {
        this.auditStaffId = auditStaffId;
    }
}
