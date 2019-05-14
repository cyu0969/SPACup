package com.example.spacup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.spacup.item.MemberInfoItem;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.StringLib;
import com.example.spacup.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MypageActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    Context context;
    ImageView profileIconImage;
    ImageView profileIconChangeImage;
    EditText nameEdit;
    EditText sextypeEdit;
    EditText birthEdit;
    EditText phoneEdit;


    MemberInfoItem currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);


        context = this;

        currentItem = ((MyApp) getApplication()).getMemberInfoItem();

        setToolbar();
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyLog.d(TAG, RemoteService.MEMBER_ICON_URL + currentItem.memberIconFilename);

        if (StringLib.getInstance().isBlank(currentItem.memberIconFilename)) {
            Picasso.with(this).load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.with(this)
                    .load(RemoteService.MEMBER_ICON_URL + currentItem.memberIconFilename)
                    .into(profileIconImage);
        }
    }

    private void setToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.profile_setting);
        }
    }

    private void setView() {
        profileIconImage = (ImageView) findViewById(R.id.profile_icon);
        profileIconImage.setOnClickListener(this);

        profileIconChangeImage = (ImageView) findViewById(R.id.profile_icon_change);
        profileIconChangeImage.setOnClickListener(this);

        nameEdit = (EditText) findViewById(R.id.profile_name);
        nameEdit.setText(currentItem.name);

        sextypeEdit = (EditText) findViewById(R.id.profile_sextype);
        sextypeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSexTypeDialog();
            }
        });

        birthEdit = (EditText) findViewById(R.id.profile_birth);
        birthEdit.setText(currentItem.birthday);
        birthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBirthdayDialog();
            }
        });
    }

    private void setSexTypeDialog() {
        final String[] sexTypes = new String[2];
        sexTypes[0] = getResources().getString(R.string.sex_man);
        sexTypes[1] = getResources().getString(R.string.sex_woman);

        new AlertDialog.Builder(this)
                .setItems(sexTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= 0) {
                            sextypeEdit.setText(sexTypes[which]);
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    private void setBirthdayDialog() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String myMonth;
                if (monthOfYear + 1 < 10) {
                    myMonth = "0" + (monthOfYear + 1);
                } else {
                    myMonth = "" + (monthOfYear + 1);
                }

                String myDay;
                if (dayOfMonth < 10) {
                    myDay = "0" + dayOfMonth;
                } else {
                    myDay = "" + dayOfMonth;
                }

                String date = year + " " + myMonth + " " + myDay;
                birthEdit.setText(date);
            }
        }, year, month, day).show();
    }

    @Override
    public void onClick(View v) {

    }
}
