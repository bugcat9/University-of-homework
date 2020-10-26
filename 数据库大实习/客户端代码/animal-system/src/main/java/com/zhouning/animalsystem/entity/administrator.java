package com.zhouning.animalsystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADMINISTRATOR")
public class administrator {

    @Id
    @Column(name = "ADMIN_ID")
    private String admin_ID;

    @Column(name = "ADMINI_NAME")
    private String admini_name;

    @Column(name = "ADMINI_PASSWORD")
    private String admini_password;

    public String getAdmin_ID() {
        return admin_ID;
    }

    public String getAdmini_name() {
        return admini_name;
    }

    public String getAdmini_password() {
        return admini_password;
    }

    public void setAdmin_ID(String admin_ID) {
        this.admin_ID = admin_ID;
    }

    public void setAdmini_name(String admini_name) {
        this.admini_name = admini_name;
    }

    public void setAdmini_password(String admini_password) {
        this.admini_password = admini_password;
    }
}
