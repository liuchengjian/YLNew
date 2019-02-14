package lchj.ylnews.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lchj.ylnews.mvp.ui.widget.VerifyButton;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.LoginPresenter;

import lchj.ylnews.R;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 01/30/2019 14:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class LoginActivity extends YLBaseActivity<LoginPresenter> implements IView {
    private RxPermissions rxPermissions;
    @BindView(R.id.mMobileEt)
    EditText mMobileEt;
    @BindView(R.id.mPwdEt)
    EditText mPwdEt;

    @BindView(R.id.mMobileEt1)
    EditText mMobileEt1;
    @BindView(R.id.mVerifyCodeBtn)
    VerifyButton mVerifyCodeBtn;
    @BindView(R.id.llPwdToLogin)
    LinearLayout llPwdToLogin;
    @BindView(R.id.llVerifyToLogin)
    LinearLayout llVerifyToLogin;

    @BindView(R.id.mVerifyCodeEt)
    EditText mVerifyCodeEt;
    @BindView(R.id.mForgetPwdTv)
    TextView mForgetPwdTv;
    //选择登录的type
    private int Type = 0;

    @BindView(R.id.mLoginBtn)
    Button mLoginBtn;
    @OnClick({R.id.mForgetPwdTv,R.id.mVerifyCodeBtn,R.id.mLoginBtn})
    void onClick(View v){
        switch (v.getId()){
            case R.id.mForgetPwdTv:
                //密码登录或者验证码登录
                showLogin(Type);
                break;
            case R.id.mVerifyCodeBtn:
                //发送验证码
                mVerifyCodeBtn.requestSendVerifyNumber();
                ArtUtils.makeText(this,"发送验证成功");
                break;

            case R.id.mLoginBtn:
                if(Type ==1){
                    //验证码登录
                    mPresenter.ToLogin(Type,Message.obtain(this, new Object[]{mMobileEt1.getText().toString(),mVerifyCodeEt.getText().toString()}));
                } else {
                    //密码登录
                    mPresenter.ToLogin(Type,Message.obtain(this, new Object[]{mMobileEt.getText().toString(),mPwdEt.getText().toString()}));
                }
                break;
        }
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //显示该登录状态
        showLogin(Type);
    }

    @Override
    @Nullable
    public LoginPresenter obtainPresenter() {
        rxPermissions = new RxPermissions(this);
        return new LoginPresenter(ArtUtils.obtainAppComponentFromContext(this),rxPermissions);
    }

    @Override
    public void showLoading() {
        showLoadingDialog("登录中····");
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
                if (message.arg1 == 1) {
                    //获取列表
//                    ArtUtils.startActivity(MainActivity.class);
                    finish();
                }
                break;
        }
    }

    /**
     * 显示该登录状态
     * @param type
     */
    private void showLogin(int type){
        if(type ==0){
            Type = 1;
            llPwdToLogin.setVisibility(View.GONE);
            llVerifyToLogin.setVisibility(View.VISIBLE);
            mForgetPwdTv.setText("密码登录？");
        }else {
            Type = 0;
            llPwdToLogin.setVisibility(View.VISIBLE);
            llVerifyToLogin.setVisibility(View.GONE);
            mForgetPwdTv.setText("验证码登录？");
        }
    }
}
