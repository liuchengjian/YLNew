package lchj.ylnews.mvp.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lchj.ylnews.mvp.model.entity.CatBean;
import lchj.ylnews.mvp.ui.activity.MainActivity;
import me.jessyan.art.base.BaseFragment;
import me.jessyan.art.base.delegate.IFragment;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.OrderPresenter;

import lchj.ylnews.R;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 01/22/2019 11:13
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class OrderFragment extends BaseFragment<OrderPresenter> implements IView {
    private RxPermissions mRxPermissions;
    @BindView(R.id.tabLayout)
    TabLayout mtablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private List<Fragment> list;
    private MyAdapter adapter;
    List<String>catnameList = new ArrayList<>();
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mPresenter.getCatList(Message.obtain(this, new Object[]{}));

    }

    @Override
    @Nullable
    public OrderPresenter obtainPresenter() {
        this.mRxPermissions = new RxPermissions(this);
        return new OrderPresenter(ArtUtils.obtainAppComponentFromContext(getActivity()),mRxPermissions);
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
                    onCatListSuccess((List<CatBean>) message.obj);
                }
                break;
        }
    }

    private void onCatListSuccess(List<CatBean> CatBeanList) {
        for(CatBean catBean:CatBeanList){
            catnameList.add(catBean.getCatname());
        }

        setFragmentList(catnameList);
    }

    /**
     * 根据请求到的catnameList动态设置子fragmengt
     * @param catnameList
     */
    private void setFragmentList(List<String> catnameList) {
        list = new ArrayList<>();
        for (int i=0;i<catnameList.size();i++){
            list.add(BaseNewsFragment.newInstance(i));
        }
        //ViewPager的适配器
        adapter = new MyAdapter(getChildFragmentManager(), getActivity());
        mViewpager.setCurrentItem(0);
        mViewpager.setAdapter(adapter);
        //绑定
        mtablayout.setupWithViewPager(mViewpager);
        //MODE_SCROLLABLE可滑动的展示
        //MODE_FIXED固定展示
        mtablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置自定义视图
        for (int i = 0; i < mtablayout.getTabCount(); i++) {
            TabLayout.Tab tab = mtablayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        private Context context;

        public MyAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * 自定义方法，提供自定义Tab
         *
         * @param position 位置
         * @return 返回Tab的View
         */
        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.new_navigation_bar, null);
            TextView textView = v.findViewById(R.id.tv_new_navigation_bar);
            textView.setText(catnameList.get(position));
            //添加一行，设置颜色
            textView.setTextColor(mtablayout.getTabTextColors());//
            return v;
        }
    }
}
