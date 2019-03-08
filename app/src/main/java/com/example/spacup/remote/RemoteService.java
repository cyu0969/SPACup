package com.example.spacup.remote;

import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.item.FavoriteItem;
import com.example.spacup.item.MemberInfoItem;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteService {

    String BASE_URL = "http://192.168.1.188:3000";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    // 사용자 정보

    @GET("/member/{phone}")
    Call<MemberInfoItem> selectMemberInfo(@Path("phone") String phone);

    @POST("/member/info")
    Call<String> insertMemberInfo(@Body MemberInfoItem memberInfoItem);

    @FormUrlEncoded
    @POST("/member/phone")
    Call<String> insertMemberPhone(@Field("phone") String phone);

    @Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);

    // 자격증 정보

    @GET("/certificate/info/{info_seq")
    Call<CertificateInfoItem> selectCertificateInfo(@Path("info_seq") int certificateInfoSeq,
                                             @Query("member_seq") int memberSeq);
    @POST("/certificate/info")
    Call<String> insertCertificateInfo(@Body CertificateInfoItem infoItem);

    @Multipart
    @POST("/certificate/info/image")
    Call<ResponseBody> uploadCertificateImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);

    @GET("/food/list")
    Call<ArrayList<CertificateInfoItem>> listCertificateInfo(@Query("member_seq") int memberSeq,
                                                      @Query("user_latitude") double userLatitude,
                                                      @Query("user_longitude") double userLongitude,
                                                      @Query("order_type") String orderType,
                                                      @Query("current_page") int currentPage);

    //즐겨찾기
    @POST("/favorite/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/favorite/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @GET("/favorite/list")
    Call<ArrayList<FavoriteItem>> listFavorite(@Query("member_seq") int memberSeq,
                                           @Query("user_latitude") double userLatitude,
                                           @Query("user_longitude") double userLongitude);
}
