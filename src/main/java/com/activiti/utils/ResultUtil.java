package com.activiti.utils;

import com.activiti.pojo.Result;

public class ResultUtil {


    /**
     * 操作数据返回结果
     * @param count
     * @param object
     * @param msg
     * @return
     */
    public static Result operatDataResult(int count,Object object,String msg) {

        Result result = new Result();

        if(count > 0){
            result.setStatus(true);
            result.setMsg(msg);
            result.setData(object);
        }else{
            result.setStatus(false);
            result.setMsg(msg);
            result.setData(object);
        }

        return result;
    }


    /**
     * 返回查询数据
     * @param msg
     * @param o
     * @return
     */
    public static Result selectData(String msg , Object o) {
        return new Result(true, msg, o);
    }



    public static Result error(String msg) {
        Result result = new Result();
        result.setStatus(false);
        result.setMsg(msg);
        return result;
    }
}
