package com.zhouning.animalsystem.repository;

import com.zhouning.animalsystem.entity.health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface healthRepository extends JpaRepository<health,String> {
    //调用插入的存储过程
    @Procedure(name = "pr_insert_into_health")
    Integer pr_insert_into_health(@Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                                  @Param("P_USER_ID") String P_USER_ID,
                                  @Param("p_CHECK_DATE") String p_CHECK_DATE,
                                  @Param("P_HEALTH_INFORMATION") String P_HEALTH_INFORMATION,
                                  @Param("P_REMARKS") String P_REMARKS);

    //调用更新的的存储过程
    @Procedure(name = "pr_update_health")
    Integer pr_update_health(@Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                             @Param("P_USER_ID") String P_USER_ID,
                             @Param("p_CHECK_DATE") String p_CHECK_DATE,
                             @Param("P_HEALTH_INFORMATION") String P_HEALTH_INFORMATION,
                             @Param("P_REMARKS") String P_REMARKS);

    //调用删除的的存储过程
    @Procedure(name = "pr_delete_health")
    Integer pr_delete_health(@Param("P_ANIMAL_ID") String P_ANIMAL_ID,
                             @Param("P_USER_ID") String P_USER_ID,
                             @Param("p_CHECK_DATE") String p_CHECK_DATE);
}
