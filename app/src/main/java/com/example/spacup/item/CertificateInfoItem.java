package com.example.spacup.item;

import com.google.gson.annotations.SerializedName;

// 자격증에 대한 정보를 저장하는 객체
@org.parceler.Parcel
public class CertificateInfoItem {

    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    public String name;
    public String type;
    public String date;
    public String homepage;
    public String description;
    @SerializedName("is_favorite") public boolean isFavorite;
    @SerializedName("image_filename") public String imageFilename;

    @Override
    public String toString() {

        return "CertificateInfoItem{" +
                "seq=" + seq +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", homepage='" + homepage + '\'' +
                ", description='" + description + '\'' +
                ", isKeep=" + isFavorite +
                ", imageFilename='" + imageFilename + '\'' +
                '}';
    }
}
