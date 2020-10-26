CREATE OR REPLACE 
PROCEDURE pr_insert_into_animal
/*
�����洢����pr_insert_into_animal
����������
	���ڶ�ANIMAL����в��룬����ͼƬ�Ƿ������ƶ��ط���ţ����ڴ˴�����
���룺
	P_ANIMAL_ID in VARCHAR2,
	P_ANIMAL_NAME in VARCHAR2,
	p_ANIMAL_SPECIES in VARCHAR2,
	P_ANIMAL_AGE in Number,
	P_SHELTER_ID in VARCHAR2,
������
	����p_out_resulutΪ�����������ʾ�Ƿ����ɹ�

*/
(
P_ANIMAL_ID in VARCHAR2,
P_ANIMAL_NAME in VARCHAR2,
p_ANIMAL_SPECIES in VARCHAR2,
P_ANIMAL_AGE in Number,
P_SHELTER_ID in VARCHAR2,
p_out_resulut out int
)AS
BEGIN
 INSERT INTO "ANIMAL"("ANIMAL_ID", "ANIMAL_NAME", "ANIMAL_SPECIES", "ANIMAL_AGE", "ANIMAL_IMAGE", "SHELTER_ID") 
 VALUES (P_ANIMAL_ID, P_ANIMAL_NAME, p_ANIMAL_SPECIES, P_ANIMAL_AGE, NULL, P_SHELTER_ID);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_animal;
