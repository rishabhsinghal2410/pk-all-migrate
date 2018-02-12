package com.review.model;

import com.google.common.base.Strings;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class User {

    @Id
    Integer id;
    String username;
    String firstName;
    String lastName;
    String contactNo;
    String password;
    String emailId;
    Integer userType;
    Integer state;
    Date dateCreated;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        if (contactNo.startsWith("+91") && contactNo.length() == 13)
            return contactNo;
        else if (contactNo.length() == 10)
            return "+91" + contactNo;

        return null;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDisplayName() {

        if (firstName == null)
            return "";

        if (lastName == null)
            return firstName.trim();

        return (this.firstName.trim() + " " + this.lastName.trim()).trim();
    }

    public boolean isValid() {
        if (Strings.isNullOrEmpty(this.firstName))
            return false;

        if (Strings.isNullOrEmpty(this.emailId))
            return false;

        return !Strings.isNullOrEmpty(this.password) || this.password.length() >= 6;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", password='" + password + '\'' +
                ", emailId='" + emailId + '\'' +
                ", userType=" + userType +
                ", state=" + state +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
