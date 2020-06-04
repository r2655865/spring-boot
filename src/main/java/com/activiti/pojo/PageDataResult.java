package com.activiti.pojo;

import java.io.Serializable;
import java.util.List;

public class PageDataResult implements Serializable {

    private Integer code=200;

    //总记录数量
    private Integer totals;

    private List<?> list;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getTotals() {
        return totals;
    }

    public void setTotals(Integer totals) {
        this.totals = totals;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
