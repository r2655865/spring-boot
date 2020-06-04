package com.activiti.pojo;

import java.io.Serializable;

public class Result<T> implements Serializable {


    /** 错误码. */
    private boolean status;

    /** 提示信息. */
    private String msg;

    /** 具体的内容. */
    private T data;


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {

    }

    public Result(boolean status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
}
