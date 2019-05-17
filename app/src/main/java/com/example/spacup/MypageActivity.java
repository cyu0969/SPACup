package com.example.spacup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spacup.item.MemberInfoItem;
import com.example.spacup.lib.EtcLib;
import com.example.spacup.lib.MyLog;
import com.example.spacup.lib.MyToast;
import com.example.spacup.lib.StringLib;
import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                setEducationTypeDialog();
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

    // 학력을 선택할 수 있는 다이얼로그 출력
    private void setEducationTypeDialog() {
        final String[] educationTypes = new String[3];
        educationTypes[0] = getResources().getString(R.string.education_high);
        educationTypes[1] = getResources().getString(R.string.education_univ);
        educationTypes[2] = getResources().getString(R.string.education_grad);

        new AlertDialog.Builder(this)
                .setItems(educationTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= 0) {
                            sextypeEdit.setText(educationTypes[which]);
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    // 생일을 선택할 수 있는 다이얼로그 출력
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

    // 오른쪽 상단 메뉴 구성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    // 왼쪽 상단의 아이콘으로 뒤로 가기 및 저장 기능을 이용하기 위한 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                break;

            case R.id.action_submit:
                save();
                break;
        }

        return true;
    }

    // 사용자가 수정하면 내용을 사용자 정보에 저장하는 메소드
    private MemberInfoItem getMemberInfoItem() {
        MemberInfoItem item = new MemberInfoItem();
        item.phone = EtcLib.getInstance().getPhoneNumber(context);
        item.name = nameEdit.getText().toString();
        item.sextype = sextypeEdit.getText().toString();
        item.birthday = birthEdit.getText().toString().replace(" ", "");

        return item;
    }

    // 기존 사용자 정보랑 이전 사용자 정보랑 비교하는 메소드
    // 변경되었으면 참, 아니면 거짓.
    private boolean isChanged(MemberInfoItem newItem) {
        if (newItem.name.trim().equals(currentItem.name)
                && newItem.sextype.trim().equals(currentItem.sextype)
                && newItem.birthday.trim().equals(currentItem.birthday)) {
            Log.d(TAG, "return " + false);
            return false;
        } else {
            return true;
        }
    }

    // 사용자가 이름을 변경했는지 확인
    // 변경되었으면 참, 아니면 거짓.
    private boolean isNoName(MemberInfoItem newItem) {
        if (StringLib.getInstance().isBlank(newItem.name)) {
            return true;
        } else {
            return false;
        }
    }

    // 사용자가 변경한 내용을 저장하는 메소드
    private void save() {
        final MemberInfoItem newItem = getMemberInfoItem();

        if (!isChanged(newItem)) {
            MyToast.s(this, R.string.no_change);
            finish();
            return;
        }

        MyLog.d(TAG, "insertItem " + newItem.toString());

        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertMemberInfo(newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String seq = response.body();
                    try {
                        currentItem.seq = Integer.parseInt(seq);
                        if (currentItem.seq == 0) {
                            MyToast.s(context, R.string.member_insert_fail_message);
                            return;
                        }
                    } catch (Exception e) {
                        MyToast.s(context, R.string.member_insert_fail_message);
                        return;
                    }
                    currentItem.name = newItem.name;
                    currentItem.sextype = newItem.sextype;
                    currentItem.birthday = newItem.birthday;
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    // 변경사항 없으면 뒤로가기 있으면 변경사항 자동 저장
    private void close() {
        MemberInfoItem newItem = getMemberInfoItem();

        if (!isChanged(newItem) && !isNoName(newItem)) {
            finish();
        } else if (isNoName(newItem)) {
            MyToast.s(context, R.string.name_need);
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.change_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    // 뒤로가기 버튼을 클릭할 경우 close메소드 불러옴
    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_icon || v.getId() == R.id.profile_icon_change) {
            startProfileIconChange();
        }
    }

    private void startProfileIconChange() {
        Intent intent = new Intent(this, MypageIconActivity.class);
        startActivity(intent);
    }
}
