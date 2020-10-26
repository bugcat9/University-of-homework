/*
创建触发器，
*/
CREATE OR REPLACE TRIGGER tr_room
BEFORE INSERT OR UPDATE
ON SHELTER
FOR EACH ROW
Begin
  if :new.SUM_ROOMS < :new.REMAIN_ROOMS
  then Raise_application_error(-20010,'剩余房间数量不能多余房间总数');
  end if;
END;
  
  
