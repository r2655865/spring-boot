-- 修改kb_teach_course表,并添加 kb_course_candidate表
drop procedure if exists CreateHospitals;
delimiter $$
create procedure CreateHospitals() begin

  if NOT exists (select * FROM information_schema.columns WHERE table_schema = DATABASE()  AND table_name = 'kb_hospitals') then

    create table kb_hospitals
    (
       id                                   varchar(40) not null comment '主键',
       name                                 varchar(20) default NULL comment '医院名称',
       sign_code                             varchar(50) default NULL comment '医院标识',
       status                               bit default 1 comment '是否启动',
       created                              timestamp default CURRENT_TIMESTAMP,
       primary key (id)
    );
  end if;

end $$
delimiter ;

call CreateHospitals();
drop procedure if exists CreateHospitals;