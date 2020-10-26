CREATE OR REPLACE 
PROCEDURE pr_update_animal
/*
创建存储过程pr_update_animal
用于对ANIMAL表进行修改
其中p_out_resulut为输出参数，表示是否修改成功
由于图片是服务器制定地方存放，不在此处设置
*/
(
P_ANIMAL_ID in VARCHAR2,
P_ANIMAL_NAME in VARCHAR2,
p_ANIMAL_SPECIES in VARCHAR2,
P_ANIMAL_AGE in Number,
P_SHELTER_ID in VARCHAR2,
p_out_resulut out int
)AS
v_count int:=0;  
BEGIN
  --判断是否存在
 select COUNT(*) into v_count
 from ANIMAL
 where "ANIMAL_ID" = P_ANIMAL_ID;
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

 UPDATE "ANIMAL" 
 SET "ANIMAL_NAME" = P_ANIMAL_NAME, "ANIMAL_SPECIES" = p_ANIMAL_SPECIES, "ANIMAL_AGE" = P_ANIMAL_AGE,  "SHELTER_ID" = P_SHELTER_ID 
 WHERE "ANIMAL_ID" = P_ANIMAL_ID;
 p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
    
END pr_update_animal;
