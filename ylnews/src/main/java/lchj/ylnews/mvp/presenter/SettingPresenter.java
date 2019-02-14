package lchj.ylnews.mvp.presenter;

import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import lchj.ylnews.mvp.model.SettingRepository;


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
public class SettingPresenter extends BasePresenter<SettingRepository> {
    private RxErrorHandler mErrorHandler;

    public SettingPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(SettingRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}