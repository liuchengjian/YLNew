package lchj.ylnews.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;


import butterknife.BindView;
import butterknife.OnClick;
import lchj.ylnews.R;
import lchj.ylnews.app.utils.GlideUtils;
import lchj.ylnews.app.utils.Utils;
import lchj.ylnews.mvp.model.entity.UserBean;
import lchj.ylnews.mvp.ui.activity.LoginActivity;
import lchj.ylnews.mvp.ui.activity.SettingActivity;
import lchj.ylnews.mvp.ui.activity.UserInfoActivity;
import me.jessyan.art.base.BaseFragment;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.MyPresenter;



/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 01/22/2019 11:14
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class MyFragment extends BaseFragment<MyPresenter> implements IView {
    private RxPermissions rxPermissions;
    @BindView(R.id.mUserNameTv)
    TextView mUserNameTv;
    @BindView(R.id.mUserIconIv)
    ImageView mUserIconIv;
    @OnClick({R.id.mUserIconIv,R.id.mSettingTv})
    void onClick(View v){
        switch (v.getId()){
            case R.id.mUserIconIv:
                if(TextUtils.isEmpty(Utils.getToken(getContext()))){
                //第一次登陆
                ArtUtils.startActivity(LoginActivity.class);
            }else {
                ArtUtils.startActivity(UserInfoActivity.class);
                //            mPresenter.getUsersData(Message.obtain(this, new Object[]{}));
            }
            break;
            case R.id.mSettingTv:
                ArtUtils.startActivity(SettingActivity.class);
                break;
        }

    }
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if(!TextUtils.isEmpty(Utils.getToken(getContext()))){
            mPresenter.getUsersData(Message.obtain(this, new Object[]{}));
        }
    }

    @Override
    @Nullable
    public MyPresenter obtainPresenter() {
        rxPermissions = new RxPermissions(this);
        return new MyPresenter(ArtUtils.obtainAppComponentFromContext(getActivity()),rxPermissions);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!TextUtils.isEmpty(Utils.getToken(getContext()))){
            mPresenter.getUsersData(Message.obtain(this, new Object[]{}));
        }
    }

    @Override
    public void setData(@Nullable Object data) {

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
                if (message.arg1 == 1) {
                    //获取列表
                    onUserSuccess();
                }
                break;
        }
    }

    private void onUserSuccess() {
        UserBean user = Utils.getUser();
        if(user!=null){
            mUserNameTv.setText(user.getUsername());
            Glide.with(this)
                    .load(user.getImage())
                    .apply(GlideUtils.GlideOptions(R.drawable.icon_default_user,//加载前
                            R.drawable.icon_default_user,//加载url为空
                            R.drawable.icon_default_user))//加载错误
                    .into(mUserIconIv);
        }else {
            mUserNameTv.setText(R.string.un_login_text);
        }


    }
}
