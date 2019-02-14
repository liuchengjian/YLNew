package lchj.ylnews.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.ui.activity.MainActivity;
import lchj.ylnews.mvp.ui.activity.NewDetailsActivityActivity;
import lchj.ylnews.mvp.ui.adapter.NewsAdapter;
import lchj.ylnews.mvp.ui.widget.RecycleViewDivider;
import me.jessyan.art.base.BaseApplication;
import me.jessyan.art.base.BaseFragment;
import me.jessyan.art.base.DefaultAdapter;
import me.jessyan.art.base.delegate.IFragment;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.BaseNewsPresenter;

import lchj.ylnews.R;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 01/30/2019 08:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class BaseNewsFragment extends BaseFragment<BaseNewsPresenter> implements IView,OnRefreshListener, OnLoadmoreListener,DefaultAdapter.OnRecyclerViewItemClickListener{
    private RxPermissions mRxPermissions;
    private int CatId = 0;
    private NewsAdapter mAdapter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    public static BaseNewsFragment newInstance(int catId) {
        BaseNewsFragment fragment = new BaseNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("catid",catId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_news, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        CatId = getArguments().getInt("catid");
        loadNewsList(true);
        Log.e("333333333","CatId:"+String.valueOf(CatId));
        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(getContext()));
        //分割线
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayoutManager.HORIZONTAL,
                R.drawable.divider_mileage));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
    }

    @Override
    @Nullable
    public BaseNewsPresenter obtainPresenter() {
        this.mRxPermissions = new RxPermissions(this);
        if (mAdapter==null){
            mAdapter = new NewsAdapter(new ArrayList<>());
        }
        return new BaseNewsPresenter(ArtUtils.obtainAppComponentFromContext(getActivity()),mRxPermissions,mAdapter);
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
                onLoadFail((boolean) message.objs[0], message.objs[1].toString());
                break;
            case 1:
                if (message.arg1 == 1) {//获取消息列表
                    onLoadSuccess((boolean) message.objs[0]);
                    mRefreshLayout.setLoadmoreFinished((boolean) message.objs[1]);
                }
                break;
        }
    }

    private void onLoadSuccess(boolean refresh) {
        if (refresh) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadmore();
        }
    }

    private void onLoadFail(boolean refresh, String msg) {
        if (refresh) {
            mRefreshLayout.finishRefresh();
        } else {
            showMessage(msg);
            mRefreshLayout.finishLoadmore();
        }
    }

    /**
     * 子项的点击事件
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

    private void loadNewsList(boolean isfresh){
        mPresenter.getNewsList(Message.obtain(this, new Object[]{CatId}),isfresh);
    }

    /**
     * 下拉刷新
     * @param refreshlayout
     */
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadNewsList(true);
    }
    /**
     * 上拉加载
     * @param refreshlayout
     */
    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        loadNewsList(false);
    }


}
