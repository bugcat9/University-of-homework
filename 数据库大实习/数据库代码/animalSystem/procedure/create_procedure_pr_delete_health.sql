CREATE OR REPLACE
PROCEDURE pr_delete_health
/*
�����洢����pr_delete_shelter
����������
	����ɾ�� SHELTER ���е�һ������
���룺
	P_ANIMAL_ID in VARCHAR2,����id
	P_USER_ID in VARCHAR2,�û�id
	p_CHECK_DATE in VARCHAR2,�������
������
	p_out_resulut int �����Ƿ�ɾ���ɹ�
*/
(
P_ANIMAL_ID in VARCHAR2,
P_USER_ID in VARCHAR2,
p_CHECK_DATE in VARCHAR2,
p_out_resulut out int
)
AS
v_count int:=0;
BEGIN
    --�ж��Ƿ����
 select COUNT(*) into v_count
 from HEALTH_CHECK_INFORMATION
 where "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = P_USER_ID AND "CHECK_DATE" = TO_DATE(p_CHECK_DATE,'YYYY-MM-DD');
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

  DELETE
  FROM HEALTH_CHECK_INFORMATION
  where "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = P_USER_ID AND "CHECK_DATE" = TO_DATE(p_CHECK_DATE,'YYYY-MM-DD');
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_delete_health;
