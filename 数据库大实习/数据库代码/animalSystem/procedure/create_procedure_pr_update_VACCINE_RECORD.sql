CREATE OR REPLACE
PROCEDURE pr_update_VACCINE_RECORD
/*
创建存储过程 pr_update_VACCINE_RECORD
用于对 VACCINE_RECORD 表进行修改
其中p_out_resulut为输出参数，表示是否修改成功
由于图片是服务器制定地方存放，不在此处设置
*/
(
P_VACCINE_ID in VARCHAR2,
P_ANIMAL_ID in VARCHAR2,
p_USER_ID in VARCHAR2,
P_VACCINATION_TIME in VARCHAR2,
P_REMARKS in VARCHAR2,
p_out_resulut out int
)AS
v_count int:=0;
BEGIN
  --判断是否存在
 select COUNT(*) into v_count
 from VACCINE_RECORD
 WHERE "VACCINE_ID" = P_VACCINE_ID AND "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = p_USER_ID AND "VACCINATION_TIME" = TO_DATE(P_VACCINATION_TIME,'YYYY-MM-DD');
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

 UPDATE "VACCINE_RECORD"
 SET "REMARKS" = P_REMARKS
 WHERE "VACCINE_ID" = P_VACCINE_ID AND "ANIMAL_ID" = P_ANIMAL_ID AND "USER_ID" = p_USER_ID AND "VACCINATION_TIME" = TO_DATE(P_VACCINATION_TIME,'YYYY-MM-DD');
 p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;

END pr_update_VACCINE_RECORD;
