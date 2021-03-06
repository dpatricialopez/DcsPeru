package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by germangarcia on 5/05/16.
 */
public class TipoInterior extends CategoriasEstandar {

    @SerializedName("siglas")
    private String siglas;

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public TipoInterior(int id, String descripcion) {
        super(id, descripcion);
    }

}
