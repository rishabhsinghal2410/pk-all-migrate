package com.review.model;

public class InvalidUser {
    public User user;
    public Exception e;

    public InvalidUser(User user, Exception e) {
        this.user = user;
        this.e = e;
    }

    @Override
    public String toString() {
        return "InvalidUser{" +
                "user=" + user +
                ", e=" + e +
                '}';
    }
}
