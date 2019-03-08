package com.example.spacup.item;

import com.google.gson.annotations.SerializedName;

// 즐겨찾기 정보를 저장하는 객체
public class FavoriteItem extends CertificateInfoItem {

    @SerializedName("favorite_seq") public String keepSeq;
    @SerializedName("favorite_member_seq") public String keepMemberSeq;
    @SerializedName("favorite_date") public String keepDate;

    @Override
    public String toString() {
        return "FavoriteItem{" +
                "FavoriteSeq='" + keepSeq + '\'' +
                ", FavoriteMemberSeq='" + keepMemberSeq + '\'' +
                ", FavoriteDate='" + keepDate + '\'' +
                '}';
    }

}
