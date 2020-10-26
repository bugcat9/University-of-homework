--创建视图animal_view
--方便用户查看动物的信息

CREATE OR REPLACE VIEW animal_view as
(
select ANIMAL_ID,ANIMAL_NAME,ANIMAL_SPECIES，ANIMAL_AGE,SHELTER_ID
from  ANIMAL
)
