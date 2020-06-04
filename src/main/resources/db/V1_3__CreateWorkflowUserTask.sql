drop procedure if exists CreateWorkflowUserTask;
delimiter $$
create procedure CreateWorkflowUserTask() begin

  if NOT exists (select * FROM information_schema.columns WHERE table_schema = DATABASE()  AND table_name = 'kb_workflow_user_task') then

    create table kb_workflow_user_task
    (
       id                   varchar(40) not null comment '主键',
       proc_def_key         varchar(100) default NULL comment '流程定义KEY',
       proc_def_name        varchar(50) default NULL comment '流程定义名称',
       task_def_key         varchar(100) default NULL comment '任务定义KEY',
       task_name            varchar(200) default NULL comment '任务名称',
       task_type            varchar(50) default NULL comment '任务类型',
       candidate_name       varchar(1000) default NULL comment '[受理人,候选人,候选组]名称',
       candidate_ids        varchar(2000) default NULL comment '[受理人,候选人,候选组]ID',
       status               int default 0,
       created              timestamp default CURRENT_TIMESTAMP,
       primary key (id)
    );

  end if;

end $$
delimiter ;

call CreateWorkflowUserTask();
drop procedure if exists CreateWorkflowUserTask;