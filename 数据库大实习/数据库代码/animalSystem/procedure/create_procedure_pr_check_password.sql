CREATE OR REPLACE
PROCEDURE pr_check_password
/********************************************
功能描述：
  检查用户密码是否正确
传入：
  user_id varchar2(20)，用户id
  user_password varchar2(50)，传进来的密码，可能根数据库中的表不一样
传出：
  out_result out number 最终判断密码是否正确的结果,如果正确为1，不正确为0
********************************************/
(
u_id in varchar2,
u_password in varchar2,
out_result out int
)
As
  v_count int:=0;
begin
  dbms_output.put_line(u_id);
  dbms_output.put_line(u_password);
  
  if (u_id is null or u_password is null)
  then
    out_result := 0;
    return;
  end if;
  select COUNT(*) into v_count
  from USER_INFORMATION
  where USER_ID = u_id and
        USER_PASSWORD = u_password ;

  dbms_output.put_line(v_count);
  if v_count =1 then
    out_result := 1;return;
  else
    out_result := 0;return;
  end if;
  
end pr_check_password;
