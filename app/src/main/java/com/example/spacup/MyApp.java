package com.example.spacup;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.item.MemberInfoItem;
import com.example.spacup.util.PrefUtil;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import timber.log.Timber;

import static com.example.spacup.Constants.PREF_KEY_IS_LOCKED;

// MemberInfoItem을 저장하고 반환하는 역활을 하는 객체
// CertificateInfoItem을 저장하고 반환하는 역활을 하는 객체
public class MyApp extends Application implements Application.ActivityLifecycleCallbacks {

    private MemberInfoItem memberInfoItem;
    private CertificateInfoItem certificateInfoItem;
    private static volatile MyApp instance = null;

    // PassCodeLock --------------------------------------//
    private boolean isNeedPassCodeConfirmation = true;
    private static MyApp app;

    public static MyApp getInstance() {
        if (app == null) app = new MyApp();
        return app;
    }
    // --------------------------------------------------//

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        instance = this;
        KakaoSDK.init(new MyApp.KakaoSDKAdapter());

        // PassCodeLock -------------------------------------------------------------------------//
        Timber.plant(new Timber.DebugTree());
        PrefUtil.setSharedPreferences(getApplicationContext());
        registerActivityLifecycleCallbacks(this);

    }

    @Override public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            isNeedPassCodeConfirmation = true;
        }
    }

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override public void onActivityStarted(Activity activity) {
        if (isNeedPassCodeConfirmation && PrefUtil.getBoolean(PREF_KEY_IS_LOCKED)) {
            activity.startActivity(PassCodeConfirmationActivity.createIntent(getApplicationContext()));
        }
        isNeedPassCodeConfirmation = false;
    }

    @Override public void onActivityResumed(Activity activity) {
    }

    @Override public void onActivityPaused(Activity activity) {

    }

    @Override public void onActivityStopped(Activity activity) {
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override public void onActivityDestroyed(Activity activity) {
    }

    // ------------------------------------------------------------------------------------------//


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

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        // 카카오 로그인 세션을 불러올 때의 설정값을 설정하는 부분.
        public ISessionConfig getSessionConfig() {

            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                    /*로그인을 하는 방식을 지정하는 부분. AuthType로는 다음 다섯 가지 방식이 있다.
                    KAKAO_TALK: 카카오톡으로 로그인(카카오톡 없으면 웹뷰 실행),
                    KAKAO_TALK_ONLY: 카카오톡으로 로그인(카카오톡이 없으면 로그인 자체가 불가능),
                    KAKAO_STORY: 카카오스토리로 로그인(카카오스토리 없으면 웹뷰 실행),
                    KAKAO_ACCOUNT: 웹뷰를 통한 로그인,
                    KAKAO_LOGIN_ALL: 모든 로그인방식 사용 가능. 정확히는, 카카오톡이나 카카오스토리가 있으면 그 쪽으로 로그인 기능을 제공하고, 둘 다 없으면 웹뷰를 통한 로그인을 제공한다.
                     */
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                    // SDK 로그인시 사용되는 WebView에서 pause와 resume시에 Timer를 설정하여 CPU소모를 절약한다.
                    // true 를 리턴할경우 webview로그인을 사용하는 화면서 모든 webview에 onPause와 onResume 시에 Timer를 설정해 주어야 한다.
                    // 지정하지 않을 시 false로 설정된다.
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                    // 로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                    // 일반 사용자가 아닌 Kakao와 제휴된 앱에서만 사용되는 값으로, 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
                    // 따로 제휴한 게 없으면 INDIVIDUAL 값으로 사용하면 된다.
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                    // Kakao SDK 에서 사용되는 WebView에서 email 입력폼의 데이터를 저장할지 여부를 결정한다.
                    // true일 경우, 다음번에 다시 로그인 시 email 폼을 누르면 이전에 입력했던 이메일이 나타난다.
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return MyApp.getGlobalApplicationContext();
                }
            };
        }
    }

    public static MyApp getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

   /* @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        KakaoSDK.init(new App.KakaoSDKAdapter());
    } */

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

}
