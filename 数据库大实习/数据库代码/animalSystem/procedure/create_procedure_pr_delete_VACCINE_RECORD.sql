CREATE OR REPLACE
PROCEDURE pr_delete_VACCINE_RECORD
/*
�����洢���� pr_delete_VACCINE_RECORD
����������
	����ɾ�� VACCINE_RECORD ���е�һ������
���룺
	P_VACCINE_ID in VARCHAR2,����id
	P_ANIMAL_ID in VARCHAR2,����id
	p_USER_ID in VARCHAR2,�û�id
	P_VACCINATION_TIME in VARCHAR2,����ע��ʱ��
������
	p_out_resulut int�������Ƿ�ɾ���ɹ�
*/
(
P_VACCINE_ID in VARCHAR2,
P_ANIMAL_ID in VARCHAR2,
p_USER_ID in VARCHAR2,
P_VACCINATION_TIME in VARCHAR2,
p_out_resulut out int
)
AS
v_count int:=0;
BEGIN
  --�ж��Ƿ����
 select COUNT(*) into v_count
 from VACCINE_RECORD
 WHERE "VACCINE_ID" = P_VACCINE_ID AND "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = p_USER_ID AND "VACCINATION_TIME" = TO_DATE(        P_VACCINATION_TIME,'YYYY-MM-DD');
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

  DELETE
  FROM VACCINE_RECORD
  WHERE "VACCINE_ID" = P_VACCINE_ID AND "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = p_USER_ID AND "VACCINATION_TIME" = TO_DATE(P_VACCINATION_TIME,'YYYY-MM-DD');
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_delete_VACCINE_RECORD;
