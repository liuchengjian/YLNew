package lchj.ylnews.mvp.presenter;

import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import lchj.ylnews.app.utils.Utils;
import lchj.ylnews.mvp.model.api.service.ResultInfo;
import lchj.ylnews.mvp.model.entity.CatBean;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import lchj.ylnews.mvp.model.OrderRepository;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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
public class OrderPresenter extends BasePresenter<OrderRepository> {
    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;
    private List<String>CatNameLst= new ArrayList<>();
    public OrderPresenter(AppComponent appComponent,RxPermissions mRxPermissions) {
        super(appComponent.repositoryManager().createRepository(OrderRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
        this.mRxPermissions = mRxPermissions;
    }

    public void getCatList(final Message msg){
        IView view = msg.getTarget();
//        String type = (String) msg.objs[0];
        Utils.getPermissions(view, mRxPermissions, mErrorHandler);
        mModel.getCatList()
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
                .subscribe(new ErrorHandleSubscriber<ResultInfo<List<CatBean>>>(mErrorHandler) {
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        view.showMessage("获取其他数据失败[" + Utils.getThrowsMsg(e) + "]");
                    }
                    @Override
                    public void onNext(ResultInfo<List<CatBean>> ResultData) {
                        Log.e("1111111","resultInfo:"+ResultData);
                        if (!ResultData.getSuccess() ||
                                ResultData.getData() == null) {
                            view.showMessage(ResultData.getMessage());
                            return;
                        }
                        if( ResultData.getData().size()>0){
                            List<CatBean> CatBeanList = ResultData.getData();

                            msg.what = 1;
                            msg.arg1 = 1;
                            msg.obj = CatBeanList;
                            msg.handleMessageToTarget();
                        }

                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}