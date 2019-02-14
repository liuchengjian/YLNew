package lchj.ylnews.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lchj.ylnews.R;
import lchj.ylnews.app.utils.DateUtils;
import me.jessyan.art.base.BaseFragment;
import me.jessyan.art.base.delegate.IActivity;
import me.jessyan.art.integration.cache.Cache;
import me.jessyan.art.integration.cache.CacheType;
import me.jessyan.art.mvp.IPresenter;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.ThirdViewUtil.convertAutoView;

public abstract class BaseTakePhotoActivity <P extends IPresenter> extends AppCompatActivity implements IActivity<P>,TakePhoto.TakeResultListener , InvokeListener {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;
    protected P mPresenter;

    private MaterialDialog loadingDialog;

    private TakePhoto mTakePhoto;
    private InvokeParam invokeParam;
    private File mTempFile;


    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = convertAutoView(name, context, attrs);
        return view == null ? super.onCreateView(name, context, attrs) : view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果 initView 返回 0, 框架则不会调用 setContentView(), 当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadingDialog = new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .build();
        //初始化TakePhoto
        if (mTakePhoto == null) {

            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));

        }
        mTakePhoto =new TakePhotoImpl(this,this);
        mTakePhoto.onCreate(savedInstanceState);


        initData(savedInstanceState);
    }

    @Override
    public void setPresenter(@Nullable P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null) mPresenter = obtainPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        this.mPresenter = null;
        this.mUnbinder = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }

    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void showLoadingDialog(String msg) {
        if (loadingDialog != null) {
            loadingDialog.setContent(msg);
            loadingDialog.show();
        }
    }

    /**
     * 取消加载框
     */
    public void dissmissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


    /**
     * 弹出选择框，默认实现
     可根据实际情况，自行修改
     */
    protected void showAlertView() {
        new AlertView("选择图片", "", "取消", null,
                new String[]{"拍照", "相册"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener(){
            public void onItemClick(Object o,int position){
                switch (position){
                    case  0:
                        createTempFile();
                        mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile));
                        break;
                    case 1:
                        mTakePhoto.onPickFromGallery();
                        break;
                }

            }
        }).show();


    }
    /**
     * 获取图片，成功回调
     */
    @SuppressLint("LogNotTimber")
    @Override
    public void takeSuccess(TResult result) {
        Log.d("TakePhoto","TakePhoto:"+result.getImage().getOriginalPath());
        Log.d("TakePhoto","TakePhoto:"+result.getImage().getCompressPath());
    }
    /**
     * 获取图片，取消回调
     */
    @Override
    public void takeCancel() {

    }
    /**
     * 获取图片，失败回调
     */
    @SuppressLint("LogNotTimber")
    @Override
    public void takeFail(TResult result, String msg) {
        Log.e("takePhoto",msg);
    }
    /**
     * TakePhoto默认实现
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhoto.onActivityResult(requestCode,resultCode,data);
    }

    @Override

    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;

    }



    /**
     * 新建临时文件
     */
    public void createTempFile(){
        String tempFileName = DateUtils.getCurrentDateTime()+".png";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            this.mTempFile = new File(Environment.getExternalStorageDirectory(),tempFileName);
            return ;
        }

        this.mTempFile = new File(getFilesDir(),tempFileName);
    }
}
