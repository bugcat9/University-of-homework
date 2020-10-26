CREATE OR REPLACE 
PROCEDURE pr_insert_into_vaccine
/*
�����洢���� pr_insert_into_vaccine
���ڶ� VACCINE ����в���
����p_out_resulutΪ�����������ʾ�Ƿ����ɹ�
����ͼƬ�Ƿ������ƶ��ط���ţ����ڴ˴�����
*/
(
P_VACCINE_ID in VARCHAR2,
P_VACCINE_TYPE in VARCHAR2,
p_VACCINE_NAME in VARCHAR2,
p_out_resulut out int
)AS
BEGIN
  INSERT INTO "VACCINE"("VACCINE_ID", "VACCINE_TYPE", "VACCINE_NAME") 
  VALUES (P_VACCINE_ID, P_VACCINE_TYPE, p_VACCINE_NAME);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_vaccine;
