
/*
������ʱ����
ʵ�ֶ�ʱÿ�궯�������Զ���1
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

