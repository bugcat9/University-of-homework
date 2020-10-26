package com.zhouning.animalsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "VACCINE_RECORD_VIEW")
//存储过程调用
@NamedStoredProcedureQueries({
        //进行插入的存储过程
        @NamedStoredProcedureQuery(name = "pr_insert_into_VACCINE_RECORD",procedureName = "pr_insert_into_VACCINE_RECORD",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_USER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINATION_TIME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_REMARKS",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行修改的存储过程
        @NamedStoredProcedureQuery(name = "pr_update_VACCINE_RECORD",procedureName = "pr_update_VACCINE_RECORD",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_USER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINATION_TIME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_REMARKS",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行删除的存储过程
        @NamedStoredProcedureQuery(name = "pr_delete_VACCINE_RECORD",procedureName = "pr_delete_VACCINE_RECORD",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_USER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINATION_TIME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
})
public class vaccineRecord {
    @Id
    @Column(name = "USER_ID")
    private String user_ID;

    @Column(name = "USER_NAME")
    private String user_NAME;

    @Column(name = "VACCINE_ID")
    private String vaccine_ID;
    @Column(name = "vaccine_name")
    private String vaccine_name;

    @Column(name = "ANIMAL_ID")
    private String animal_ID;
    @Column(name = "ANIMAL_NAME")
    private String animal_name;

    @Column(name = "VACCINATION_TIME")
    private String vaccination_time;

    @Column(name = "REMARKS")
    private String remarks;


    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public void setUser_NAME(String user_NAME) {
        this.user_NAME = user_NAME;
    }

    public void setVaccine_ID(String vaccine_ID) {
        this.vaccine_ID = vaccine_ID;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public void setAnimal_ID(String animal_ID) {
        this.animal_ID = animal_ID;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public void setVaccination_time(String vaccination_time) {
        this.vaccination_time = vaccination_time;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public String getUser_NAME() {
        return user_NAME;
    }

    public String getVaccine_ID() {
        return vaccine_ID;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public String getAnimal_ID() {
        return animal_ID;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public String getVaccination_time() {
        return vaccination_time;
    }

    public String getRemarks() {
        return remarks;
    }



}
