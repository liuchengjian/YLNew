package lchj.ylnews.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TResult;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import lchj.ylnews.app.utils.Const;
import lchj.ylnews.app.utils.GlideUtils;
import lchj.ylnews.app.utils.PrefUtils;
import lchj.ylnews.app.utils.Utils;
import lchj.ylnews.mvp.model.entity.UserBean;
import lchj.ylnews.mvp.ui.widget.HeaderBar;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.UserInfoPresenter;

import lchj.ylnews.R;


/**
 * 编辑用户activity
 */
public class UserInfoActivity extends BaseTakePhotoActivity<UserInfoPresenter> implements IView {
    @BindView(R.id.mHeaderBar)
    HeaderBar mHeaderBar;

    @BindView(R.id.mUserIconIv)//头像
    ImageView mUserIconIv;
    @BindView(R.id.mUserNameEt)//名称
    EditText mUserNameEt;
    @BindView(R.id.mGenderMaleRb)//男
    RadioButton mGenderMaleRb;
    @BindView(R.id.mGenderFemaleRb)//女
    RadioButton mGenderFemaleRb;
    @BindView(R.id.mUserPwdTv)//密码
    EditText mUserPwdTv;
    @BindView(R.id.mUserSignEt)//签名
    EditText mUserSignEt;

    private String mLocalFileUrl;//选择图片地址
    private UserBean user;
    private int sex = 0;
    private RxPermissions mRxPermissions;


    @OnClick({R.id.mUserIconIv,})
    void onClick(View view){
        switch (view.getId()){
            case R.id.mUserIconIv:
                showAlertView();
                break;
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_user_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initBaseData();

    }

    /**
     * 初始化基础数据
     */
    private void initBaseData() {
        user = Utils.getUser();
        if(user!=null){
            Glide.with(this)
                    .load(user.getImage())
                    .apply(GlideUtils.GlideOptions(R.drawable.icon_default_user,//加载前
                            R.drawable.icon_default_user,//加载url为空
                            R.drawable.icon_default_user))//加载错误
                    .into(mUserIconIv);
            mUserNameEt.setText(user.getUsername());
            mUserPwdTv.setText(user.getPassword());
            if(user.getSex() == 1){
                mGenderMaleRb.setChecked(true);
                mGenderFemaleRb.setChecked(false);
            }else {
                mGenderMaleRb.setChecked(false);
                mGenderFemaleRb.setChecked(true);
            }
            mUserSignEt.setText(user.getSignature());
        }


        mHeaderBar.onClickRightTV(v -> {
            if(mGenderMaleRb.isChecked()){
                sex = 1;
            }  else {
                sex = 2;
            }
            mPresenter.getSaveUser(Message.obtain(this, new Object[]{
                    mLocalFileUrl,
                    sex,
                    mUserNameEt.getText().toString(),
                    mUserPwdTv.getText().toString(),
                    mUserSignEt.getText().toString(),
            }));
        });
    }

    @Override
    @Nullable
    public UserInfoPresenter obtainPresenter() {
        mRxPermissions = new RxPermissions(this);
        return new UserInfoPresenter(ArtUtils.obtainAppComponentFromContext(this),mRxPermissions);
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
                if(message.arg1 ==1){
                    ArtUtils.makeText(this,"保存成功");
                    finish();
                }
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        mLocalFileUrl = result.getImage().getOriginalPath();
//        PrefUtils.getInstance().put(Const.KEY_SP_USER_ICON, mLocalFileUrl);
        user.setImage(mLocalFileUrl);
        initBaseData();
    }
}
