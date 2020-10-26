CREATE OR REPLACE 
PROCEDURE pr_update_shelter
/*
pr_update_shelter
用于对 SHELTER 表进行修改
其中p_out_resulut为输出参数，表示是否修改成功
*/
(
P_SHELTER_ID in VARCHAR2,
P_SHELTER_NAME in VARCHAR2,
p_SHELTER_ADDRESS in VARCHAR2,
P_POSTCODE in VARCHAR2,
P_SUM_ROOMS in NUMBER,
P_REMAIN_ROOMS in NUMBER,
P_SHELTER_COMMENT in VARCHAR2,
p_out_resulut out int
)AS
v_count int:=0;  
BEGIN
  --判断是否存在
 select COUNT(*) into v_count
 from SHELTER
 where "SHELTER_ID" = P_SHELTER_ID;
 if v_count =0 then
    p_out_resulut := 0;return;
  end if;

UPDATE "SHELTER" 
SET "SHELTER_NAME" = P_SHELTER_NAME, "SHELTER_ADDRESS" = p_SHELTER_ADDRESS, "POSTCODE" = P_POSTCODE, "SUM_ROOMS" = P_SUM_ROOMS, 
"REMAIN_ROOMS" = P_REMAIN_ROOMS, "SHELTER_COMMENT" = P_SHELTER_COMMENT 
WHERE "SHELTER_ID" = P_SHELTER_ID;

 p_out_resulut:=1;
exception
  when others then
    p_out_resulut:=0;
    
END pr_update_shelter;
