package com.review.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "review_pic")
public class PostPic {
    @Id
    int id;
    int reviewId;
    String picUrl;
    int coverPic;
    Date uploadedDate;
    int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(int coverPic) {
        this.coverPic = coverPic;
    }

    public Date getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "PostPic{" +
                "id=" + id +
                ", reviewId=" + reviewId +
                ", picUrl='" + picUrl + '\'' +
                ", coverPic=" + coverPic +
                ", uploadedDate=" + uploadedDate +
                ", state=" + state +
                '}';
    }
}
