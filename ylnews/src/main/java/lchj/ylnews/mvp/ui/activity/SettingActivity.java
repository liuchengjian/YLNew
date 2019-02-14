package lchj.ylnews.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;
import lchj.ylnews.app.utils.CacheDataUtils;
import lchj.ylnews.app.utils.PrefUtils;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.SettingPresenter;

import lchj.ylnews.R;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 02/11/2019 09:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SettingActivity extends YLBaseActivity<SettingPresenter> implements IView {
    @BindView(R.id.mCleanAllCache)
    TextView mCleanAllCache;
    @BindView(R.id.mCleanCacheSize)
    TextView mCleanCacheSize;

    @BindColor(R.color.colorAccent)
    int title_color;
    @BindColor(R.color.common_blue)
    int gray_color;
    @BindColor(R.color.colorPrimary)
    int blue_color;

    @OnClick(R.id.mCleanAllCache)
    void OnClick(){
        new MaterialDialog.Builder(this)
                .title(R.string.setting_clear_cache)
                .titleGravity(GravityEnum.CENTER)
                .content(R.string.is_clear_cache)
                .positiveColor(gray_color)
                .positiveText(R.string.sure)
                .negativeText(R.string.cancel)
                .negativeColor(blue_color)
                .onAny((dialog, which) -> {
                    if (which == DialogAction.POSITIVE) {
                        CacheDataUtils.clearAllCache(this);
                        PrefUtils.getInstance().clear();
                        setupAppCache();
                        ArtUtils.makeText(this, getString(R.string.clear_success));
                    }
                })
                .show();
    }
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_setting; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setupAppCache();
    }

    @Override
    @Nullable
    public SettingPresenter obtainPresenter() {
        return new SettingPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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


    //设置缓存大小
    public void setupAppCache() {
        String size = CacheDataUtils.getTotalCacheSize(this);
        mCleanCacheSize.setText("0 B".equals(size) ?
                getString(R.string.no_cache) : size);
    }
}
