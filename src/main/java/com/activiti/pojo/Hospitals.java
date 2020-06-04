package com.activiti.pojo;

import com.activiti.utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="kb_hospitals")
public class Hospitals implements Serializable {

    @Id
    @KeySql(genId = IDGenerator.class)
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;


    @Column(name = "sign_code")
    private String signCode;

    @Column(name = "status")
    private Boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;


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

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
