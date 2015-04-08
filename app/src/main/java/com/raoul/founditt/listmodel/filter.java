package com.raoul.founditt.listmodel;

/**
 * Created by mobile_perfect on 14/12/14.
 */
public class filter implements Comparable<filter> {

    private long id;
    private String filtername;

    public filter(long id, String filtername) {
        super();
        this.id = id;
        this.filtername = filtername;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }




    public int compareTo(filter other) {
        return (int) (id - other.getId());
    }
}
