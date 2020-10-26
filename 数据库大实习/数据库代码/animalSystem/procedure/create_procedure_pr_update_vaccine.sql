CREATE OR REPLACE
PROCEDURE pr_update_vaccine
/*
创建存储过程pr_update_vaccine
用于对 VACCINE 表进行修改
其中p_out_resulut为输出参数，表示是否修改成功
由于图片是服务器制定地方存放，不在此处设置
*/
(
P_VACCINE_ID in VARCHAR2,
P_VACCINE_TYPE in VARCHAR2,
p_VACCINE_NAME in VARCHAR2,
p_out_resulut out int
)AS
v_count int:=0;
BEGIN
  --判断是否存在
 select COUNT(*) into v_count
 from VACCINE
 where "VACCINE_ID" = P_VACCINE_ID;
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

 UPDATE "VACCINE"
 SET "VACCINE_TYPE" = P_VACCINE_TYPE, "VACCINE_NAME" = p_VACCINE_NAME
 WHERE "VACCINE_ID" = P_VACCINE_ID;
 p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;

END pr_update_vaccine;
