package com.review.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "review_content")
public class Post {

    @Id
    String id;
    @Column(name = "review_header")
    String postHeader;
    @Column(name = "review_content")
    String postContent;
    String landingUrl;
    String coverPic;
    String userCreated;
    Date dateUploaded;
    String parentSite;
    String facebookId;
    Integer state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostHeader() {
        return postHeader;
    }

    public void setPostHeader(String postHeader) {
        this.postHeader = postHeader;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getLandingUrl() {
        return landingUrl;
    }

    public void setLandingUrl(String landingUrl) {
        this.landingUrl = landingUrl;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getParentSite() {
        return parentSite;
    }

    public void setParentSite(String parentSite) {
        this.parentSite = parentSite;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", postHeader='" + postHeader + '\'' +
                ", postContent='" + postContent + '\'' +
                ", landingUrl='" + landingUrl + '\'' +
                ", coverPic='" + coverPic + '\'' +
                ", userCreated='" + userCreated + '\'' +
                ", dateUploaded=" + dateUploaded +
                ", parentSite='" + parentSite + '\'' +
                ", facebookId='" + facebookId + '\'' +
                ", state=" + state +
                '}';
    }
}
