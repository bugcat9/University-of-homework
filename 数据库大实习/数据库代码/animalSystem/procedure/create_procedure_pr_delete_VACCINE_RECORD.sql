CREATE OR REPLACE
PROCEDURE pr_delete_VACCINE_RECORD
/*
创建存储过程 pr_delete_VACCINE_RECORD
功能描述：
	用于删除 VACCINE_RECORD 表中的一行数据
传入：
	P_VACCINE_ID in VARCHAR2,疫苗id
	P_ANIMAL_ID in VARCHAR2,动物id
	p_USER_ID in VARCHAR2,用户id
	P_VACCINATION_TIME in VARCHAR2,疫苗注射时间
传出：
	p_out_resulut int，代表是否删除成功
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
  --判断是否存在
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
