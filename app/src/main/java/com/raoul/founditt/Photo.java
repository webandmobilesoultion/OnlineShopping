package com.raoul.founditt;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about a given Photo 
 */

@ParseClassName("Photo")
public class Photo extends ParseObject {

	public Photo() {
		// A default constructor is required.
	}

	public ParseFile getImage() {
		return getParseFile("image");
	}
    public String  getID(){
        return getObjectId();
    }
    public void setID(String id)
    {
        put("objecId",id);
    }
	public void setImage(ParseFile file) {
		put("image", file);
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser user) {
		put("user", user);
	}

	public String getRetail(){
        return getString("retail");
    }
    public void setRetail(String retail){
        put("retail",retail);
    }

    public String getLike(){
        return getString("like");
    }
    public void setLike(String like){
        put("like",like);
    }

    public String getTagline(){
        return getString("tagline");
    }
    public void setTagline(String tagline){
        put("tagline",tagline);
    }

    public String getLocation(){
        return getString("location");
    }
    public void setLocation(String location){
        put("location",location);
    }

    public String getExpiry(){
        return getString("expiry");
    }
    public void setExpiry(Date expiry){
        put("expiry",expiry);
    }
    public String getCategory(){
        return getString("category");
    }
    public void setCategory(String category){
        put("category",category);
    }

}
