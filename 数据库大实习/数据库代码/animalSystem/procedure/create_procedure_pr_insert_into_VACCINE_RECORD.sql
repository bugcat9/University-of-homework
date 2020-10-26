CREATE OR REPLACE
PROCEDURE pr_insert_into_VACCINE_RECORD
/*
创建存储过程 pr_insert_into_VACCINE_RECORD
用于对 VACCINE_RECORD 表进行插入
其中p_out_resulut为输出参数，表示是否插入成功
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
BEGIN
 INSERT INTO "VACCINE_RECORD"("VACCINE_ID", "ANIMAL_ID", "USER_ID", "VACCINATION_TIME", "REMARKS")
 VALUES (P_VACCINE_ID, P_ANIMAL_ID,p_USER_ID, TO_DATE(P_VACCINATION_TIME,'YYYY-MM-DD'),P_REMARKS);
  p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
END pr_insert_into_VACCINE_RECORD;
