CREATE OR REPLACE
PROCEDURE pr_update_vaccine
/*
�����洢����pr_update_vaccine
���ڶ� VACCINE ������޸�
����p_out_resulutΪ�����������ʾ�Ƿ��޸ĳɹ�
����ͼƬ�Ƿ������ƶ��ط���ţ����ڴ˴�����
*/
(
P_VACCINE_ID in VARCHAR2,
P_VACCINE_TYPE in VARCHAR2,
p_VACCINE_NAME in VARCHAR2,
p_out_resulut out int
)AS
v_count int:=0;
BEGIN
  --�ж��Ƿ����
 select COUNT(*) into v_count
 from VACCINE
 where "VACCINE_ID" = P_VACCINE_ID;
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

 UPDATE "VACCINE"
 SET "VACCINE_TYPE" = P_VACCINE_TYPE, "VACCINE_NAME" = p_VACCINE_NAME
 WHERE "VACCINE_ID" = P_VACCINE_ID;
 p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;

END pr_update_vaccine;
