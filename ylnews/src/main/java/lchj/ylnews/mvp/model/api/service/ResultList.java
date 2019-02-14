package lchj.ylnews.mvp.model.api.service;

import java.util.List;

import lchj.ylnews.mvp.model.entity.NewsBean;

public class ResultList<T> {

    private Integer total;
    private Integer page_num;
    private List<T> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage_num() {
        return page_num;
    }

    public void setPage_num(Integer page_num) {
        this.page_num = page_num;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
