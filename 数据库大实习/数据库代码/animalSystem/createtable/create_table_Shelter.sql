
--收容所信息表
create TABLE Shelter(
Shelter_ID varchar2(20) primary key,  --主键
shelter_name varchar2(50),
shelter_address varchar2(50),
postcode varchar2(20),
sum_rooms Number(4),--总房间数
remain_rooms Number(4),--剩余房间数
Shelter_comment varchar2(30)---备注
);
