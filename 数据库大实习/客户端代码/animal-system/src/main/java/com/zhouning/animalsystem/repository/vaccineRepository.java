package com.zhouning.animalsystem.repository;

import com.zhouning.animalsystem.entity.vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface vaccineRepository extends JpaRepository<vaccine,String> {

//    //
//    @Query("select ")
//    List<vaccine> findAllBy();

    //调用插入的存储过程
    @Procedure(name = "pr_insert_into_vaccine")
    Integer pr_insert_into_animal(@Param("P_VACCINE_ID") String P_ANIMAL_ID,
                                  @Param("P_VACCINE_TYPE") String P_ANIMAL_NAME,
                                  @Param("p_VACCINE_NAME") String p_ANIMAL_SPECIES
                                  );

    //调用更新的的存储过程
    @Procedure(name = "pr_update_vaccine")
    Integer pr_update_shelter(@Param("P_VACCINE_ID") String P_ANIMAL_ID,
                             @Param("P_VACCINE_TYPE") String P_ANIMAL_NAME,
                             @Param("p_VACCINE_NAME") String p_ANIMAL_SPECIES);

    //调用删除的的存储过程
    @Procedure(name = "pr_delete_vaccine")
    Integer pr_delete_vaccine(@Param("P_VACCINE_ID") String P_VACCINE_ID);
}
