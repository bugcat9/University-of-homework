CREATE OR REPLACE
PROCEDURE pr_check_password
/********************************************
����������
  ����û������Ƿ���ȷ
���룺
  user_id varchar2(20)���û�id
  user_password varchar2(50)�������������룬���ܸ����ݿ��еı�һ��
������
  out_result out number �����ж������Ƿ���ȷ�Ľ��,�����ȷΪ1������ȷΪ0
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
