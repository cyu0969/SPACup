package com.example.spacup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spacup.adapter.FavoriteListAdapter;
import com.example.spacup.item.CertificateInfoItem;
import com.example.spacup.item.FavoriteItem;
import com.example.spacup.lib.MyLog;
import com.example.spacup.remote.RemoteService;
import com.example.spacup.remote.ServiceGenerator;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 즐겨찾기 리스트를 보여주는 프래그먼트
public class SpecupFavoriteFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    Context context;
    int memberSeq;

    RecyclerView keepRecyclerView;
    TextView noDataText;

    FavoriteListAdapter FavoriteListAdapter;

    ArrayList<FavoriteItem> FavoriteList = new ArrayList<>();

    public static SpecupFavoriteFragment newInstance() {
        SpecupFavoriteFragment f = new SpecupFavoriteFragment();
        return f;
    }

    // fragment_bestfood_keep.xml 기반으로 뷰를 생성
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();

        memberSeq = ((MyApp)getActivity().getApplication()).getMemberSeq();

        View layout = inflater.inflate(R.layout.fragment_certificate_favorite, container, false);

        return layout;
    }

    // 프래그먼트가 일시 중지 상태가 되었다가 다시 보여질 때 호출
    // 즐겨찾기 상태가 변경되었을 경우 이를 반영하는 용도로 사용
    @Override
    public void onResume() {
        super.onResume();

        MyApp myApp = ((MyApp) getActivity().getApplication());
        CertificateInfoItem currentInfoItem = myApp.getCertificateInfoItem();

        if (FavoriteListAdapter != null && currentInfoItem != null) {
            FavoriteListAdapter.setItem(currentInfoItem);
            myApp.setCertificateInfoItem(null);

            if (FavoriteListAdapter.getItemCount() == 0) {
                noDataText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.nav_favorite);

        keepRecyclerView = (RecyclerView) view.findViewById(R.id.keep_list);
        noDataText = (TextView) view.findViewById(R.id.no_keep);

        FavoriteListAdapter = new FavoriteListAdapter(context,
                R.layout.row_specup_favorite, FavoriteList, memberSeq);
        StaggeredGridLayoutManager layoutManager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        keepRecyclerView.setLayoutManager(layoutManager);
        keepRecyclerView.setAdapter(FavoriteListAdapter);

        listFavorite(memberSeq);
    }

    // 서버에서 즐겨찾기한 맛집 정보를 조회
    private void listFavorite(int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<FavoriteItem>> call
                = remoteService.listFavorite(memberSeq);
        call.enqueue(new Callback<ArrayList<FavoriteItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FavoriteItem>> call,
                                   Response<ArrayList<FavoriteItem>> response) {
                ArrayList<FavoriteItem> list = response.body();

                if (list == null) {
                    list = new ArrayList<>();
                }

                noDataText.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    MyLog.d(TAG, "list size " + list.size());
                    if (list.size() == 0) {
                        noDataText.setVisibility(View.VISIBLE);
                    } else {
                        FavoriteListAdapter.setItemList(list);
                    }
                } else {
                    MyLog.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FavoriteItem>> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }
}
