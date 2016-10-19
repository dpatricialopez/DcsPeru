package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jhonjimenez on 19/10/16.
 */

public class AceptarAsignacionProductosContenedor {
    @SerializedName("pedidos")
    private List<AceptarAsignacionProductos> pedidos;

    public List<AceptarAsignacionProductos> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<AceptarAsignacionProductos> pedidos) {
        this.pedidos = pedidos;
    }
}
