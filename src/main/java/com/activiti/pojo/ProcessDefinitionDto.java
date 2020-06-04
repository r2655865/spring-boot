package com.activiti.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class ProcessDefinitionDto implements Serializable {

    private String id;
    private String name;
    private String key;
    private Integer version;
    private String deploymentId;
    private String resourceName;
    private String imageName;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date deployTime;
    private String suspended;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

    public String getSuspended() {
        return suspended;
    }

    public void setSuspended(String suspended) {
        this.suspended = suspended;
    }
}
