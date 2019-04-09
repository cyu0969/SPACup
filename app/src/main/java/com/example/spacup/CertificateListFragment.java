package com.example.spacup;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spacup.custom.EndlessRecyclerViewScrollListener;

public class CertificateListFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    Context context;

    int memberSeq;

    RecyclerView CertificateList;
    TextView noDataText;

    TextView orderType;
    TextView orderJob;
    TextView orderFavorite;

    ImageView listType;

    StaggeredGridLayoutManager layoutManager;
    EndlessRecyclerViewScrollListener scrollListener;


    @Override
    public void onClick(View v) {

    }
}
