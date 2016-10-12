package net.movilbox.dcsperu.Entry;

/**
 * Created by dianalopez on 10/10/16.
 */

public class EntNoticia {
    private int id;
    private String titulo;
    private String contenido;
    private String url_image;


    private int estado;

    public EntNoticia(int id, String titulo, String contenido, String url_image, int estado){
            this.id=id;
            this.titulo=titulo;
            this.contenido=contenido;
            this.url_image=url_image;
            this.estado=estado;

    }

    public EntNoticia() {

    }


    public int getId(){
        return id;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getContenido(){
        return contenido;
    }

    public int getEstado(){
        return estado;
    }
    public String getUrl_image(){
        return url_image;
    }

    public void setId(int id){
        this.id=id;
    }
    public void setTitulo(String titulo){
        this.titulo=titulo;
    }
    public void setContenido(String contenido){
        this.contenido=contenido;
    }

    public void setEstado(int estado){
        this.estado=estado;
    }
    public void setUrl_image(String url_image){
        this.url_image=url_image;
    }



}
