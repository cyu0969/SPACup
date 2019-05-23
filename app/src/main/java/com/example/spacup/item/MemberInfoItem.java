package com.example.spacup.item;

import com.google.gson.annotations.SerializedName;

// 사용자 정보를 저장하는 객체
public class MemberInfoItem {

    public int seq;
    public  String id;
    public  String password;
    public  String name;
    public  String email;
    public  String phone;
    public String education;
    public String birthday;
    @SerializedName("member_icon_filename") public String memberIconFilename;

    @Override
    public String toString() {
        return "MemberInfoItem{" +
                "seq=" + seq +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", memberIconFilename='" + memberIconFilename + '\'' +
                ", education='" + education + '\'' +
                ", birthday='" + birthday + '\'' +
                '}'
                ;
    }
}
