CREATE OR REPLACE
PROCEDURE pr_insert_into_health
/*
pr_insert_into_health
����������
	���ڶ� HEALTH_CHECK_INFORMATION ����в���
���룺
	P_ANIMAL_ID in VARCHAR2,
	P_USER_ID in VARCHAR2,
	p_CHECK_DATE in VARCHAR2,
	P_HEALTH_INFORMATION in VARCHAR2,
	P_REMARKS in VARCHAR2,
������
	p_out_resulut int����ʾ�Ƿ����ɹ�
*/
(
P_ANIMAL_ID in VARCHAR2,
P_USER_ID in VARCHAR2,
p_CHECK_DATE in VARCHAR2,
P_HEALTH_INFORMATION in VARCHAR2,
P_REMARKS in VARCHAR2,
p_out_resulut out int
)AS
BEGIN
 INSERT INTO "HEALTH_CHECK_INFORMATION"("ANIMAL_ID", "USER_ID", "CHECK_DATE", "HEALTH_INFORMATION", "REMARKS")
 VALUES (P_ANIMAL_ID, P_USER_ID, TO_DATE(p_CHECK_DATE,'YYYY-MM-DD'), P_HEALTH_INFORMATION, P_REMARKS);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_health;
