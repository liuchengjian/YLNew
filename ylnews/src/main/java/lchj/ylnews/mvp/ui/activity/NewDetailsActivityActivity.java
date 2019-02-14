package lchj.ylnews.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lchj.ylnews.app.utils.ACache;
import lchj.ylnews.app.utils.Utils;
import lchj.ylnews.mvp.model.entity.NewsDetailsBean;
import lchj.ylnews.mvp.model.entity.UpvoteBean;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.ArtUtils;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

import lchj.ylnews.mvp.presenter.NewDetailsActivityPresenter;

import lchj.ylnews.R;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArtTemplate on 01/29/2019 16:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NewDetailsActivityActivity extends YLBaseActivity<NewDetailsActivityPresenter> implements IView {
    private RxPermissions mRxPermissions;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvUpdateTime)
    TextView tvUpdateTime;
    @BindView(R.id.tvReadCount)
    TextView tvReadCount;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.ivUpvote)
    ImageView ivUpvote;
    @BindView(R.id.tvUpvoteCount)
    TextView tvUpvoteCount;
    @BindView(R.id.mTextWebView)
    WebView mTextWebView;

    private Boolean isUpvote= false;
    private int id =0;
    private int upvoteCount = 0;

    private ACache acache;//缓存框架
    private Gson gson;

    @OnClick(R.id.ivUpvote)
    void onUpvoteClick(){
        if(TextUtils.isEmpty(Utils.getToken(this))){
            ArtUtils.startActivity(LoginActivity.class);
//            ArtUtils.makeText(this,getString(R.string.please_login));
            return;
        }
        if(isUpvote){
            //取消点赞
            mPresenter.toUpvoteNo(Message.obtain(this, new Object[]{id}));
            if(upvoteCount!=0){
                upvoteCount=upvoteCount-1;
            }
        }else{
            //点赞
            mPresenter.toUpvote(Message.obtain(this, new Object[]{id}));
            upvoteCount=upvoteCount+1;

        }

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        acache=ACache.get(this);//创建ACache组件
        gson = new Gson();
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        final NewsDetailsBean newsDetailsBean = (NewsDetailsBean)acache.getAsObject("NewsDetailsBean");
        if(newsDetailsBean!=null){
            initBaseDate(newsDetailsBean);
        }else {
            //网络请求
            mPresenter.getNewDetails(Message.obtain(this, new Object[]{id}));
        }
        if(!TextUtils.isEmpty(Utils.getToken(this))){
            mPresenter.isUpvote(Message.obtain(this, new Object[]{id}));
        }

    }



    @Override
    @Nullable
    public NewDetailsActivityPresenter obtainPresenter() {
        this.mRxPermissions = new RxPermissions(this);
        return new NewDetailsActivityPresenter(ArtUtils.obtainAppComponentFromContext(this),mRxPermissions);
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
                if (message.arg1 == 2) {
                    //获取列表
                    onLoadSuccess((NewsDetailsBean) message.obj);
                    break;
                }else if(message.arg1 == 3) {
                    //点赞成功
                    onIsUpvoteSuccess((UpvoteBean) message.obj);
                    break;
                }else if(message.arg1 == 4) {
                    //点赞成功
                    onUpvoteSuccess(true);
                    break;
                }else if(message.arg1 == 5) {
                    //点赞成功
                    onUpvoteSuccess(false);
                    break;
                }
        }
    }

    private void onIsUpvoteSuccess(UpvoteBean obj) {
        if(obj!=null){
            if (obj.getIsUpvote()==1){
                onUpvoteSuccess(true);
            }else {
                onUpvoteSuccess(false);
            }
        }
    }

    /**
     * 获取成功
     * @param obj
     */
    private void onLoadSuccess(NewsDetailsBean obj) {
        if(obj!=null){
//            acache.put("NewsDetailsBean", obj, 60*60*1);//将数据存入缓存中，有效时间设置为1小时
            initBaseDate(obj);

        }

    }

    /**
     * 点赞
     */

    private void onUpvoteSuccess(Boolean upvote) {
        if(upvote){
            //点赞成功
            isUpvote = true;
            ivUpvote.setImageResource(R.drawable.icon_upvote_yes);
        }else {
            //取消点赞成功
            isUpvote = false;
            ivUpvote.setImageResource(R.drawable.icon_upvote_no);
        }
        tvUpvoteCount.setText(String.valueOf(upvoteCount));
    }


    private void initBaseDate(NewsDetailsBean obj){
        tvTitle.setText(obj.getTitle());
        tvUpdateTime.setText(obj.getUpdate_time());
        tvReadCount.setText(String.valueOf(obj.getRead_count())+"人看过");
        tvDescription.setText(obj.getDescription());

        mTextWebView.loadDataWithBaseURL(null, obj.getContent(), "text/html", "utf-8", null);
        upvoteCount = Integer.parseInt(obj.getUpvote_count());
        tvUpvoteCount.setText(obj.getUpvote_count());
    }

}
