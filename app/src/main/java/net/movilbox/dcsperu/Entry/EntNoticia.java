package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dianalopez on 10/10/16.
 */

public class EntNoticia {


    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("contenido")
    private String contenido;

    @SerializedName("url")
    private String url;

    @SerializedName("fecha")
    private String date;

    @SerializedName("url_image")
    private String url_image;

    @SerializedName("file_name")
    private String file_name;

    @SerializedName("file_url")
    private String file_url;

    @SerializedName("estado")
    private int estado;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("fecha_lectura")
    private String fecha_lectura;

    @SerializedName("sincronizado")
    private int sincronizado;

    @SerializedName("vigencia")
    private int vigencia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContain() {
        return contenido;
    }

    public void setContain(String contain) {
        this.contenido = contain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return url_image;
    }

    public void setImage(String image) {
        this.url_image = image;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public int getStatus() {
        return estado;
    }

    public void setStatus(int status) {
        this.estado = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha_lectura() {
        return fecha_lectura;
    }

    public void setFecha_lectura(String fecha_lectura) {
        this.fecha_lectura = fecha_lectura;
    }

    public int getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        this.sincronizado = sincronizado;
    }

    public int getVigencia() {
        return vigencia;
    }

    public void setVigencia(int vigencia) {
        this.vigencia = vigencia;
    }
}




