package com.zhouning.animalsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "ANIMAL_VIEW")    //跟视图所关联
//存储过程调用
@NamedStoredProcedureQueries({
        //进行插入的存储过程
    @NamedStoredProcedureQuery(name = "pr_insert_into_animal",procedureName = "pr_insert_into_animal",
        parameters={
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_NAME",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_ANIMAL_SPECIES",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_AGE",type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_ID",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
        }
        ),
        //进行修改的存储过程
    @NamedStoredProcedureQuery(name = "pr_update_animal",procedureName = "pr_update_animal",
        parameters={
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_NAME",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_ANIMAL_SPECIES",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_AGE",type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_ID",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
        }
    ),
        //进行删除的存储过程
    @NamedStoredProcedureQuery(name = "pr_delete_animal",procedureName = "pr_delete_animal",
        parameters={
        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_ANIMAL_ID",type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
        }
    ),
})
public class animal {
    @Id
    @Column(name = "ANIMAL_ID")
    private String animal_ID;

    @Column(name = "ANIMAL_NAME")
    private String animal_name;

    @Column(name = "ANIMAL_SPECIES")
    private String animal_species;

    @Column(name = "ANIMAL_AGE")
    private Integer animal_age;

    @Column(name = "SHELTER_ID")
    private String Shelter_ID;

    public void setAnimal_ID(String animal_ID) {
        this.animal_ID = animal_ID;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public void setAnimal_species(String animal_species) {
        this.animal_species = animal_species;
    }

    public void setAnimal_age(Integer animal_age) {
        this.animal_age = animal_age;
    }

    public void setShelter_ID(String shelter_ID) {
        Shelter_ID = shelter_ID;
    }

    public String getAnimal_ID() {
        return animal_ID;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public String getAnimal_species() {
        return animal_species;
    }

    public Integer getAnimal_age() {
        return animal_age;
    }

    public String getShelter_ID() {
        return Shelter_ID;
    }
}
