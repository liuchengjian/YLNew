package lchj.ylnews.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lchj.ylnews.app.utils.GlideImageLoader;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.ui.activity.MainActivity;
import lchj.ylnews.mvp.ui.activity.NewDetailsActivityActivity;
import lchj.ylnews.mvp.ui.adapter.NewsAdapter;
import lchj.ylnews.mvp.ui.widget.RecycleViewDivider;
import me.jessyan.art.base.BaseFragment;
import me.jessyan.art.base.DefaultAdapter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.HomePresenter;

import lchj.ylnews.R;


/**
 *create lChj
 *
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IView,DefaultAdapter.OnRecyclerViewItemClickListener,OnBannerListener{
    private RxPermissions mRxPermissions;
    private NewsAdapter mAdapter;
    List<String> imageUrls = new ArrayList<>();
    List<Integer>ids = new ArrayList<>();
    List<NewsBean> newsHeadsList = new ArrayList<>();
    List <NewsBean> newsBeanList = new ArrayList<>();
    String[] url ={"http://www.iyi8.com/uploadfile/2017/1224/20171224115223386.jpg","http://www.iyi8.com/uploadfile/2017/1224/20171224115223386.jpg"};

    @BindView(R.id.mBanner)
    Banner mBanner;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getIndexlist(Message.obtain(this, new Object[]{}));

        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getContext()));
        //分割线
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayoutManager.HORIZONTAL,
                R.drawable.divider_mileage));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mBanner.setOnBannerListener(this);
    }

    /**
     * 设置Banner属性
     */
    private void setBanner(List<String> imageUrls) {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置banner样式
        mBanner.setImageLoader(new GlideImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Accordion);
        //设置标题集合（当banner样式有显示title时）
//        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setImages(imageUrls);
//        mBanner.setImages(Arrays.asList(Const.HOME_BANNER_ONE,Const. HOME_BANNER_TWO, Const.HOME_BANNER_THREE, Const.HOME_BANNER_FOUR));
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }


    @Override
    @Nullable
    public HomePresenter obtainPresenter() {
        this.mRxPermissions = new RxPermissions(this);
        //初始化Adapter在这
        if(mAdapter ==null ){
            mAdapter = new NewsAdapter(new ArrayList<>());
        }
        return new HomePresenter(ArtUtils.obtainAppComponentFromContext(getActivity()),mRxPermissions,mAdapter);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
//        ((MainActivity) getActivity()).showLoading();
    }

    @Override
    public void hideLoading() {
//        ((MainActivity) getActivity()).hideLoading();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArtUtils.snackbarText(message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case 0:

                break;
            case 1:
                if (message.arg1 == 1) {
                    //获取列表
                    onHeadsSuccess((List<NewsBean>) message.obj);
                }
                break;
        }
    }

    private void onHeadsSuccess(List<NewsBean> newsHeadsLists) {
        for(NewsBean newsBean :newsHeadsLists ){
            ids.add(newsBean.getId());
            imageUrls.add(newsBean.getImage());
        }
        if (imageUrls != null) {
            setBanner(imageUrls);
        }
    }

    /**
     * 子项点击事件
     * @param view
     * @param viewType
     * @param data
     * @param position
     */
    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        NewsBean bean = (NewsBean) data;
        int id = bean.getId();
        Intent intent = new Intent(getContext(), NewDetailsActivityActivity.class);
        intent.putExtra("id",id);
        ArtUtils.startActivity(intent);
    }

    @Override
    public void OnBannerClick(int position) {
        int id = ids.get(position);
        Intent intent = new Intent(getContext(), NewDetailsActivityActivity.class);
        intent.putExtra("id",id);
        ArtUtils.startActivity(intent);
    }
}
