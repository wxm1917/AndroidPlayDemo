package com.qiyi.openapi.demo.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qiyi.apilib.ApiLib;
import com.qiyi.apilib.model.BaseEntity;
import com.qiyi.apilib.model.ChannelEntity;
import com.qiyi.apilib.model.RecommendEntity;
import com.qiyi.apilib.model.VideoInfo;
import com.qiyi.apilib.utils.ImageUtils;
import com.qiyi.apilib.utils.StringUtils;
import com.qiyi.apilib.utils.UiUtils;
import com.qiyi.openapi.demo.QYPlayerUtils;
import com.qiyi.openapi.demo.R;
import com.qiyi.openapi.demo.view.CycleBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouxiaming on 15/5/8.
 */
public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int ROW_NUM = 3;
    private int TYPE_FOCUS = 0x001;
    private int TYPE_CHANNEL_NAME = 0x002;
    private int TYPE_VIDEO = 0x003;
    private int TYPE_NEWS = 0x004;

    private int[] screen;
    Activity context;

    private List<BaseEntity> entityList = new ArrayList<BaseEntity>();
    public RecommendAdapter(Activity context){
        this.context = context;
        screen = UiUtils.getScreenWidthAndHeight(context);
    }

    public void setData(RecommendEntity entity) {
        reAssembleData(entity);
        this.notifyDataSetChanged();
    }

    /**
     * 重新组装数据，以适配RecyclerView
     */
    private void reAssembleData(RecommendEntity entity) {
        if (entity == null) {
            return;
        }

        List<RecommendEntity.RecommendItem> recommendItems = entity.recommendItemList;
        if (recommendItems == null || recommendItems.size() < 1) {
            return;
        }

        entityList.clear();
        for (RecommendEntity.RecommendItem recommendItem : recommendItems) {
            if (recommendItem.videoInfoList == null || recommendItem.videoInfoList.size() < 1) {
                continue;
            }

            if (StringUtils.isEmpty(recommendItem.channelId) || StringUtils.isEmpty(recommendItem.channelName)) {
                //轮播图
                entityList.add(recommendItem);
                continue;
            }

            //频道信息
            ChannelEntity.Channel channel = new ChannelEntity.Channel();
            channel.id = recommendItem.channelId;
            channel.name = recommendItem.channelName;
            entityList.add(channel);
            //如果是资讯信息，需要重新组装数据
            if ("25".equals(channel.id)) {
                reAssembleNewsItem(channel, entityList, recommendItem.videoInfoList);
            } else {
                int needDeleteCount = recommendItem.videoInfoList.size() % ROW_NUM; //根据每一行的item数取余，不能一行显示的就删掉
                for (int i = 0; i < recommendItem.videoInfoList.size() - needDeleteCount; i++) {
                    //当前频道下面的视频信息
                    entityList.add(recommendItem.videoInfoList.get(i));
                }
            }
        }
    }

    private void reAssembleNewsItem(ChannelEntity.Channel channel, List<BaseEntity> entities, List<VideoInfo> videoInfoList) {
        //只显示4条数据
        for (int i = 0; i < 4; i+=2) {
            NewsItem item = new NewsItem();
            for (int j = 0; j < NewsItem.ROW_NUM; j++) {
                item.addVideo(videoInfoList.get(i + j));
            }
            entities.add(item);
        }
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOCUS) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_recommend_focus, parent, false);
            return new FocusViewHolder(view);
        } else if (viewType == TYPE_CHANNEL_NAME) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_channel_name_item, parent, false);
            return new ChannelInfoViewHolder(view);
        } else if (viewType == TYPE_VIDEO) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_video_info_item, parent, false);
            return new VideoInfoViewHolder(view);
        } else if (viewType == TYPE_NEWS) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_news_info_item, parent, false);
            return new NewsViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (entityList.get(position) instanceof RecommendEntity.RecommendItem) {
            return TYPE_FOCUS;
        } else if (entityList.get(position) instanceof ChannelEntity.Channel) {
            return TYPE_CHANNEL_NAME;
        } else if (entityList.get(position) instanceof NewsItem) {
            return TYPE_NEWS;
        } else {
            return TYPE_VIDEO;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).setData(position);
    }


    @Override
    public int getItemCount() {
        return entityList.size();
    }

    /**
     * 判断是否需要占用整行
     * @param position
     */
    private boolean needTakeOneRow(int position) {
        int type = getItemViewType(position);
        if (type == TYPE_FOCUS || type == TYPE_CHANNEL_NAME || type == TYPE_NEWS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {   // 布局是GridLayoutManager所管理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果是焦点图或者频道名称的对象则占据spanCount的位置，否则就只占用1个位置
                    return (needTakeOneRow(position)) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }


    abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View view) {
            super(view);
        }

       abstract void setData(int position);

    }

    /**
     * 焦点图的ViewHolder
     */
    class FocusViewHolder extends BaseViewHolder {
        CycleBanner mCycleBanner;
        public FocusViewHolder(View itemView) {
            super(itemView);
            mCycleBanner = new CycleBanner(context, itemView);
        }

        @Override
        void setData(int position) {
            RecommendEntity.RecommendItem recommendItem = (RecommendEntity.RecommendItem) entityList.get(position);
            mCycleBanner.setData(recommendItem.videoInfoList);
        }
    }

    /**
     * 标题的ViewHolder
     */
    class ChannelInfoViewHolder extends BaseViewHolder {
        TextView channelNameView;
        public ChannelInfoViewHolder(View itemView) {
            super(itemView);
            channelNameView = (TextView) itemView.findViewById(R.id.channel_name);
        }

        @Override
        void setData(int position) {
            ChannelEntity.Channel channel = (ChannelEntity.Channel)entityList.get(position);
            channelNameView.setText(channel.name);
        }
    }

    class VideoInfoViewHolder extends BaseViewHolder implements View.OnClickListener{
        ImageView cover;
        TextView name;
        TextView playCount;
        TextView snsScore;

        public VideoInfoViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_image_name);
            playCount = (TextView) itemView.findViewById(R.id.item_image_description);
            snsScore = (TextView) itemView.findViewById(R.id.item_image_description2);
            cover = (ImageView) itemView.findViewById(R.id.item_image_img);
            itemView.setOnClickListener(this);
            resizeImageView(cover);
        }

        /**
         * 视频图片为竖图展示取分辨率为：_120_160
         * @param cover
         */
        private void resizeImageView(ImageView cover) {
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int coverWidth = (screenWidth - context.getResources().getDimensionPixelSize(R.dimen.video_card_margin_margin_horizontal)*2*4) / 3;
            int coverHeight = (int)(160.0f / 120.0f * coverWidth);
            cover.setMinimumHeight(coverHeight);
            cover.setMaxHeight(coverHeight);
            cover.setMaxHeight(coverWidth);
            cover.setMinimumWidth(coverWidth);
            cover.setAdjustViewBounds(false);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cover.getLayoutParams();
            lp.height = coverHeight;
            lp.width = coverWidth;
            cover.setLayoutParams(lp);
        }

        public void setData(int position){
            VideoInfo video = (VideoInfo)entityList.get(position);
            if (!StringUtils.isEmpty(video.shortTitle)) {
                name.setText(video.shortTitle);
            } else {
                name.setText("");
            }

            if (!StringUtils.isEmpty(video.playCountText)) {
                playCount.setText(context.getString(R.string.play_count, video.playCountText));
            } else {
                playCount.setText("");
            }

            if (!StringUtils.isEmpty(video.snsScore)) {
                snsScore.setText(context.getString(R.string.sns_score, video.snsScore));
            } else {
                snsScore.setText("");
            }

            Glide.clear(cover); //清除缓存
            Glide.with(ApiLib.CONTEXT).load(ImageUtils.getRegImage(video.img, ImageUtils.IMG_260_360)).animate(R.anim.alpha_on).into(cover);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            BaseEntity dataObj = entityList.get(position);
            if (dataObj instanceof VideoInfo) {
                VideoInfo videoInfo = (VideoInfo)dataObj;
                QYPlayerUtils.jumpToPlayerActivity(context, videoInfo.aId, videoInfo.tId);
            }
        }
    }

    class NewsViewHolder extends BaseViewHolder implements View.OnClickListener{
        ImageView cover1, cover2;
        TextView name1, name2;
        TextView playCount1, playCount2;
        TextView snsScore1, snsScore2;
        View item1View, item2View;
        public NewsViewHolder(View itemView) {
            super(itemView);
            item1View = itemView.findViewById(R.id.item1);
            name1 = (TextView) item1View.findViewById(R.id.item_image_name);
            playCount1 = (TextView) item1View.findViewById(R.id.item_image_description);
            snsScore1 = (TextView) item1View.findViewById(R.id.item_image_description2);
            cover1 = (ImageView) item1View.findViewById(R.id.item_image_img);
            item1View.setOnClickListener(this);
            resizeImageView(cover1);
            item2View = itemView.findViewById(R.id.item2);
            name2 = (TextView) item2View.findViewById(R.id.item_image_name);
            playCount2 = (TextView) item2View.findViewById(R.id.item_image_description);
            snsScore2 = (TextView) item2View.findViewById(R.id.item_image_description2);
            cover2 = (ImageView) item2View.findViewById(R.id.item_image_img);
            item2View.setOnClickListener(this);
            resizeImageView(cover2);
        }

        /**
         * 资讯图片为横图展示取分辨率为：_480_270
         * @param cover
         */
        private void resizeImageView(ImageView cover) {
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int coverWidth = (screenWidth - context.getResources().getDimensionPixelSize(R.dimen.video_card_margin_margin_horizontal)*2*3) / 2;
            int coverHeight = (int)(270.0f / 480.0f * coverWidth);
            cover.setMinimumHeight(coverHeight);
            cover.setMaxHeight(coverHeight);
            cover.setMaxHeight(coverWidth);
            cover.setMinimumWidth(coverWidth);
            cover.setAdjustViewBounds(false);
            //set height as layoutParameter too
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cover.getLayoutParams();
            lp.height = coverHeight;
            lp.width = coverWidth;
            cover.setLayoutParams(lp);
        }

        private void setItemData(View itemVew, ImageView cover, TextView name, TextView snsScore, TextView playCount, VideoInfo video) {
            if (!StringUtils.isEmpty(video.shortTitle)) {
                name.setText(video.shortTitle);
            } else {
                name.setText("");
            }

            if (!StringUtils.isEmpty(video.playCountText)) {
                playCount.setText(context.getString(R.string.play_count, video.playCountText));
            } else {
                playCount.setText("");
            }

            if (!StringUtils.isEmpty(video.snsScore)) {
                snsScore.setText(context.getString(R.string.sns_score, video.snsScore));
            } else {
                snsScore.setText("");
            }
            itemVew.setTag(R.id.tag_key, video);
            Glide.clear(cover); //清除缓存
            Glide.with(ApiLib.CONTEXT).load(ImageUtils.getRegImage(video.img, ImageUtils.IMG_480_270)).animate(R.anim.alpha_on).into(cover);
        }

        @Override
        void setData(int position) {
            NewsItem item = (NewsItem)entityList.get(position);
            setItemData(item1View, cover1, name1, snsScore1, playCount1, item.videoInfos.get(0));
            setItemData(item2View, cover2, name2, snsScore2, playCount2, item.videoInfos.get(1));
        }

        @Override
        public void onClick(View v) {
            VideoInfo videoInfo = (VideoInfo)v.getTag(R.id.tag_key);
            QYPlayerUtils.jumpToPlayerActivity(context, videoInfo.aId, videoInfo.tId);
        }
    }

    /**
     * 为资讯信息封装Item
     */
    static class NewsItem extends BaseEntity {
        public static int ROW_NUM = 2;
        public ChannelEntity.Channel channel;
        public List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
        public void addVideo(VideoInfo videoInfo) {
            videoInfos.add(videoInfo);
        }
    }

}
