package com.example.spacup.custom;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

// 리사이클뷰 사용하면서 데이터가 추가적으로 필요할 때 사용되는 객체
public class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 4;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    RecyclerView.LayoutManager layoutManager;

    // LinearLayoutManager를 위한 생성자, 오픈소스에서 보고 배운거라 정확히는....
    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    // GridLayoutManager를 위한 생성자, 오픈소스에서 보고 배운거라 정확히는....
    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    // StaggeredGridLayoutManager를 위한 생성자
    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    // 현재 화면의 여러 스팬 중에서 가장 마지막에 보이는 아이템의 위치를 반환
    public int getLastVisibleItemPosition(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // 스크롤된 후에 호출되는 컬백 메소드이며 아이템을 추가로 로딩해야 하는지를 체크
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions =
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItemPosition(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition =
                    ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition =
                    ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        // 새로 로딩한 전체 아이템 개수가 이전에 설정된 전체 아이템 개수보다 작을 경우
        // 상태를 초기화한다. 이런 경우는 리사이클러뷰의 데이터가 초기화된 경우에 발생한다.
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // 현재 로딩중인 상태이고 새로 로딩한 전체 아이템 개수가 이전에 저장한 전체 아이템 개수보다
        // 크다면 로딩이 완료한 것으로 간주한다.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // 로딩중이 아니고 화면에 보이는 마지막 아이템 위치에 visibleThreshold을 더한 값이
        // totalItemCount 보다 큰 경우 새로 로딩할 아이템이 있는 것으로 간주한다.
        // 이때 onLoadMore() 메소드가 호출된다.
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    private void onLoadMore(int currentPage, int totalItemCount, RecyclerView view) {
    }
}
