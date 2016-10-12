package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by germangarcia on 21/05/16.
 */
public class ListaGrupos {

    @SerializedName("cant_combo_grupo")
    private List<GrupoCombos> cant_combo_grupo;

    @SerializedName("cant_sim_grupo")
    private List<GrupoSims> cant_sim_grupo;

    @SerializedName("indicadores_puntos")
    private List<EntPuntoIndicado> entPuntoIndicadoList;


    public List<EntPuntoIndicado> getEntPuntoIndicadoList() {
        return entPuntoIndicadoList;
    }

    public void setEntPuntoIndicadoList(List<EntPuntoIndicado> entPuntoIndicadoList) {
        this.entPuntoIndicadoList = entPuntoIndicadoList;
    }

    public List<GrupoCombos> getGrupoCombos() {
        return cant_combo_grupo;
    }

    public void setGrupoSims(List<GrupoSims> cant_sim_grupo) {
        this.cant_sim_grupo= cant_sim_grupo;
    }
    public List<GrupoSims> getGrupoSims() {
        return cant_sim_grupo;
    }

    public void setGrupoCombos(List<GrupoCombos> cant_combo_grupo) {
        this.cant_combo_grupo = cant_combo_grupo;
    }


}
