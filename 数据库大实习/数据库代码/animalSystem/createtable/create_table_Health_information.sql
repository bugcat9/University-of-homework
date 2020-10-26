--建立检查健康的表格
create table Health_Check_information(
animal_ID varchar2(20),
user_ID varchar2(20),
Check_date Date,
Health_information varchar2(50),
Remarks varchar2(50), --备注
constraint PK_Health_Check_information primary key (animal_ID,user_ID,Check_date) --添加主键
)
