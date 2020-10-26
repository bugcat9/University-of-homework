package com.zhouning.animalsystem.repository;

import com.zhouning.animalsystem.entity.vaccineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface vaccineRecordRepository extends JpaRepository<vaccineRecord,String> {
    //调用插入的存储过程
    @Procedure(name = "pr_insert_into_VACCINE_RECORD")
    Integer pr_insert_into_VACCINE_RECORD(@Param("P_VACCINE_ID") String P_VACCINE_ID,
                                          @Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                                          @Param("p_USER_ID") String p_USER_ID,
                                          @Param("P_VACCINATION_TIME") String P_VACCINATION_TIME,
                                          @Param("P_REMARKS") String P_REMARKS
                                       );

    //调用更新的的存储过程
    @Procedure(name = "pr_update_VACCINE_RECORD")
    Integer pr_update_VACCINE_RECORD(@Param("P_VACCINE_ID") String P_VACCINE_ID,
                                     @Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                                     @Param("p_USER_ID") String p_USER_ID,
                                     @Param("P_VACCINATION_TIME") String P_VACCINATION_TIME,
                                     @Param("P_REMARKS") String P_REMARKS
                                        );

    //调用删除的的存储过程
    @Procedure(name = "pr_delete_VACCINE_RECORD")
    Integer pr_delete_VACCINE_RECORD(@Param("P_VACCINE_ID") String P_VACCINE_ID,
                                     @Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                                     @Param("p_USER_ID") String p_USER_ID,
                                     @Param("P_VACCINATION_TIME") String P_VACCINATION_TIME);
}
