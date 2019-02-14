package lchj.ylnews.mvp.model.api.service;

import java.util.List;

public class ResultInfo<T> {

    private String status;
    private String message;
    private List<T> list;
    private T data;

    public Boolean getSuccess() {
        if(status.equals("1")){
            //请求成功
            return true;
        }
        return false;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
