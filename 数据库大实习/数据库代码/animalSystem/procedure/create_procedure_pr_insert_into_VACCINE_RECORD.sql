CREATE OR REPLACE
PROCEDURE pr_insert_into_VACCINE_RECORD
/*
�����洢���� pr_insert_into_VACCINE_RECORD
���ڶ� VACCINE_RECORD ����в���
����p_out_resulutΪ�����������ʾ�Ƿ����ɹ�
����ͼƬ�Ƿ������ƶ��ط���ţ����ڴ˴�����
*/
(
P_VACCINE_ID in VARCHAR2,
P_ANIMAL_ID in VARCHAR2,
p_USER_ID in VARCHAR2,
P_VACCINATION_TIME in VARCHAR2,
P_REMARKS in VARCHAR2,
p_out_resulut out int
)AS
BEGIN
 INSERT INTO "VACCINE_RECORD"("VACCINE_ID", "ANIMAL_ID", "USER_ID", "VACCINATION_TIME", "REMARKS")
 VALUES (P_VACCINE_ID, P_ANIMAL_ID,p_USER_ID, TO_DATE(P_VACCINATION_TIME,'YYYY-MM-DD'),P_REMARKS);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_VACCINE_RECORD;
