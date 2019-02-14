package lchj.ylnews.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import lchj.ylnews.R;
import lchj.ylnews.mvp.model.entity.NewsBean;
import me.jessyan.art.base.BaseHolder;

public class NewsHolder extends BaseHolder<NewsBean> {
    @BindView(R.id.iv)
    ImageView mImageView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvCatName)
    TextView tvCatName;
    @BindView(R.id.tvReadCount)
    TextView tvReadCount;
    @BindView(R.id.tvUpdateTime)
    TextView tvUpdateTime;
    public NewsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(NewsBean data, int position) {
//        mImageView.setImageResource(Integer.parseInt(data.getThumbnail_pic_s()));
        Glide.with(itemView.getContext())
                .load(data.getImage())
//                .placeholder(R.mipmap.ic_launcher)//图片加载出来前，显示的图片
//                .error(R.drawable.black_background)//图片加载失败后，显示的图片
                .into(mImageView);
        tvTitle.setText(data.getTitle());
        tvCatName.setText("栏目:"+data.getCatname());
        tvReadCount.setText("阅读数:"+String.valueOf(data.getRead_count())+"次");
        tvUpdateTime.setText("更新时间:"+data.getUpdate_time());
    }

    @Override
    protected void onRelease() {
        this.tvTitle = null;
        this.tvCatName = null;
        this.tvReadCount = null;
        this.tvUpdateTime = null;

    }
}
