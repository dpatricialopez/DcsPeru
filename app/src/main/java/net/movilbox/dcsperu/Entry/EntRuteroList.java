package net.movilbox.dcsperu.Entry;

import java.io.Serializable;

/**
 * Created by germangarcia on 28/06/16.
 */
public class EntRuteroList implements Serializable{

    private int idpos;

    private String nombre_punto;

    private String texto_direccion;

    private int stock;

    private String dias_inve;

    private int quiebre;

    private String codigo_cum;

    private int estado_visita;

    private String nom_distrito;

    private String detalle;

    private int stock_sim;

    private int stock_combo;

    private int dias_inve_sim;

    private int dias_inve_combo;

    private String ultima_visita;

    private int tipo_visita;

    private String telefono;

    private double latitud;

    private double longitud;

    private int stock_seguridad_combo;

    private int stock_seguridad_sim;

    public int getStock_seguridad_combo() {
        return stock_seguridad_combo;
    }

    public void setStock_seguridad_combo(int stock_seguridad_combo) {
        this.stock_seguridad_combo = stock_seguridad_combo;
    }

    public int getStock_seguridad_sim() {
        return stock_seguridad_sim;
    }

    public void setStock_seguridad_sim(int stock_seguridad_sim) {
        this.stock_seguridad_sim = stock_seguridad_sim;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getTipo_visita() {
        return tipo_visita;
    }

    public void setTipo_visita(int tipo_visita) {
        this.tipo_visita = tipo_visita;
    }

    public String getUltima_visita() {
        return ultima_visita;
    }

    public void setUltima_visita(String ultima_visita) {
        this.ultima_visita = ultima_visita;
    }

    public int getIdpos() {
        return idpos;
    }

    public void setIdpos(int idpos) {
        this.idpos = idpos;
    }

    public String getNombre_punto() {
        return nombre_punto;
    }

    public void setNombre_punto(String nombre_punto) {
        this.nombre_punto = nombre_punto;
    }

    public String getTexto_direccion() {
        return texto_direccion;
    }

    public void setTexto_direccion(String texto_direccion) {
        this.texto_direccion = texto_direccion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDias_inve() {
        return dias_inve;
    }

    public void setDias_inve(String dias_inve) {
        this.dias_inve = dias_inve;
    }

    public int getQuiebre() {
        return quiebre;
    }

    public void setQuiebre(int quiebre) {
        this.quiebre = quiebre;
    }

    public String getCodigo_cum() {
        return codigo_cum;
    }

    public void setCodigo_cum(String codigo_cum) {
        this.codigo_cum = codigo_cum;
    }

    public int getEstado_visita() {
        return estado_visita;
    }

    public void setEstado_visita(int estado_visita) {
        this.estado_visita = estado_visita;
    }

    public String getNom_distrito() {
        return nom_distrito;
    }

    public void setNom_distrito(String nom_distrito) {
        this.nom_distrito = nom_distrito;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public int getStock_sim() {
        return stock_sim;
    }

    public void setStock_sim(int stock_sim) {
        this.stock_sim = stock_sim;
    }

    public int getStock_combo() {
        return stock_combo;
    }

    public void setStock_combo(int stock_combo) {
        this.stock_combo = stock_combo;
    }

    public int getDias_inve_sim() {
        return dias_inve_sim;
    }

    public void setDias_inve_sim(int dias_inve_sim) {
        this.dias_inve_sim = dias_inve_sim;
    }

    public int getDias_inve_combo() {
        return dias_inve_combo;
    }

    public void setDias_inve_combo(int dias_inve_combo) {
        this.dias_inve_combo = dias_inve_combo;
    }

}
