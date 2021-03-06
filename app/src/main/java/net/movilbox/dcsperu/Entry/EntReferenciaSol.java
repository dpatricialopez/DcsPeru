package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jhonjimenez on 10/10/16.
 */

public class EntReferenciaSol {
    @SerializedName("id_bodega")
    private int id_bodega;

    @SerializedName("id_referencia")
    private int id_referencia;

    @SerializedName("producto")
    private String producto;

    @SerializedName("total")
    private int total;

    @SerializedName("totalCupo")
    private int totalCupo;

    @SerializedName("tipo_bodega")
    private int tipo_bodega_re;

    @SerializedName("precio_pdv")
    private double precio_pdv;

    @SerializedName("precio_publico")
    private double precio_publico;

    @SerializedName("tipo_ref")
    private int tipo_ref;

    private int cantidadSol;

    public int getCantidadSol() {
        return cantidadSol;
    }

    public void setCantidadSol(int cantidadSol) {
        this.cantidadSol = cantidadSol;
    }

    public int getId_bodega() {
        return id_bodega;
    }

    public void setId_bodega(int id_bodega) {
        this.id_bodega = id_bodega;
    }

    public int getId_referencia() {
        return id_referencia;
    }

    public void setId_referencia(int id_referencia) {
        this.id_referencia = id_referencia;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTipo_bodega() {
        return tipo_bodega_re;
    }

    public void setTipo_bodega(int tipo_bodega) {
        this.tipo_bodega_re = tipo_bodega;
    }

    public int getTipo_ref() {
        return tipo_ref;
    }

    public void setTipo_ref(int tipo_ref) {
        this.tipo_ref = tipo_ref;
    }

    public double getPrecio_pdv() {
        return precio_pdv;
    }

    public void setPrecio_pdv(double precio_pdv) {
        this.precio_pdv = precio_pdv;
    }

    public double getPrecio_publico() {
        return precio_publico;
    }

    public void setPrecio_publico(double precio_publico) {
        this.precio_publico = precio_publico;
    }

    public int getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(int totalCupo) {
        this.totalCupo = totalCupo;
    }
}
