package com.example.spacup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.spacup.lib.MyLog;
import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificateListFragment extends Fragment implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    Context context;

    int memberSeq;

    RecyclerView certificateList;

    TextView noDataText;

    TextView orderClass;
    TextView orderJob;
    TextView orderPopularity;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        memberSeq = ((MyApp)this.getActivity().getApplication()).getMemberSeq();

        View layout = inflater.inflate(R.layout.fragment_certificate_list, container, false);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        MyApp myApp = ((MyApp) getActivity().getApplication());
        CertificateInfoItem currentInfoItem = myApp.getCertificateInfoItem();

        if (infoListAdapter != null && currentInfoItem != null) {
            infoListAdapter.setItem(currentInfoItem);
            myApp.setCertificateInfoItem(null);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_certificate);

        orderType = Constant.ORDER_TYPE_METER;

        certificateList = (RecyclerView) view.findViewById(R.id.list);
        noDataText = (TextView) view.findViewById(R.id.no_data);
        listType = (ImageView) view.findViewById(R.id.list_type);

        orderClass = (TextView) view.findViewById(R.id.order_type);
        orderJob = (TextView) view.findViewById(R.id.order_job);
        orderPopularity = (TextView) view.findViewById(R.id.order_popularity);

        orderClass.setOnClickListener(this);
        orderJob.setOnClickListener(this);
        orderPopularity.setOnClickListener(this);
        listType.setOnClickListener(this);

        setRecyclerView();

        listInfo(memberSeq, orderType, 0);
    }

    private void setLayoutManager(int row) {
        layoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        certificateList.setLayoutManager(layoutManager);
    }

    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        infoListAdapter = new InfoListAdapter(context,
                R.layout.row_certificate_list, new ArrayList<CertificateInfoItem>());
        certificateList.setAdapter(infoListAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {

            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                listInfo(memberSeq, orderType, page);
            }
        };
        certificateList.addOnScrollListener(scrollListener);
    }

    private void listInfo(int memberSeq, String orderType, final int currentPage) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<CertificateInfoItem>> call = remoteService.listCertificateInfo(memberSeq, orderType, currentPage);
        call.enqueue(new Callback<ArrayList<CertificateInfoItem>>() {

            public void onResponse(Call<ArrayList<CertificateInfoItem>> call,
                                   Response<ArrayList<CertificateInfoItem>> response) {
                ArrayList<CertificateInfoItem> list = response.body();

                if (response.isSuccessful() && list != null) {
                    infoListAdapter.addItemList(list);

                    if (infoListAdapter.getItemCount() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        noDataText.setVisibility(View.GONE);
                    }
                }
            }


            public void onFailure(Call<ArrayList<CertificateInfoItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.list_type) {
            changeListType();

        } else {
            if (v.getId() == R.id.order_type) {
                orderType = Constant.ORDER_TYPE_METER;

                setOrderTextColor(R.color.text_color_green,
                        R.color.text_color_black, R.color.text_color_black);

            } else if (v.getId() == R.id.order_job) {
                orderType = Constant.ORDER_TYPE_FAVORITE;

                setOrderTextColor(R.color.text_color_black,
                        R.color.text_color_green, R.color.text_color_black);

            } else if (v.getId() == R.id.order_popularity) {
                orderType = Constant.ORDER_TYPE_RECENT;

                setOrderTextColor(R.color.text_color_black,
                        R.color.text_color_black, R.color.text_color_green);
            }

            setRecyclerView();
            listInfo(memberSeq, orderType, 0);
        }
    }

    private void setOrderTextColor(int color1, int color2, int color3) {
        orderClass.setTextColor(ContextCompat.getColor(context, color1));
        orderJob.setTextColor(ContextCompat.getColor(context, color2));
        orderPopularity.setTextColor(ContextCompat.getColor(context, color3));
    }

    private void changeListType() {
        if (listTypeValue == 1) {
            listTypeValue = 2;
            listType.setImageResource(R.drawable.ic_list2);
        } else {
            listTypeValue = 1;
            listType.setImageResource(R.drawable.ic_list);

        }
        setLayoutManager(listTypeValue);
    }
}
