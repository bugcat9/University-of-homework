/*
������������
*/
CREATE OR REPLACE TRIGGER tr_room
BEFORE INSERT OR UPDATE
ON SHELTER
FOR EACH ROW
Begin
  if :new.SUM_ROOMS < :new.REMAIN_ROOMS
  then Raise_application_error(-20010,'ʣ�෿���������ܶ��෿������');
  end if;
END;
  
  
