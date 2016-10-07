package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by germangarcia on 21/05/16.
 */
public class ListaGrupos {

    @SerializedName("grupo_sims")
    private ListaGrupoSims array_grupo_sims;

    @SerializedName("grupo_combos")
    private ListaGrupoCombos array_grupo_combos;


    public ListaGrupoSims getGrupoSims() {
        return array_grupo_sims;
    }

    public void setGrupoSims(ListaGrupoSims array_grupo_sims) {
        this.array_grupo_sims= array_grupo_sims;
    }
    public ListaGrupoCombos getGrupoCombos() {
        return array_grupo_combos;
    }

    public void setGrupoCombos(ListaGrupoCombos array_grupo_combos) {
        this.array_grupo_combos = array_grupo_combos;
    }


}
