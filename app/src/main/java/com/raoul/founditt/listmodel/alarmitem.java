package com.raoul.founditt.listmodel;

/**
 * Created by mobile_perfect on 15/12/14.
 */
public class alarmitem {
    private String ID;
    private String username;
    private String userimageurl;
    private String comment;
    private String productimageurl;
    private String time;


    public String getUsername() {
        return username;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setUsername(String username) {
        this.username= username;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment=comment;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setProductimageurl(String productimageurl) {
        this.productimageurl = productimageurl;
    }

    public void setUserimageurl(String userimageurl) {
        this.userimageurl = userimageurl;
    }

    public String getProductimageurl() {
        return productimageurl;
    }

    public String getUserimageurl() {
        return userimageurl;
    }
}


