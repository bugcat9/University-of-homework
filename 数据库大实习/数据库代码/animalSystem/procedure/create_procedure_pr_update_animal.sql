CREATE OR REPLACE 
PROCEDURE pr_update_animal
/*
�����洢����pr_update_animal
���ڶ�ANIMAL������޸�
����p_out_resulutΪ�����������ʾ�Ƿ��޸ĳɹ�
����ͼƬ�Ƿ������ƶ��ط���ţ����ڴ˴�����
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
  --�ж��Ƿ����
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
