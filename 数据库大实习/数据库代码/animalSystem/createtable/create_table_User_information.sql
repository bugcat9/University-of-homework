--创建用户信息表
create TABLE User_information(
user_ID varchar2(20) primary key,
user_name  varchar2(50) not null,
user_password varchar2(50) not null,
email varchar2(50),
cellphone_number varchar2(14),
Shelter_ID varchar2(20)，  
admin_ID varchar2(20)，
constraint FK_User_Shelter_ID foreign key (Shelter_ID) references Shelter(Shelter_ID)，  --外键参考收容所id
constraint FK_admin_ID foreign key (admin_ID) references administrator(admin_ID)  --外键参考管理员id
);
