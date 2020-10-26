CREATE OR REPLACE
PROCEDURE pr_update_health
/*
pr_insert_into_health
���ڶ� HEALTH_CHECK_INFORMATION ����в���
����p_out_resulutΪ�����������ʾ�Ƿ����ɹ�
*/
(
P_ANIMAL_ID in VARCHAR2,
P_USER_ID in VARCHAR2,
p_CHECK_DATE in VARCHAR2,
P_HEALTH_INFORMATION in VARCHAR2,
P_REMARKS in VARCHAR2,
p_out_resulut out int
)AS
v_count int:=0;
BEGIN
  --�ж��Ƿ����
 select COUNT(*) into v_count
 from HEALTH_CHECK_INFORMATION
 where "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = P_USER_ID AND "CHECK_DATE" = TO_DATE(p_CHECK_DATE,'YYYY-MM-DD');
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

 UPDATE "HEALTH_CHECK_INFORMATION"
 SET "HEALTH_INFORMATION" = P_HEALTH_INFORMATION, "REMARKS" = P_REMARKS
 WHERE "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = P_USER_ID AND "CHECK_DATE" = TO_DATE(p_CHECK_DATE,'YYYY-MM-DD');
 p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;

END pr_update_health;
