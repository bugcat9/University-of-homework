CREATE OR REPLACE
PROCEDURE pr_delete_health
/*
创建存储过程pr_delete_shelter
功能描述：
	用于删除 SHELTER 表中的一行数据
传入：
	P_ANIMAL_ID in VARCHAR2,动物id
	P_USER_ID in VARCHAR2,用户id
	p_CHECK_DATE in VARCHAR2,检查日期
传出：
	p_out_resulut int 代表是否删除成功
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
    --判断是否存在
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
