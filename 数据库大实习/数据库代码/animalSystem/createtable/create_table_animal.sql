
--动物信息表
create TABLE animal(
animal_ID varchar2(20) primary key ,
animal_name varchar2(20),
animal_species varchar2(20),
animal_age Number (4),
animal_image varchar2 (50),
Shelter_ID varchar2(20),
--添加主键约束
constraint FK_animal_Shelter_ID foreign key (Shelter_ID) references Shelter(Shelter_ID)
)
