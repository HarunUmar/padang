package com.hitesh_sahu.retailapp.model.entities;

/**
 * Created by Kuncoro on 29/02/2016.
 */
public class HistoryData {
    private String id, tgl ,status;

    public HistoryData() {
    }

    public HistoryData(String id, String tgl, String status) {
        this.id = id;
        this.tgl= tgl;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl =tgl;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status =status;
    }



}
