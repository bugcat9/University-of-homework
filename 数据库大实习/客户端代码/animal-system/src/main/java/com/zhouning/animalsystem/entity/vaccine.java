package com.zhouning.animalsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "VACCINE")
//存储过程调用
@NamedStoredProcedureQueries({
        //进行插入的存储过程
        @NamedStoredProcedureQuery(name = "pr_insert_into_vaccine",procedureName = "pr_insert_into_vaccine",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_TYPE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_VACCINE_NAME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行修改的存储过程
        @NamedStoredProcedureQuery(name = "pr_update_vaccine",procedureName = "pr_update_vaccine",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_TYPE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_VACCINE_NAME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行删除的存储过程
        @NamedStoredProcedureQuery(name = "pr_delete_vaccine",procedureName = "pr_delete_vaccine",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_VACCINE_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
})
public class vaccine {
    @Id
    @Column(name = "VACCINE_ID")
    private String vaccine_ID;

    @Column(name = "VACCINE_TYPE")
    private String vaccine_type;

    @Column(name = "VACCINE_NAME")
    private String vaccine_name;

    public void setVaccine_ID(String vaccine_ID) {
        this.vaccine_ID = vaccine_ID;
    }

    public void setVaccine_type(String vaccine_type) {
        this.vaccine_type = vaccine_type;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public String getVaccine_ID() {
        return vaccine_ID;
    }

    public String getVaccine_type() {
        return vaccine_type;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }
}
