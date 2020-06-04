drop procedure if exists creatUserTable;
delimiter $$
create procedure creatUserTable() begin
  if NOT exists (select * FROM information_schema.columns WHERE table_schema = DATABASE()  AND table_name = 'kb_accounts') then
    create table  if not exists  kb_accounts
    (
       id                   varchar(40) not null,
       user_name            varchar(40) not null,
       pass_word            varchar(40) not null,
       nick_name            varchar(20),
       mobile               varchar(20),
       status               int,
       created              timestamp default CURRENT_TIMESTAMP,
       primary key (id)
    );
    alter table kb_accounts comment '用户表';
  end if;
end $$
delimiter ;

call creatUserTable();
drop procedure if exists creatUserTable;