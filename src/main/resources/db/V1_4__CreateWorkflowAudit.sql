drop procedure if exists CreateWorkflowAudit;
delimiter $$
create procedure CreateWorkflowAudit() begin

  if NOT exists (select * FROM information_schema.columns WHERE table_schema = DATABASE()  AND table_name = 'kb_workflow_audit') then

    create table kb_workflow_audit
    (
       id                                   varchar(40) not null comment '主键',
       process_instance_id                  varchar(100) default NULL comment '流程定义KEY',
       taskId                               varchar(50) default NULL comment '任务id',
       task_name                            varchar(200) default NULL comment '任务名称',
       audit_role_code                      varchar(20) default NULL comment '审批人角色',
       audit_staffId                        varchar(40) default NULL comment '审批人员id',
       opinion                              varchar(100) default NULL comment '审批意见',
       status                               int default 0,
       apply_staffId                        varchar(50) default NULL comment '申请人id',
       created                              timestamp default CURRENT_TIMESTAMP,
       primary key (id)
    );
  end if;

end $$
delimiter ;

call CreateWorkflowAudit();
drop procedure if exists CreateWorkflowAudit;