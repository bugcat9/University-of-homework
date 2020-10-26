package com.zhouning.animalsystem.repository;

import com.zhouning.animalsystem.entity.animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface animalRepository extends JpaRepository<animal,String> {

    //调用插入的存储过程
    @Procedure(name = "pr_insert_into_animal")
    Integer pr_insert_into_animal(@Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                                  @Param("P_ANIMAL_NAME") String P_ANIMAL_NAME,
                                  @Param("p_ANIMAL_SPECIES") String p_ANIMAL_SPECIES,
                                  @Param("P_ANIMAL_AGE") Integer P_ANIMAL_AGE,
                                  @Param("P_SHELTER_ID") String P_SHELTER_ID);

    //调用更新的的存储过程
    @Procedure(name = "pr_update_animal")
    Integer pr_update_animal(@Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                                  @Param("P_ANIMAL_NAME") String P_ANIMAL_NAME,
                                  @Param("p_ANIMAL_SPECIES") String p_ANIMAL_SPECIES,
                                  @Param("P_ANIMAL_AGE") Integer P_ANIMAL_AGE,
                                  @Param("P_SHELTER_ID") String P_SHELTER_ID);

    //调用删除的的存储过程
    @Procedure(name = "pr_delete_animal")
    Integer pr_delete_animal(@Param("P_ANIMAL_ID") String P_ANIMAL_ID);

}
