package com.zhouning.animalsystem.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "HEALTH_VIEW")

//存储过程调用
@NamedStoredProcedureQueries({
        //进行插入的存储过程
        @NamedStoredProcedureQuery(name = "pr_insert_into_health",procedureName = "pr_insert_into_health",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_USER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_CHECK_DATE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_HEALTH_INFORMATION",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_REMARKS",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行修改的存储过程
        @NamedStoredProcedureQuery(name = "pr_update_health",procedureName = "pr_update_health",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_USER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_CHECK_DATE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_HEALTH_INFORMATION",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_REMARKS",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行删除的存储过程
        @NamedStoredProcedureQuery(name = "pr_delete_health",procedureName = "pr_delete_health",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_USER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_CHECK_DATE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
})
public class health {

    @Id
    @Column(name = "ANIMAL_ID")
    private String animal_ID;


    @Column(name = "ANIMAL_NAME")
    private String animal_name;

    @Column(name = "USER_ID")
    private String user_ID;

    @Column(name = "USER_NAME")
    private String user_name;

    @Column(name = "HEALTH_INFORMATION")
    private String health_information;

    @Column(name = "CHECK_DATE")
    private String check_date;

    public String getAnimal_ID() {
        return animal_ID;
    }

    public void setAnimal_ID(String animal_ID) {
        this.animal_ID = animal_ID;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setHealth_information(String health_information) {
        this.health_information = health_information;
    }

    public void setCheck_date(String check_date) {
        this.check_date = check_date;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getHealth_information() {
        return health_information;
    }

    public String getCheck_date() {
        return check_date;
    }

    public String getRemarks() {
        return remarks;
    }

    @Column(name = "REMARKS")
    private String remarks;
}
