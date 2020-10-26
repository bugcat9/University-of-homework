package com.zhouning.animalsystem.repository;

import com.zhouning.animalsystem.entity.shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface shelterRepository extends JpaRepository<shelter,String> {

    //调用插入的存储过程
    @Procedure(name = "pr_insert_into_shelter")
    Integer pr_insert_into_shelter(@Param("P_SHELTER_ID") String P_SHELTER_ID,
                                   @Param("P_SHELTER_NAME") String P_SHELTER_NAME,
                                   @Param("p_SHELTER_ADDRESS") String p_SHELTER_ADDRESS,
                                   @Param("P_POSTCODE") String P_POSTCODE,
                                   @Param("P_SUM_ROOMS") Integer P_SUM_ROOMS,
                                   @Param("P_REMAIN_ROOMS") Integer P_REMAIN_ROOMS,
                                   @Param("P_SHELTER_COMMENT") String P_SHELTER_COMMENT);

    //调用更新的的存储过程
    @Procedure(name = "pr_update_shelter")
    Integer pr_update_shelter(@Param("P_SHELTER_ID") String P_SHELTER_ID,
                              @Param("P_SHELTER_NAME") String P_SHELTER_NAME,
                              @Param("p_SHELTER_ADDRESS") String p_SHELTER_ADDRESS,
                              @Param("P_POSTCODE") String P_POSTCODE,
                              @Param("P_SUM_ROOMS") Integer P_SUM_ROOMS,
                              @Param("P_REMAIN_ROOMS") Integer P_REMAIN_ROOMS,
                              @Param("P_SHELTER_COMMENT") String P_SHELTER_COMMENT);

    //调用删除的的存储过程
    @Procedure(name = "pr_delete_shelter")
    Integer pr_delete_shelter(@Param("P_SHELTER_ID") String P_SHELTER_ID);
}
