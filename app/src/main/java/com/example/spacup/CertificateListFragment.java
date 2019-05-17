package com.example.spacup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spacup.adapter.InfoListAdapter;
import com.example.spacup.custom.EndlessRecyclerViewScrollListener;

public class CertificateListFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    Context context;

    int memberSeq;

    RecyclerView certificateList;

    TextView noDataText;
    TextView orderMeter;
    TextView orderFavorite;
    TextView orderRecent;

    ImageView listType;

    InfoListAdapter infoListAdapter;
    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    int listTypeValue = 2;
    String orderType;

    public static CertificateListFragment newInstance() {
        CertificateListFragment f = new CertificateListFragment();
        return f;
    }

    @Override
    public void onClick(View v) {

    }
}
