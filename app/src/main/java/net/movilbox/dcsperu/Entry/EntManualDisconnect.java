package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by germangarcia on 28/06/16.
 */
public class EntManualDisconnect {


    @SerializedName("id")
    private int id;

    @SerializedName("idUser")
    private int idUser;

    @SerializedName("idDistri")
    private int idDistri;

    @SerializedName("type")
    private int type;

    @SerializedName("date")
    private int date;

    public EntManualDisconnect() {
    }

    @SerializedName("network_type")
    private int network_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdDistri() {
        return idDistri;
    }

    public void setIdDistri(int idDistri) {
        this.idDistri = idDistri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(int network_type) {
        this.network_type = network_type;
    }
}
