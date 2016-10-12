package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by germangarcia on 21/05/16.
 */
public class GrupoCombos {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre_grupo")
    private String  nombre_grupo;

    @SerializedName("cant_grupo_vendedor")
    private int cant_grupo_vendedor;

    @SerializedName("cant_grupo_distri")
    private int cant_grupo_distri;

    public int getId(){ return id;}


    public String getNombre_grupo() {
        return nombre_grupo;
    }

    public void setNombre_grupo(String nombre_grupo) {
        this.nombre_grupo= nombre_grupo;
    }

    public int getCant_grupo_vendedor() {
        return cant_grupo_vendedor;
    }

    public void setCant_grupo_vendedor(int cant_grupo_vendedor) {
        this.cant_grupo_vendedor = cant_grupo_vendedor;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCant_grupo_distri() {
        return cant_grupo_distri;
    }

    public void setCant_grupo_distri(int cant_grupo_distri) {
        this.cant_grupo_distri = cant_grupo_distri;
    }

}
