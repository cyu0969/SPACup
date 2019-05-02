package com.example.spacup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spacup.adapter.InfoListAdapter;
import com.example.spacup.custom.EndlessRecyclerViewScrollListener;
import com.example.spacup.item.CertificateInfoItem;

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

    int listTypeValue = 2;

    public static CertificateListFragment newInstance() {
        CertificateListFragment f = new CertificateListFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        memberSeq = ((MyApp)this.getActivity().getApplication()).getMemberSeq();

        View layout = inflater.inflate(R.layout.fragment_certificate_list, container, false);

        return layout;
    }

    @Override
    public void onClick(View v) {

    }
}
