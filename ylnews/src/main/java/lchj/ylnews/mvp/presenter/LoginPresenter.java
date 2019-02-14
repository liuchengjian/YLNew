package lchj.ylnews.mvp.presenter;

import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import lchj.ylnews.app.BaseApplication;
import lchj.ylnews.app.utils.PrefUtils;
import lchj.ylnews.app.utils.Utils;
import lchj.ylnews.mvp.model.api.service.ResultIndex;
import lchj.ylnews.mvp.model.api.service.ResultInfo;
import lchj.ylnews.mvp.model.entity.LoginBody;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.model.entity.TokenBean;
import me.jessyan.art.base.App;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import lchj.ylnews.mvp.model.LoginRepository;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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
public class LoginPresenter extends BasePresenter<LoginRepository> {
    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;

    public LoginPresenter(AppComponent appComponent,RxPermissions mRxPermissions) {
        super(appComponent.repositoryManager().createRepository(LoginRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
        this.mRxPermissions = mRxPermissions;
    }


    public void ToLogin(int type,final Message msg){
        IView view = msg.getTarget();
        String phone = (String) msg.objs[0];
        LoginBody body = null;
        if(type ==1){
            String VerifyCode = (String) msg.objs[1];
            body =new LoginBody(type,phone,"",VerifyCode);
        }else {
            String password = (String) msg.objs[1];
            body =new LoginBody(type,phone,password,"");
        }

        Utils.getPermissions(view, mRxPermissions, mErrorHandler);

        mModel.ToLogin(body)
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
                .subscribe(new ErrorHandleSubscriber<ResultInfo<TokenBean>>(mErrorHandler) {
                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        view.showMessage("获取登陆数据失败[" + Utils.getThrowsMsg(e) + "]");
                        msg.handleMessageToTarget();
                    }
                    @Override
                    public void onNext(ResultInfo<TokenBean> ResultData) {
                        Log.e("1111111","resultInfo:"+ResultData);
                        if (!ResultData.getSuccess() ||
                                ResultData.getData() == null) {
                            view.showMessage(ResultData.getMessage());
                            return;
                        }
                        if( ResultData.getData()!=null){
                            TokenBean tokenBean =  ResultData.getData();
                            Utils.setToken(BaseApplication.mContext,tokenBean.getToken());
                            PrefUtils.getInstance().put("token",tokenBean.getToken());
                            msg.what = 1;
                            msg.arg1 = 1;
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