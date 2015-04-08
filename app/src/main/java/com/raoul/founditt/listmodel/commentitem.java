package com.raoul.founditt.listmodel;

import com.parse.ParseFile;

/**
 * Created by mobile_perfect on 14/12/14.
 */
public class commentitem  {


    private String username;
    private String iamgeurl;
    private String comment;
    private ParseFile image;
    private String diftime;

    public void setDiftime(String diftime) {
        this.diftime = diftime;
    }

    public String getDiftime() {
        return diftime;
    }

    public ParseFile getImage() {
        return image;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public String getusername() {
        return username;
    }
    public void setusername(String username) {
        this.username = username;
    }
    public String getIamgeurl() {
        return iamgeurl;
    }
    public void setIamgeurl(String iamgeurl) {
        this.iamgeurl = iamgeurl;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}

