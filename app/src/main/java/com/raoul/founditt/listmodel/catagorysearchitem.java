package com.raoul.founditt.listmodel;

/**
 * Created by mobile_perfect on 14/12/14.
 */
public class catagorysearchitem implements Comparable<catagorysearchitem> {

    private long id;
    private String catagory;
    private String contente;


    public catagorysearchitem(long id, String catagory) {
        super();
        this.id = id;
        this.catagory=catagory;
        this.contente=contente;


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory= catagory;
    }



    public int compareTo(catagorysearchitem other) {
        return (int) (id - other.getId());
    }
}
