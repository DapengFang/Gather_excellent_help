package com.gather_excellent_help.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gather_excellent_help.R;
import com.gather_excellent_help.api.Url;
import com.gather_excellent_help.bean.NewsListBean;
import com.gather_excellent_help.presenter.NewsFirstPresenter;
import com.gather_excellent_help.utils.DensityUtil;
import com.gather_excellent_help.utils.imageutils.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dapeng Fang on 2017/8/10.
 */

public class NewsFirstAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<NewsListBean.DataBean> newsData;
    private int status = 1;
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;
    private static final int TYPE_FOOTER = -2;
    private int extra = 1;
    //private ImageLoader mIageLoader;

    public NewsFirstAdapter(Context context,List<NewsListBean.DataBean> newsData) {
        this.context = context;
        this.newsData = newsData;
        //mIageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return position;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = View.inflate(parent.getContext(), R.layout.activity_view_footer, null);
            return new FooterViewHolder(view);
        } else {
            View rootView = View.inflate(parent.getContext(), R.layout.item_news_first_content, null);
            return new NewsFirstViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
        } else if (holder instanceof NewsFirstViewHolder) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long curr_time = System.currentTimeMillis();
            NewsFirstViewHolder newsHolder = (NewsFirstViewHolder) holder;
            if(position!=getItemCount()-1) {
                NewsListBean.DataBean dataBean = newsData.get(position);
                int id = dataBean.getId();
                String title = dataBean.getTitle();
                String category_name = dataBean.getCategory_name();
                String img_url = dataBean.getImg_url();
                String add_time = dataBean.getAdd_time();
                int click = dataBean.getClick();
                if(newsHolder.tvNewsFirstCount!=null) {
                    newsHolder.tvNewsFirstCount.setText(click+"阅读");
                }
                if(category_name!=null) {
                    if(newsHolder.tvNewsFirstWhich!=null) {
                        newsHolder.tvNewsFirstWhich.setText(category_name);
                    }
                }
                if(img_url!=null) {
                    if(newsHolder.ivNewsFirstPic!=null) {
                        //mIageLoader.loadImage(Url.IMG_URL+img_url,newsHolder.ivNewsFirstPic,true);
                        Glide.with(context).load(Url.IMG_URL+img_url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)//图片的缓存
                                .into(newsHolder.ivNewsFirstPic);//请求成功后把图片设置到的控件
                    }
                }
                if(title!=null) {
                    if(newsHolder.tvNewsFirstTitle!=null) {
                        newsHolder.tvNewsFirstTitle.setText(title);
                    }
                }
                if(add_time!=null) {
                    newsHolder.tvNewsFirstTime.setText(add_time);
//                    try {
//                        Date date = sf.parse(add_time);
//                        long time = date.getTime();
//                        long result_time = curr_time - time;
//                        long day = result_time / (3600000 * 24);
//                        long rest_time = result_time%(3600000 * 24);
//                        long hour = rest_time / 3600000;
//                        long rest_time2 = rest_time%3600000;
//                        long min = rest_time2 / 60000;
//
//
////                        long min = result_time / 60000;
////                        if(newsHolder.tvNewsFirstTime!=null) {
////                            newsHolder.tvNewsFirstTime.setText(min+"分钟前");
////                        }
//                        if(newsHolder.tvNewsFirstTime!=null) {
//                            if(day == 0) {
//                                if(hour ==0) {
//                                    newsHolder.tvNewsFirstTime.setText(min+"分钟前");
//                                }else{
//                                    newsHolder.tvNewsFirstTime.setText(hour+"小时"+min+"分钟前");
//                                }
//                            }else{
//                                if(hour == 0) {
//                                    newsHolder.tvNewsFirstTime.setText(day+"天"+min+"分钟前");
//                                }else{
//                                    newsHolder.tvNewsFirstTime.setText(day+"天"+hour+"小时"+min+"分钟前");
//                                }
//                            }
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                }

                if(newsHolder.ll_news_detail!=null) {
                    newsHolder.ll_news_detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onNewsItemClickListner.onItemClick(view,position);
                        }
                    });
                }
               
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == newsData?0:newsData.size()+extra;
    }

    /**
     * 加载新闻的ViewHolder
     */
    public class NewsFirstViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_news_first_pic)
        ImageView ivNewsFirstPic;
        @Bind(R.id.tv_news_first_title)
        TextView tvNewsFirstTitle;
        @Bind(R.id.tv_news_first_which)
        TextView tvNewsFirstWhich;
        @Bind(R.id.tv_news_first_count)
        TextView tvNewsFirstCount;
        @Bind(R.id.tv_news_first_time)
        TextView tvNewsFirstTime;
        @Bind(R.id.ll_news_detail)
        LinearLayout ll_news_detail;

        public NewsFirstViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * footer view
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_prompt)
        TextView tv_load_prompt;
        @Bind(R.id.progress)
        ProgressBar progress;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 40));
            itemView.setLayoutParams(params);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    progress.setVisibility(View.VISIBLE);
                    tv_load_prompt.setText("正在加载...");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("上拉加载更多");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    System.out.println("LOAD_NONE----");
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("已无更多加载");
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                default:
                    break;
            }
        }

    }

    // change recycler state
    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }
    private OnNewsItemClickListner onNewsItemClickListner;

    public interface OnNewsItemClickListner{
        void onItemClick(View v,int position);
    }

    public void setOnNewsItemClickListner(OnNewsItemClickListner onNewsItemClickListner) {
        this.onNewsItemClickListner = onNewsItemClickListner;
    }
}
