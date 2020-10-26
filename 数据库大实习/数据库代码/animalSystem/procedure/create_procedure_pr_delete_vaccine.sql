CREATE OR REPLACE 
PROCEDURE pr_delete_vaccine
/*
�����洢���� pr_delete_vaccine
����������
����ɾ�� VACCINE ���е�һ������
���룺
	P_VACCINE_ID VARCHAR2������id
������
	p_out_resulut int�������Ƿ�ɾ���ɹ�
*/
(
P_VACCINE_ID in VARCHAR2,
p_out_resulut out int
)
AS
v_count int:=0; 
BEGIN
  --�ж��Ƿ����
 select COUNT(*) into v_count
 from VACCINE
 where "VACCINE_ID" = P_VACCINE_ID;
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

  DELETE
  FROM VACCINE 
  where "VACCINE_ID" = P_VACCINE_ID;
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_delete_vaccine;
