package lchj.ylnews.mvp.presenter;

import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import lchj.ylnews.app.utils.Const;
import lchj.ylnews.app.utils.Utils;
import lchj.ylnews.mvp.model.api.service.ResultIndex;
import lchj.ylnews.mvp.model.api.service.ResultInfo;
import lchj.ylnews.mvp.model.api.service.ResultList;
import lchj.ylnews.mvp.model.entity.CatBean;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.ui.adapter.NewsAdapter;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import lchj.ylnews.mvp.model.BaseNewsRepository;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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
public class BaseNewsPresenter extends BasePresenter<BaseNewsRepository> {
    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;
    List<NewsBean>newsBeanList = new ArrayList<>();
    private NewsAdapter mAdapter;
    private int page ,size = Const.page_limit;
    public BaseNewsPresenter(AppComponent appComponent,RxPermissions rxPermissions,NewsAdapter mAdapter) {
        super(appComponent.repositoryManager().createRepository(BaseNewsRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
        this.mRxPermissions = rxPermissions;
        this.mAdapter = mAdapter;
        if (mAdapter!=null){
            this.newsBeanList = mAdapter.getInfos();
        }
    }
    public void getNewsList(final Message msg,boolean isRefresh){
        IView view = msg.getTarget();
        int catid = (int) msg.objs[0];
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        Utils.getPermissions(view, mRxPermissions, mErrorHandler);
        mModel.getNewlist(catid,page,size)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    addDispose(disposable);
                    view.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    view.hideLoading();//隐藏下拉刷新的进度条
                    //因为hideLoading,为默认方法,直接可以调用所以不需要发送消息给handleMessage()来处理,
                    //HandleMessageToTarget()的原理就是发送消息并回收消息
                    //调用默认方法后不需要调用HandleMessageToTarget(),但是如果后面对view没有其他操作了请调用message.recycle()回收消息
                    //msg.recycle();
                })
                .subscribe(new ErrorHandleSubscriber<ResultInfo<ResultList<NewsBean>>>(mErrorHandler) {
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        msg.what = 0;
                        msg.arg1 = 1;
                        msg.objs = new Object[]{isRefresh, "获取新闻列表数据失败[" + Utils.getThrowsMsg(e) + "]"};

                        newsBeanList.clear();
                        mAdapter.notifyDataSetChanged();
                        msg.handleMessageToTarget();
                    }
                    @Override
                    public void onNext(ResultInfo<ResultList<NewsBean>> ResultData) {
                        Log.e("1111111","resultInfo:"+ResultData);
                        if (!ResultData.getSuccess() ||
                                ResultData.getData() == null) {
                            view.showMessage(ResultData.getMessage());
                            msg.what = 0;
                            msg.arg1 = 1;
                            msg.objs = new Object[]{isRefresh, ResultData.getMessage()};
                            msg.handleMessageToTarget();
                            return;
                        }
                        if( ResultData.getData().getList().size()>0){
                            List<NewsBean> jhNewsList = ResultData.getData().getList();
                            if (jhNewsList == null ||
                                    jhNewsList.isEmpty()) {
                                if (isRefresh && !newsBeanList.isEmpty()) {
                                    newsBeanList.clear();
                                    mAdapter.notifyDataSetChanged();

                                    msg.what = 0;
                                    msg.arg1 = 1;
                                    msg.objs = new Object[]{isRefresh, isRefresh ? "暂无数据" : "没有更多数据了"};
                                    msg.handleMessageToTarget();
                                }
                            }
                            if (isRefresh) {
                                newsBeanList.clear();
                            }
                            newsBeanList.addAll(jhNewsList);
                            mAdapter.notifyDataSetChanged();

                            boolean isNoMore = false;
                            if (newsBeanList.size() >= ResultData.getData().getTotal()) {
                                isNoMore = true;
                            }
                            msg.what = 1;
                            msg.arg1 = 1;
                            msg.objs = new Object[]{isRefresh, isNoMore};
                            msg.handleMessageToTarget();

                        }
                    }
                });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mRxPermissions = null;
    }
}