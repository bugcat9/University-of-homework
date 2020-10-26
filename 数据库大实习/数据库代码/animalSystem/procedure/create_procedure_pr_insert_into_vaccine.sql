CREATE OR REPLACE 
PROCEDURE pr_insert_into_vaccine
/*
创建存储过程 pr_insert_into_vaccine
用于对 VACCINE 表进行插入
其中p_out_resulut为输出参数，表示是否插入成功
由于图片是服务器制定地方存放，不在此处设置
*/
(
P_VACCINE_ID in VARCHAR2,
P_VACCINE_TYPE in VARCHAR2,
p_VACCINE_NAME in VARCHAR2,
p_out_resulut out int
)AS
BEGIN
  INSERT INTO "VACCINE"("VACCINE_ID", "VACCINE_TYPE", "VACCINE_NAME") 
  VALUES (P_VACCINE_ID, P_VACCINE_TYPE, p_VACCINE_NAME);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_vaccine;
