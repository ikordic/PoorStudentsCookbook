package com.kordic.ivan.poorstudentscookbook.Model;

public class User
{

    private String userId;
    private String userName;
    private String userSurname;
    private String userEmail;
    private String userUsername;
    private String userProfileImage;

    public User()
    {
    }

    public User(String userId, String userName, String userSurname, String userEmail, String userUsername, String userProfileImage)
    {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userUsername = userUsername;
        this.userProfileImage = userProfileImage;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserSurname()
    {
        return userSurname;
    }

    public void setUserSurname(String userSurname)
    {
        this.userSurname = userSurname;
    }

    public String getUserEmail()
    {
        return userEmail;
    }

    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

    public String getUserUsername()
    {
        return userUsername;
    }

    public void setUserUsername(String userUsername)
    {
        this.userUsername = userUsername;
    }

    public String getUserProfileImage()
    {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage)
    {
        this.userProfileImage = userProfileImage;
    }
}