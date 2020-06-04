package com.activiti.service;


import com.activiti.mapper.AccountsMapper;
import com.activiti.pojo.Accounts;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;


import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AccountsService extends BaseService<Accounts>{

    @Autowired
    AccountsMapper accountsMapper;


    public Accounts selectByParams(Map<String,Object> params) {

        return accountsMapper.selectByParams(params);
    }


    @Override
    public Mapper<Accounts> getMapper() {
        return accountsMapper;
    }

    @Override
    public PageInfo listByPage(Map<String, Object> params) {
        return null;
    }
}
