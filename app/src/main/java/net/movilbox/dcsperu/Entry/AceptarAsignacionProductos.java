package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jhonjimenez on 18/10/16.
 */

public class AceptarAsignacionProductos {

    @SerializedName("id_pedido")
    private int id_pedido;

    @SerializedName("total_cantidad")
    private int total_cantidad;

    @SerializedName("total_cantidad_carga")
    private int total_cantidad_carga;

    @SerializedName("items")
    private List<DetalleAsignacionProductos> detalleAsignacionProductos;

    private String comentarioRechazo;

    public String marcaProducto = "";

    public Boolean indicadorChekend = false;

    public Boolean indicadorChekendDevolver = false;

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public Boolean getIndicadorChekend() {
        return indicadorChekend;
    }

    public void setIndicadorChekend(Boolean indicadorChekend) {
        this.indicadorChekend = indicadorChekend;
    }

    public Boolean getIndicadorChekendDevolver() {
        return indicadorChekendDevolver;
    }

    public void setIndicadorChekendDevolver(Boolean indicadorChekendDevolver) {
        this.indicadorChekendDevolver = indicadorChekendDevolver;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public List<DetalleAsignacionProductos> getDetalleAsignacionProductos() {
        return detalleAsignacionProductos;
    }

    public void setDetalleAsignacionProductos(List<DetalleAsignacionProductos> detalleAsignacionProductos) {
        this.detalleAsignacionProductos = detalleAsignacionProductos;
    }

    public int getTotal_cantidad() {
        return total_cantidad;
    }

    public void setTotal_cantidad(int total_cantidad) {
        this.total_cantidad = total_cantidad;
    }

    public int getTotal_cantidad_carga() {
        return total_cantidad_carga;
    }

    public void setTotal_cantidad_carga(int total_cantidad_carga) {
        this.total_cantidad_carga = total_cantidad_carga;
    }
    public String getComentarioRechazo() {
        return comentarioRechazo;
    }

    public void setComentarioRechazo(String comentarioRechazo) {
        this.comentarioRechazo = comentarioRechazo;
    }
}
