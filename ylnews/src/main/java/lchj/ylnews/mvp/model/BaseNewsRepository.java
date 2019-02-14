package lchj.ylnews.mvp.model;

import java.util.List;

import io.reactivex.Observable;
import lchj.ylnews.mvp.model.api.service.CommonService;
import lchj.ylnews.mvp.model.api.service.ResultIndex;
import lchj.ylnews.mvp.model.api.service.ResultInfo;
import lchj.ylnews.mvp.model.api.service.ResultList;
import lchj.ylnews.mvp.model.entity.CatBean;
import lchj.ylnews.mvp.model.entity.NewsBean;
import lchj.ylnews.mvp.model.entity.NewsDetailsBean;
import me.jessyan.art.mvp.IModel;
import me.jessyan.art.mvp.IRepositoryManager;

/**
 * ================================================
 * 必须实现 IModel
 * 可以根据不同的业务逻辑划分多个 Repository 类, 多个业务逻辑相近的页面可以使用同一个 Repository 类
 * 无需每个页面都创建一个独立的 Repository
 * 通过 {@link me.jessyan.art.mvp.IRepositoryManager#createRepository(java.lang.Class)} 获得的 Repository 实例, 为单例对象
 * <p>
 * Created by MVPArtTemplate on 01/30/2019 08:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class BaseNewsRepository implements IModel {
    private IRepositoryManager mManager;

    public BaseNewsRepository(IRepositoryManager manager) {
        this.mManager = manager;
    }

    public Observable<ResultInfo<ResultList<NewsBean>>> getNewlist(int catid,int page,int size) {
        return mManager.createRetrofitService(CommonService.class)
                .getNewlist(catid,page,size);
    }
    @Override
    public void onDestroy() {

    }
}
