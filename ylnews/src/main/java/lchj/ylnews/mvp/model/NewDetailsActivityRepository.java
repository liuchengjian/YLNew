package lchj.ylnews.mvp.model;

import io.reactivex.Observable;
import lchj.ylnews.mvp.model.api.service.CommonService;
import lchj.ylnews.mvp.model.api.service.ResultIndex;
import lchj.ylnews.mvp.model.api.service.ResultInfo;
import lchj.ylnews.mvp.model.api.service.ResultList;
import lchj.ylnews.mvp.model.entity.NewsDetailsBean;
import lchj.ylnews.mvp.model.entity.UpvoteBean;
import lchj.ylnews.mvp.model.entity.UpvoteBody;
import me.jessyan.art.mvp.IModel;
import me.jessyan.art.mvp.IRepositoryManager;

/**
 * ================================================
 * 必须实现 IModel
 * 可以根据不同的业务逻辑划分多个 Repository 类, 多个业务逻辑相近的页面可以使用同一个 Repository 类
 * 无需每个页面都创建一个独立的 Repository
 * 通过 {@link me.jessyan.art.mvp.IRepositoryManager#createRepository(java.lang.Class)} 获得的 Repository 实例, 为单例对象
 * <p>
 * Created by MVPArtTemplate on 01/29/2019 16:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArt">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class NewDetailsActivityRepository implements IModel {
    private IRepositoryManager mManager;

    public NewDetailsActivityRepository(IRepositoryManager manager) {
        this.mManager = manager;
    }

    public Observable<ResultInfo<NewsDetailsBean>> getNewDetails(int id) {
        return mManager.createRetrofitService(CommonService.class)
                .getNewDetails(id);
    }
    public Observable<ResultInfo<UpvoteBean>> isUpvote(int id) {
        return mManager.createRetrofitService(CommonService.class)
                .isUpvote(id);
    }
    public Observable<ResultInfo> toUpvote(UpvoteBody upvoteBody) {
        return mManager.createRetrofitService(CommonService.class)
                .toUpvote(upvoteBody);
    }

    public Observable<ResultInfo> toUpvoteNo(UpvoteBody upvoteBody) {
        return mManager.createRetrofitService(CommonService.class)
                .toUpvoteNo(upvoteBody);
    }

    @Override
    public void onDestroy() {

    }
}
