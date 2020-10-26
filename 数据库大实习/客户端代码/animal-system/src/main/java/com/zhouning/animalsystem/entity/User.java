package com.zhouning.animalsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER_VIEW")
//登录查询密码的存储过程
@NamedStoredProcedureQuery(name = "pr_check_password",procedureName = "pr_check_password",
        parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN,name = "u_id",type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN,name = "u_password",type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT,name = "out_result",type = Integer.class)
        }
)
public class User {

    @Id
    @Column(name = "USER_ID")
    private   String user_ID;

    @Column(name = "USER_NAME")
    private  String user_name;

//    @Column(name = "USER_PASSWORD")
//    private  String user_password;

    @Column(name = "EMAIL")
    private  String email;

    @Column(name = "CELLPHONE_NUMBER")
    private  String cellphone_number;

    @Column(name = "SHELTER_ID")
    private  String Shelter_ID;

    @Column(name = "SHELTER_NAME")
    private String Shelter_Name;

    public void setShelter_Name(String shelter_Name) {
        Shelter_Name = shelter_Name;
    }

    public String getShelter_Name() {
        return Shelter_Name;
    }
//    @Column(name = "ADMIN_ID")
//    private  String admin_ID;

    public String getUser_ID() {
        return user_ID;
    }

    public String getUser_name() {
        return user_name;
    }

//    public String getUser_password() {
//        return user_password;
//    }

    public String getEmail() {
        return email;
    }

    public String getCellphone_number() {
        return cellphone_number;
    }

    public String getShelter_ID() {
        return Shelter_ID;
    }


//    public String getAdmin_ID() {
//        return admin_ID;
//    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCellphone_number(String cellphone_number) {
        this.cellphone_number = cellphone_number;
    }

    public void setShelter_ID(String shelter_ID) {
        Shelter_ID = shelter_ID;
    }
//
//    public void setAdmin_ID(String admin_ID) {
//        this.admin_ID = admin_ID;
//    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

//    public void setUser_password(String user_password) {
//        this.user_password = user_password;
//    }
}
