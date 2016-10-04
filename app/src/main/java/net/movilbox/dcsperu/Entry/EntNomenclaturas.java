package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

/**
 * Created by germangarcia on 24/06/16.
 */
public class EntNomenclaturas {

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("letras")
    private String letras;

    @SerializedName("tipo_nom")
    private int tipo_nom;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLetras() {
        return letras;
    }

    public void setLetras(String letras) {
        this.letras = letras;
    }

    public int getTipo_nom() {
        return tipo_nom;
    }

    public void setTipo_nom(int tipo_nom) {
        this.tipo_nom = tipo_nom;
    }
}
