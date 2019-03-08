package com.example.spacup.item;

import com.google.gson.annotations.SerializedName;

// 자격증에 대한 정보를 저장하는 객체
@org.parceler.Parcel
public class CertificateInfoItem {

    public int seq;
    public String name;
    public String tel;
    public String price;
    public String type;
    public String date;
    public String homepage;
    public String description;

    @Override
    public String toString() {

        return "CertificateInfoItem{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", price='" + price + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", homepage='" + homepage + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
