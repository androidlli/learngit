package me.fangx.zhihu.presenter;

import android.content.Context;
import android.view.View;

import java.util.List;

import me.fangx.zhihu.modle.bean.ArticleListBean;
import me.fangx.zhihu.modle.entity.ArticleListEntity;
import me.fangx.zhihu.modle.http.ExploreListService;
import me.fangx.zhihu.modle.http.utils.RetrofitUtils;
import me.fangx.zhihu.utils.BaseUtil;
import me.fangx.zhihu.view.ExploreListView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fangxiao on 16/1/28.
 */
public class ExplorePresenter extends BasePresenter<ExploreListView> {

    private static final String tag = "growthhacker";

    public static final int LIMIT = 10;

    private ExploreListService exploreListService;
    private Subscription mSubscription;

    public ExplorePresenter(Context context) {
        exploreListService = RetrofitUtils.createApi(context, ExploreListService.class);
    }

    @Override
    public void attachView(ExploreListView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }


    public void loadList(final int page) {
        checkViewAttached();

        String date = BaseUtil.getNYYMMDD(1 - page);

        int offset = (page - 1) * LIMIT;

        mSubscription = exploreListService.getHomeArticleList(tag, LIMIT, offset)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<ArticleListBean>, List<ArticleListBean>>() {
                    @Override
                    public List<ArticleListBean> call(List<ArticleListBean> articleListBeanList) {
                        for (ArticleListBean articleListBean : articleListBeanList) {
                            articleListBean.setSummary(BaseUtil.delHTMLTag(articleListBean.getSummary()));
                            articleListBean.setContent(BaseUtil.delHTMLTag(articleListBean.getContent()));
                        }
                        return articleListBeanList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ArticleListBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e.getMessage(), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }

                    @Override
                    public void onNext(List<ArticleListBean> articleListBeanList) {

                        if (page == 1) {
                            getMvpView().refresh(articleListBeanList);
                        } else {
                            getMvpView().loadMore(articleListBeanList);
                        }
                    }
                });

    }


    //数据缓存
    private void saveDataToDBorSP(ArticleListEntity articleListEntity) {

    }


}