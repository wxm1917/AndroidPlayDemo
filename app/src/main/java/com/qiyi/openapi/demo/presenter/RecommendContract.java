package com.qiyi.openapi.demo.presenter;

import com.qiyi.apilib.model.RecommendEntity;

import java.util.List;

/**
 * Created by zhouxiaming on 2017/5/8.
 */

public interface RecommendContract {
    interface IView extends IBaseContractView {
        void renderRecommendDetail(RecommendEntity recommendEntitiy);
    }

    interface IPresenter extends IBaseContrackPresenter {
        void resetPageIndex();
        void loadRecommendDetailFromServer(boolean showLoadingView);
        boolean hasMore();
    }
}
