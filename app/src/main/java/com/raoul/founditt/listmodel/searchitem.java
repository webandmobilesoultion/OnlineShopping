package com.raoul.founditt.listmodel;

/**
 * Created by mobile_perfect on 14/12/14.
 */
public class searchitem implements Comparable<searchitem> {

private long id;
private String catagory;
private String contente;


public searchitem(long id, String catagory ,String contente) {
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

    public String getContente() {
        return contente;
    }

    public void setContente(String contente) {
        this.contente= contente;
    }

public int compareTo(searchitem other) {
        return (int) (id - other.getId());
        }
        }
