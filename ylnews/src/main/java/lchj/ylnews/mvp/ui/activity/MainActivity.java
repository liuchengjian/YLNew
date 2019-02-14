package lchj.ylnews.mvp.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RelativeLayout;

import com.qihoo360.replugin.RePlugin;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lchj.ylnews.R;
import lchj.ylnews.app.utils.FragmentUtils;
import lchj.ylnews.mvp.ui.fragment.HomeFragment;
import lchj.ylnews.mvp.ui.fragment.MyFragment;
import lchj.ylnews.mvp.ui.fragment.OrderFragment;
import me.jessyan.art.base.App;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static lchj.ylnews.app.utils.Const.ACTIVITY_FRAGMENT_REPLACE;
import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.MainPresenter;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 01/22/2019 10:41
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class MainActivity extends YLBaseActivity<MainPresenter> implements IView {

    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;
    @BindView(R.id.toolbar_back)
    RelativeLayout mToolbarBack;
    private List<Integer> mTitles;
    private List<Fragment> mFragments;
    private List<Integer> mNavIds;
    private int mReplace = 0;
    private long exitTime = 0; ////记录第一次点击的时间
    HomeFragment homeFragment;
    OrderFragment orderFragment;
    MyFragment myFragment;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        if (mTitles == null) {
            mTitles = new ArrayList<>();
            mTitles.add(R.string.nav_bar_home);
            mTitles.add(R.string.nav_bar_order);
            mTitles.add(R.string.nav_bar_user);
        }
        if (mNavIds == null) {
            mNavIds = new ArrayList<>();
            mNavIds.add(R.id.tab_home);
            mNavIds.add(R.id.tab_order);
            mNavIds.add(R.id.tab_user);
        }
        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            orderFragment = OrderFragment.newInstance();
            myFragment = MyFragment.newInstance();

        } else {
            mReplace = savedInstanceState.getInt(ACTIVITY_FRAGMENT_REPLACE);
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = (HomeFragment) FragmentUtils.findFragment(fm, HomeFragment.class);
            orderFragment = (OrderFragment) FragmentUtils.findFragment(fm, OrderFragment.class);
            myFragment = (MyFragment) FragmentUtils.findFragment(fm, MyFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(homeFragment);
            mFragments.add(orderFragment);
            mFragments.add(myFragment);
        }
        FragmentUtils.addFragments(getSupportFragmentManager(), mFragments, R.id.main_frame, 0);
        mBottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_home:
                    mReplace = 0;
                    break;
                case R.id.tab_order:
                    mReplace = 1;
//                    RePlugin.startActivity(MainActivity.this, RePlugin.createIntent("com.dqgb.fxsn", "com.dqgb.fxsn.LoginActivity"));
                    break;
                case R.id.tab_user:
                    mReplace = 2;
                    break;
            }
            FragmentUtils.hideAllShowFragment(mFragments.get(mReplace));
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //保存当前Activity显示的Fragment索引
        outState.putInt(ACTIVITY_FRAGMENT_REPLACE, mReplace);
    }

    @Override
    @Nullable
    public MainPresenter obtainPresenter() {
        return new MainPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }
    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dissmissLoadingDialog();
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
                break;
        }
    }

    /**
     * 双击退出app
     */
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ArtUtils.makeText(this, getString(R.string.go_on_exit));
            exitTime = System.currentTimeMillis();
        } else {
            //killMyself();
            ((App) getApplication()).getAppComponent().appManager().appExit();
            //System.exit(0);//正常退出App
            //android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
