package com.rename.qiyuan.main.presenter;

import com.qiyi.apilib.model.RecommendEntity;

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
