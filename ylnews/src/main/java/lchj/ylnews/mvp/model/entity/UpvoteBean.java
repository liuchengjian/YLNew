package lchj.ylnews.mvp.model.entity;

import java.io.Serializable;

public class UpvoteBean implements Serializable {

    private int isUpvote;

    public int getIsUpvote() {
        return isUpvote;
    }

    public void setIsUpvote(int isUpvote) {
        this.isUpvote = isUpvote;
    }
}
