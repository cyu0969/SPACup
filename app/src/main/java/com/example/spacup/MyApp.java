package com.example.spacup;

import android.app.Application;
import android.os.StrictMode;

import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.item.MemberInfoItem;

// MemberInfoItem을 저장하고 반환하는 역활을 하는 객체
// CertificateInfoItem을 저장하고 반환하는 역활을 하는 객체
public class MyApp extends Application {

    private MemberInfoItem memberInfoItem;
    private CertificateInfoItem certificateInfoItem;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public MemberInfoItem getMemberInfoItem() {
        if (memberInfoItem == null) memberInfoItem = new MemberInfoItem();

        return memberInfoItem;
    }

    public void setMemberInfoItem(MemberInfoItem item) {
        this.memberInfoItem = item;
    }

    public int getMemberSeq() {
        return memberInfoItem.seq;
    }

    public void setCertificateInfoItem(CertificateInfoItem certificateInfoItem) {
        this.certificateInfoItem = certificateInfoItem;
    }

    public CertificateInfoItem getCertificateInfoItem() {
        return certificateInfoItem;
    }

}
