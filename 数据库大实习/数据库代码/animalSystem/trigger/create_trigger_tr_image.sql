/*
������������
����ά�� Animal ����ͼƬ��ŵ�λ��
*/
CREATE OR REPLACE TRIGGER tr_image
After INSERT OR UPDATE
ON Animal
FOR EACH ROW
Begin
  UPDATE Animal
  SET  ANIMAL_IMAGE = 'E:'  --��֮����Խ����޸�
  WHERE ANIMAL_ID= :new.ANIMAL_ID;
END;
