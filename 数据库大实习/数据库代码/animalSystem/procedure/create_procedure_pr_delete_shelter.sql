CREATE OR REPLACE
PROCEDURE pr_delete_shelter
/*
创建存储过程pr_delete_shelter
功能描述：
	用于删除 SHELTER 表中的一行数据
传入：
	P_SHELTER_ID VARCHAR2，收容所id
传出：
	p_out_resulut int，代表是否删除成功
*/
(
P_SHELTER_ID in VARCHAR2,
p_out_resulut out int
)
AS
v_count int:=0;
BEGIN
    --判断是否存在
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
