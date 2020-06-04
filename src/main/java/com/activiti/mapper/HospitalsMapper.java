package com.activiti.mapper;


import com.activiti.pojo.Hospitals;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface HospitalsMapper extends Mapper<Hospitals> {

  List<Hospitals> selectByParams(Map<String,Object> params);
}
