package com.activiti.mapper;


import com.activiti.pojo.Accounts;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

public interface AccountsMapper extends Mapper<Accounts> {

  Accounts selectByParams(Map<String,Object> params);
}
