package lchj.ylnews.mvp.model.entity;

import java.io.Serializable;

public class NewsDetailsBean implements Serializable {
    private Integer id;
    private Integer catid;
    private String image;
    private String title;
    private Integer read_count;
    private Integer status;
    private Integer is_position;
    private String update_time;
    private String create_time;
    private String catname;//栏目
    private String content;
    private String small_title;
    private String is_head_figure;
    private String is_allowcomments;
    private String source_type;
    private String description;
    private String upvote_count;
    private String commont_count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCatid() {
        return catid;
    }

    public void setCatid(Integer catid) {
        this.catid = catid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRead_count() {
        return read_count;
    }

    public void setRead_count(Integer read_count) {
        this.read_count = read_count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIs_position() {
        return is_position;
    }

    public void setIs_position(Integer is_position) {
        this.is_position = is_position;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSmall_title() {
        return small_title;
    }

    public void setSmall_title(String small_title) {
        this.small_title = small_title;
    }

    public String getIs_head_figure() {
        return is_head_figure;
    }

    public void setIs_head_figure(String is_head_figure) {
        this.is_head_figure = is_head_figure;
    }

    public String getIs_allowcomments() {
        return is_allowcomments;
    }

    public void setIs_allowcomments(String is_allowcomments) {
        this.is_allowcomments = is_allowcomments;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpvote_count() {
        return upvote_count;
    }

    public void setUpvote_count(String upvote_count) {
        this.upvote_count = upvote_count;
    }

    public String getCommont_count() {
        return commont_count;
    }

    public void setCommont_count(String commont_count) {
        this.commont_count = commont_count;
    }
}
