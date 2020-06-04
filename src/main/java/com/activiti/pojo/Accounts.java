package com.activiti.pojo;

import com.activiti.utils.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Table(name="kb_accounts")
public class Accounts implements Serializable {


    @Id
    @KeySql(genId = IDGenerator.class)
    @Column(name = "id")
    private String id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "pass_word")
    private String passWord;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "status")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
