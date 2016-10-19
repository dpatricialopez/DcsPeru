package net.movilbox.dcsperu.Entry;

/**
 * Created by jhonjimenez on 18/10/16.
 */

public class DetalleAsignacionProductos {

    private int cantidad;
    private int cantidad_carga;
    private String referencia;
    private int bodega;
    private int tipo_refe;
    private String nom_bod;
    private String tipo_producto;

    public String getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(String tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad_carga() {
        return cantidad_carga;
    }

    public void setCantidad_carga(int cantidad_carga) {
        this.cantidad_carga = cantidad_carga;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getBodega() {
        return bodega;
    }

    public void setBodega(int bodega) {
        this.bodega = bodega;
    }

    public int getTipo_refe() {
        return tipo_refe;
    }

    public void setTipo_refe(int tipo_refe) {
        this.tipo_refe = tipo_refe;
    }

    public String getNom_bod() {
        return nom_bod;
    }

    public void setNom_bod(String nom_bod) {
        this.nom_bod = nom_bod;
    }
}
