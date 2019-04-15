package com.example.spacup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.lib.MyLog;

import org.parceler.Parcels;

import okhttp3.Address;

public class SpecupRegisterInputFragment extends Fragment implements View.OnClickListener {

    public static final String INFO_ITEM = "INFO_ITEM";
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    CertificateInfoItem infoItem;
    Address address;

    EditText nameEdit;
    EditText telEdit;
    EditText descriptionEdit;
    TextView currentLength;

    public static SpecupRegisterInputFragment newInstance(CertificateInfoItem infoItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_ITEM, Parcels.wrap(infoItem));

        SpecupRegisterInputFragment fragment = new SpecupRegisterInputFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            infoItem = Parcels.unwrap(getArguments().getParcelable(INFO_ITEM));
            if (infoItem.seq != 0) {
                SpecupRegisterActivity.certificateInfoItem = infoItem;
            }
            MyLog.d(TAG, "infoItem " + infoItem);
        }
    }



    @Override
    public void onClick(View v) {

    }
}
