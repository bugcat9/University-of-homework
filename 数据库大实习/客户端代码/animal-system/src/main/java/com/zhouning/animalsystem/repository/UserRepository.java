package com.zhouning.animalsystem.repository;


import com.zhouning.animalsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

//继承JpaRepository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("select u from User u where u.user_ID = ?1")
    User findByUserId(String user_ID);

    //调用存储过程判断密码是否正确
    @Procedure(name = "pr_check_password")
    Integer pr_check_password(@Param("u_id")String user_id,@Param("u_password")String user_password);
}
