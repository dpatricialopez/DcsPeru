package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by germangarcia on 21/05/16.
 */
public class ListaGrupoSims {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre_grupo_sims")
    private String  nombre_grupo_sims;

    @SerializedName("cant_ventas_grupo_sims")
    private int cant_ventas_grupo_sims;

    @SerializedName("cant_cumplimiento_grupo_sims")
    private int cant_cumplimiento_grupo_sims;

    public String getNombre_grupo_sims() {
        return nombre_grupo_sims;
    }

    public void setNombre_grupo_sims(String nombre_grupo_sims) {
        this.nombre_grupo_sims= nombre_grupo_sims;
    }
    public int getCant_ventas_grupo_sims() {
        return cant_ventas_grupo_sims;
    }

    public void setCant_ventas_grupo_sims(int cant_ventas_grupo_sims) {
        this.cant_ventas_grupo_sims = cant_ventas_grupo_sims;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCant_cumplimiento_grupo_sims() {
        return cant_cumplimiento_grupo_sims;
    }

    public void setCant_cumplimiento_grupo_sims(int cant_cumplimiento_sims) {
        this.cant_cumplimiento_grupo_sims = cant_cumplimiento_sims;
    }



}
