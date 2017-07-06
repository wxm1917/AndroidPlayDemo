package com.rename.qiyuan.main.presenter;

/**
 * Created by zhouxiaming on 2017/5/5.
 */

public interface IBaseContractView {
    void showLoadingView();
    void dismissLoadingView();
    void showEmptyView();
    void showNetWorkErrorView();
}
