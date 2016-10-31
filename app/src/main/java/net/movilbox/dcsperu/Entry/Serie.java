package net.movilbox.dcsperu.Entry;

import java.io.Serializable;

/**
 * Created by jhonjimenez on 27/10/16.
 */

public class Serie implements Serializable{

    private String serie;
    private int id_producto;
    private int check;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }
}
