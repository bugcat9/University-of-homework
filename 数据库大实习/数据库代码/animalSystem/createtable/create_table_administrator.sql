--创建超级管理员用户信息表
create TABLE administrator(
admin_ID varchar2(20) primary key,
admini_name varchar2(50) not null,
admini_password varchar2(50) not null
);
