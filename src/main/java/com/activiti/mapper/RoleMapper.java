package com.activiti.mapper;


import com.activiti.pojo.Role;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends Mapper<Role> {

   List<Role> listQueryByPage(Map<String,Object> map);
}
