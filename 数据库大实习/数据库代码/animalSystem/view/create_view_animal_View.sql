--������ͼanimal_view
--�����û��鿴�������Ϣ

CREATE OR REPLACE VIEW animal_view as
(
select ANIMAL_ID,ANIMAL_NAME,ANIMAL_SPECIES��ANIMAL_AGE,SHELTER_ID
from  ANIMAL
)
