package com.example.spacup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.lib.GoLib;
import com.example.spacup.lib.MyLog;

// 정보 공유 액티비티
public class SpecupRegisterActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    public static CertificateInfoItem certificateInfoItem = null;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specup_register);

        context = this;

        int memberSeq = ((MyApp)getApplication()).getMemberSeq();

        CertificateInfoItem infoItem = new CertificateInfoItem();
        infoItem.memberSeq = memberSeq;

        MyLog.d(TAG, "infoItem " + infoItem.toString());

        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.specup_register);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_close:
                finish();
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
