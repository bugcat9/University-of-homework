/*
创建触发器，
用于维护 Animal 表中图片存放的位置
*/
CREATE OR REPLACE TRIGGER tr_image
After INSERT OR UPDATE
ON Animal
FOR EACH ROW
Begin
  UPDATE Animal
  SET  ANIMAL_IMAGE = 'E:'  --在之后可以进行修改
  WHERE ANIMAL_ID= :new.ANIMAL_ID;
END;
