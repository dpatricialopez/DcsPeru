package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by germangarcia on 28/06/16.
 */
public class EntSincronizarRepartidor {

    @SerializedName("datos_noticias")
    private List<EntNoticia_Repartidor> entNoticia_repartidorList;

    @SerializedName("datos_pedidos")
    private ListSincronizarRepartidor listSincronizarRepartidor;

    public List<EntNoticia_Repartidor> getEntNoticia_repartidorList() {
        return entNoticia_repartidorList;
    }

    public void setEntNoticia_repartidorList(List<EntNoticia_Repartidor> entNoticia_repartidorList) {
        this.entNoticia_repartidorList = entNoticia_repartidorList;
    }

    public ListSincronizarRepartidor getListSincronizarRepartidor() {
        return listSincronizarRepartidor;
    }

    public void setListSincronizarRepartidor(ListSincronizarRepartidor listSincronizarRepartidor) {
        this.listSincronizarRepartidor = listSincronizarRepartidor;
    }
}
