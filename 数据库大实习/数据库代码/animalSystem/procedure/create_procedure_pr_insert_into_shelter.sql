CREATE OR REPLACE 
PROCEDURE pr_insert_into_shelter
/*
pr_insert_into_shelter
����������
���ڶ� SHELTER ����в���
����p_out_resulutΪ�����������ʾ�Ƿ����ɹ�
*/
(
P_SHELTER_ID in VARCHAR2,
P_SHELTER_NAME in VARCHAR2,
p_SHELTER_ADDRESS in VARCHAR2,
P_POSTCODE in VARCHAR2,
P_SUM_ROOMS in NUMBER,
P_REMAIN_ROOMS in NUMBER,
P_SHELTER_COMMENT in VARCHAR2,
p_out_resulut out int
)AS
BEGIN
 INSERT INTO SHELTER("SHELTER_ID", "SHELTER_NAME", "SHELTER_ADDRESS", "POSTCODE", "SUM_ROOMS", "REMAIN_ROOMS", "SHELTER_COMMENT") 
 VALUES (P_SHELTER_ID, P_SHELTER_NAME, p_SHELTER_ADDRESS, P_POSTCODE, P_SUM_ROOMS, P_REMAIN_ROOMS, P_SHELTER_COMMENT);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_shelter;
