package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by germangarcia on 21/05/16.
 */
public class ListaGrupoCombos {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre_grupo_combos")
    private String  nombre_grupo_combos;

    @SerializedName("cant_ventas_grupo_combos")
    private int cant_ventas_grupo_combos;

    @SerializedName("cant_cumplimiento_grupo_combos")
    private int cant_cumplimiento_grupo_combos;

    public String getNombre_grupo_combos() {
        return nombre_grupo_combos;
    }

    public void setNombre_grupo_combos(String nombre_grupo_combos) {
        this.nombre_grupo_combos= nombre_grupo_combos;
    }
    public int getCant_ventas_grupo_combos() {
        return cant_ventas_grupo_combos;
    }

    public void setCant_ventas_grupo_combos(int cant_ventas_grupo_combo) {
        this.cant_ventas_grupo_combos = cant_ventas_grupo_combos;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCant_cumplimiento_grupo_combos() {
        return cant_cumplimiento_grupo_combos;
    }

    public void setCant_cumplimiento_grupo_combos(int cant_cumplimiento_combos) {
        this.cant_cumplimiento_grupo_combos = cant_cumplimiento_combos;
    }

}
