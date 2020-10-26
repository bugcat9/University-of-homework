package com.zhouning.animalsystem.entity;

import javax.persistence.*;

@Entity
@Table(name = "SHELTER_VIEW")
//存储过程调用
@NamedStoredProcedureQueries({
        //进行插入的存储过程
        @NamedStoredProcedureQuery(name = "pr_insert_into_shelter",procedureName = "pr_insert_into_shelter",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_NAME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_SHELTER_ADDRESS",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_POSTCODE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SUM_ROOMS",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_REMAIN_ROOMS",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_COMMENT",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行修改的存储过程
        @NamedStoredProcedureQuery(name = "pr_update_shelter",procedureName = "pr_update_shelter",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_NAME",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "p_SHELTER_ADDRESS",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_POSTCODE",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SUM_ROOMS",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_REMAIN_ROOMS",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_COMMENT",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
        //进行删除的存储过程
        @NamedStoredProcedureQuery(name = "pr_delete_shelter",procedureName = "pr_delete_shelter",
                parameters={
                        @StoredProcedureParameter(mode = ParameterMode.IN ,name = "P_SHELTER_ID",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT ,name = "p_out_resulut",type = Integer.class)
                }
        ),
})
public class shelter {
    @Id
    @Column(name = "SHELTER_ID")
    private String shelter_id;
    @Column(name = "SHELTER_NAME")
    private String shelter_name;
    @Column(name = "SHELTER_ADDRESS")
    private String shelter_address;
    @Column(name = "POSTCODE")
    private String postcode;    //邮编
    @Column(name = "SUM_ROOMS")
    private int sum_rooms;     //房间总数
    @Column(name = "REMAIN_ROOMS")
    private int remain_rooms;   //剩余房间数量
    @Column(name = "SHELTER_COMMENT")
    private String shelter_comment; //备注

    public void setShelter_id(String shelter_id) {
        this.shelter_id = shelter_id;
    }

    public void setShelter_name(String shelter_name) {
        this.shelter_name = shelter_name;
    }

    public void setShelter_address(String shelter_address) {
        this.shelter_address = shelter_address;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setSum_rooms(int sum_rooms) {
        this.sum_rooms = sum_rooms;
    }

    public void setRemain_rooms(int remain_rooms) {
        this.remain_rooms = remain_rooms;
    }

    public void setShelter_comment(String shelter_comment) {
        this.shelter_comment = shelter_comment;
    }

    public String getShelter_id() {
        return shelter_id;
    }

    public String getShelter_name() {
        return shelter_name;
    }

    public String getShelter_address() {
        return shelter_address;
    }

    public String getPostcode() {
        return postcode;
    }

    public int getSum_rooms() {
        return sum_rooms;
    }

    public int getRemain_rooms() {
        return remain_rooms;
    }

    public String getShelter_comment() {
        return shelter_comment;
    }
}
