package com.activiti.utils;

import tk.mybatis.mapper.genid.GenId;

import java.util.UUID;

public class IDGenerator implements GenId<String> {


    @Override
    public String genId(String s, String s1) {
        return UUID.randomUUID().toString().replace("-","");
    }
}
