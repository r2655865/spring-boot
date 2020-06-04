drop procedure if exists creatRoleTable;
delimiter $$
create procedure creatRoleTable() begin
  if NOT exists (select * FROM information_schema.columns WHERE table_schema = DATABASE()  AND table_name = 'kb_role') then
    create table  if not exists  kb_role
    (
       id                   varchar(40) NOT NULL,
       role_name             varchar(40) not null,
       code                 varchar(40) not null,
       created              timestamp default CURRENT_TIMESTAMP,
       primary key (id)
    );
    alter table kb_role comment '角色表';
  end if;


end $$
delimiter ;

call creatRoleTable();
drop procedure if exists creatRoleTable;