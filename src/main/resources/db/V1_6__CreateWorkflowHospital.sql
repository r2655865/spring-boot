drop procedure if exists CreateWorkFlowHospital;
delimiter $$
create procedure CreateWorkFlowHospital() begin

  if NOT exists (select * FROM information_schema.columns WHERE table_schema = DATABASE()  AND table_name = 'kb_workflow_hospital') then

    create table kb_workflow_hospital
    (
       id                                   varchar(40)  not null comment '主键',
       sign_code                            varchar(50)  default NULL comment '医院标识',
       task_def_key                         varchar(40)  default NULL comment '流程编码',
       process_instance_id                  varchar(20)  default NULL comment '流程定义KEY',
       created                              timestamp default CURRENT_TIMESTAMP,
       primary key (id)
    );
  end if;

end $$
delimiter ;

call CreateWorkFlowHospital();
drop procedure if exists CreateWorkFlowHospital;