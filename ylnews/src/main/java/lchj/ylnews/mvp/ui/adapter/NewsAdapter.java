package lchj.ylnews.mvp.ui.adapter;

import android.view.View;

import java.util.List;

import lchj.ylnews.R;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.ui.holder.NewsHolder;
import me.jessyan.art.base.BaseHolder;
import me.jessyan.art.base.DefaultAdapter;

public class NewsAdapter extends DefaultAdapter<NewsBean> {
    public NewsAdapter(List<NewsBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<NewsBean> getHolder(View v, int viewType) {
        return new NewsHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_news_list;
    }
}
