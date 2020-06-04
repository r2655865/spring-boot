package com.activiti.pojo;

import java.io.Serializable;

public class ActivitiApply implements Serializable {

    private String key;

    private String applyUserId;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

}
