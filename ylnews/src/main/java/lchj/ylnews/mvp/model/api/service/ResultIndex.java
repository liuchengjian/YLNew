package lchj.ylnews.mvp.model.api.service;

import java.util.List;

import lchj.ylnews.mvp.model.entity.NewsBean;

/**
 * 首页需要的实体类
 */
public class ResultIndex {

    private String total;
    private String page_num;
    private List<NewsBean> heads;//头图
    private List<NewsBean> positions;//可评论


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public List<NewsBean> getHeads() {
        return heads;
    }

    public void setHeads(List<NewsBean> heads) {
        this.heads = heads;
    }

    public List<NewsBean> getPositions() {
        return positions;
    }

    public void setPositions(List<NewsBean> positions) {
        this.positions = positions;
    }
}
