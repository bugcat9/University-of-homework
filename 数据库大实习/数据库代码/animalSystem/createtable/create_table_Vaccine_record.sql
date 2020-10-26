--创建疫苗记录表（打疫苗的记录）
create table Vaccine_record(
vaccine_ID varchar2(20),
animal_ID varchar2(20),
user_ID varchar2(20),
Vaccination_time Date,
Remarks varchar2(50),
constraint PK_Vaccine_record primary key (vaccine_ID,animal_ID,user_ID,Vaccination_time) --添加主键
) 
