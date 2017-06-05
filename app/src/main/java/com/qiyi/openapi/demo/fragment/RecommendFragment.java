package com.qiyi.openapi.demo.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.qiyi.apilib.model.RecommendEntity;
import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.adapter.RecommendAdapter;
import com.qiyi.openapi.demo.presenter.RecommendContract;
import com.qiyi.openapi.demo.presenter.RecommendPresenter;


/**
 * Created by zhouxiaming on 2017/5/7.
 */

public class RecommendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecommendContract.IView{
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecommendPresenter mPresenter;
    private RecommendAdapter mAdapter;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        super.initView();
        mPresenter = new RecommendPresenter(this);
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.fragment_toolbar);
        toolbar.setTitle(R.string.recommend_title);
        setActionBar(toolbar);

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, RecommendAdapter.ROW_NUM);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecommendAdapter(mActivity);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected void loadData() {
        super.loadData();
        mPresenter.loadRecommendDetailFromServer(true);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadRecommendDetailFromServer(false);
    }

    @Override
    public void showLoadingView() {
        showLoadingBar();
    }

    @Override
    public void dismissLoadingView() {
        hideLoadingBar();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyView() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNetWorkErrorView() {
        mRefreshLayout.setRefreshing(false);
        Toast.makeText(mActivity, R.string.network_error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void renderRecommendDetail(RecommendEntity recommendEntity) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setData(recommendEntity);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mAdapter.getItemCount() == 0) {
                loadData();
            }
        }
    }
}
