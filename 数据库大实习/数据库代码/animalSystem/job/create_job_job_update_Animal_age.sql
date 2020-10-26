
/*
创建定时任务
实现定时每年动物年龄自动加1
*/
DECLARE 
  job number;
BEGIN
DBMS_JOB.SUBMIT(
  JOB => job,
  what => 'UPDATE ANIMAL
          SET ANIMAL.ANIMAL_AGE = ANIMAL.ANIMAL_AGE + 1;',
  INTERVAL =>'TRUNC(SYSDATE)+365'
);
commit;
END;

