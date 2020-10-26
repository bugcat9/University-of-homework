CREATE OR REPLACE 
PROCEDURE pr_delete_shelter
/**
�����洢����pr_delete_shelter 
����������
	����ɾ�� SHELTER ���е�һ������
���룺
  P_SHELTER_ID varchar2��������idid
������
  out_result out number �����ж������Ƿ���ȷ�Ľ��,�����ȷΪ1������ȷΪ0
**/
(
P_SHELTER_ID in VARCHAR2,
p_out_resulut out int
)
AS
v_count int:=0; 
BEGIN
    --�ж��Ƿ����
 select COUNT(*) into v_count
 from SHELTER
 where "SHELTER_ID" = P_SHELTER_ID;
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;  

  DELETE
  FROM SHELTER 
  WHERE "SHELTER_ID" = P_SHELTER_ID;
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_delete_shelter;
