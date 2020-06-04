package com.activiti.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class ActivitiModelDto implements Serializable {

    private String id;
    private Integer rev;
    private String name;
    private String key;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastUpdateTime;
    private Integer version;
    private String metoInfo;
    private String deploymentId;
    private String editorSourceValueID;
    private String editorSourceExtraValueID;
    private Date deployTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRev() {
        return rev;
    }

    public void setRev(Integer rev) {
        this.rev = rev;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getMetoInfo() {
        return metoInfo;
    }

    public void setMetoInfo(String metoInfo) {
        this.metoInfo = metoInfo;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getEditorSourceValueID() {
        return editorSourceValueID;
    }

    public void setEditorSourceValueID(String editorSourceValueID) {
        this.editorSourceValueID = editorSourceValueID;
    }

    public String getEditorSourceExtraValueID() {
        return editorSourceExtraValueID;
    }

    public void setEditorSourceExtraValueID(String editorSourceExtraValueID) {
        this.editorSourceExtraValueID = editorSourceExtraValueID;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }
}
