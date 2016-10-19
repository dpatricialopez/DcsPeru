package net.movilbox.dcsperu.Entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by germangarcia on 27/06/16.
 */
public class EntLisSincronizar extends EntEstandar {

    @SerializedName("id_territorio")
    public int id_territorio;

    @SerializedName("accion")
    private String accion;

    @SerializedName("categoria")
    public int categoria;

    @SerializedName("cedula")
    public String cedula;

    @SerializedName("celular")
    public String celular;

    @SerializedName("ciudad")
    public int ciudad;

    @SerializedName("cod_cum")
    public String codigo_cum;

    @SerializedName("depto")
    public int depto;

    @SerializedName("distrito")
    public int distrito;

    @SerializedName("email")
    public String email;

    @SerializedName("estado_com")
    public int estado_com;

    @SerializedName("idpos")
    private int idpos;

    @SerializedName("nombre_cliente")
    public String nombre_cliente;

    @SerializedName("nombre_punto")
    private String nombre_punto;

    @SerializedName("ref_direccion")
    public String ref_direccion;

    @SerializedName("subcategoria")
    public int subcategoria;

    @SerializedName("telefono")
    public String telefono;

    @SerializedName("territorio")
    public int territorio;

    @SerializedName("texto_direccion")
    public String texto_direccion;

    @SerializedName("tipo_documento")
    private int tipo_documento;

    @SerializedName("vende_recargas")
    public int vende_recargas;

    @SerializedName("zona")
    public int zona;

    @SerializedName("tipo_via")
    public int tipo_via;

    @SerializedName("nombre_via")
    public String nombre_via;

    @SerializedName("nro_via")
    public String nro_via;

    @SerializedName("nombre_mzn")
    public String nombre_mzn;

    @SerializedName("lote")
    public String lote;

    @SerializedName("tipo_vivienda")
    public int tipo_vivienda;

    @SerializedName("descripcion_vivienda")
    public String descripcion_vivienda;

    @SerializedName("tipo_interior")
    public int tipo_interior;

    @SerializedName("nro_interior")
    public String nro_interior;

    @SerializedName("tipo_urbanizacion")
    public int tipo_urbanizacion;

    @SerializedName("num_int_urbanizacion")
    public String num_int_urbanizacion;

    @SerializedName("tipo_ciudad")
    public int tipo_ciudad;

    @SerializedName("des_tipo_ciudad")
    public String des_tipo_ciudad;

    @SerializedName("latitud")
    private double latitud;

    @SerializedName("longitud")
    private double longitud;

    @SerializedName("estado_visita")
    private double estadoVisita;

    @SerializedName("pn")
    private String pn;

    @SerializedName("stock")
    private int stock;

    @SerializedName("producto")
    private String producto;

    @SerializedName("dias_inve")
    private double dias_inve;

    @SerializedName("ped_sugerido")
    private String ped_sugerido;

    @SerializedName("precio_referencia")
    private double precio_referencia;

    @SerializedName("precio_publico")
    private double precio_publico;

    @SerializedName("quiebre")
    private int quiebre;

    @SerializedName("precioventa")
    private double precioventa;

    @SerializedName("speech")
    private String speech;

    @SerializedName("pantalla")
    private String pantalla;

    @SerializedName("cam_frontal")
    private String cam_frontal;

    @SerializedName("cam_tras")
    private String cam_tras;

    @SerializedName("flash")
    private String flash;

    @SerializedName("banda")
    private String banda;

    @SerializedName("memoria")
    private String memoria;

    @SerializedName("expandible")
    private String expandible;

    @SerializedName("bateria")
    private String bateria;

    @SerializedName("bluetooth")
    private String bluetooth;

    @SerializedName("tactil")
    private String tactil;

    @SerializedName("tec_fisico")
    private String tec_fisico;

    @SerializedName("carrito_compras")
    private String carrito_compras;

    @SerializedName("correo")
    private String correo;

    @SerializedName("enrutador")
    private String enrutador;

    @SerializedName("radio")
    private String radio;

    @SerializedName("wifi")
    private String wifi;

    @SerializedName("gps")
    private String gps;

    @SerializedName("so")
    private String so;

    @SerializedName("web")
    private String web;

    @SerializedName("img")
    private String img;

    @SerializedName("id_referencia")
    private int id_referencia;

    @SerializedName("valor_referencia")
    private double valor_referencia;

    @SerializedName("valor_directo")
    private double valor_directo;

    @SerializedName("tipo_pro")
    private int tipo_pro;

    @SerializedName("detalle")
    private String detalle;

    @SerializedName("serie")
    private String serie;

    @SerializedName("referencias")
    private List<EntReferencia> referenciaLis;

    @SerializedName("paquete")
    private int paquete;

    @SerializedName("id_vendedor")
    private int id_vendedor;

    @SerializedName("distri")
    private int distri;

    @SerializedName("combo")
    private int combo;

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getPaquete() {
        return paquete;
    }

    public void setPaquete(int paquete) {
        this.paquete = paquete;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public int getDistri() {
        return distri;
    }

    public void setDistri(int distri) {
        this.distri = distri;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<EntReferencia> getReferenciaLis() {
        return referenciaLis;
    }

    public void setReferenciaLis(List<EntReferencia> referenciaLis) {
        this.referenciaLis = referenciaLis;
    }

    public int getId_referencia() {
        return id_referencia;
    }

    public void setId_referencia(int id_referencia) {
        this.id_referencia = id_referencia;
    }

    public double getValor_referencia() {
        return valor_referencia;
    }

    public void setValor_referencia(double valor_referencia) {
        this.valor_referencia = valor_referencia;
    }

    public double getValor_directo() {
        return valor_directo;
    }

    public void setValor_directo(double valor_directo) {
        this.valor_directo = valor_directo;
    }

    public int getTipo_pro() {
        return tipo_pro;
    }

    public void setTipo_pro(int tipo_pro) {
        this.tipo_pro = tipo_pro;
    }

    public double getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(double precioventa) {
        this.precioventa = precioventa;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getPantalla() {
        return pantalla;
    }

    public void setPantalla(String pantalla) {
        this.pantalla = pantalla;
    }

    public String getCam_frontal() {
        return cam_frontal;
    }

    public void setCam_frontal(String cam_frontal) {
        this.cam_frontal = cam_frontal;
    }

    public String getCam_tras() {
        return cam_tras;
    }

    public void setCam_tras(String cam_tras) {
        this.cam_tras = cam_tras;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getBanda() {
        return banda;
    }

    public void setBanda(String banda) {
        this.banda = banda;
    }

    public String getMemoria() {
        return memoria;
    }

    public void setMemoria(String memoria) {
        this.memoria = memoria;
    }

    public String getExpandible() {
        return expandible;
    }

    public void setExpandible(String expandible) {
        this.expandible = expandible;
    }

    public String getBateria() {
        return bateria;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public String getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(String bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getTactil() {
        return tactil;
    }

    public void setTactil(String tactil) {
        this.tactil = tactil;
    }

    public String getTec_fisico() {
        return tec_fisico;
    }

    public void setTec_fisico(String tec_fisico) {
        this.tec_fisico = tec_fisico;
    }

    public String getCarrito_compras() {
        return carrito_compras;
    }

    public void setCarrito_compras(String carrito_compras) {
        this.carrito_compras = carrito_compras;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEnrutador() {
        return enrutador;
    }

    public void setEnrutador(String enrutador) {
        this.enrutador = enrutador;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public double getDias_inve() {
        return dias_inve;
    }

    public void setDias_inve(double dias_inve) {
        this.dias_inve = dias_inve;
    }

    public String getPed_sugerido() {
        return ped_sugerido;
    }

    public void setPed_sugerido(String ped_sugerido) {
        this.ped_sugerido = ped_sugerido;
    }

    public double getPrecio_referencia() {
        return precio_referencia;
    }

    public void setPrecio_referencia(double precio_referencia) {
        this.precio_referencia = precio_referencia;
    }

    public double getPrecio_publico() {
        return precio_publico;
    }

    public void setPrecio_publico(double precio_publico) {
        this.precio_publico = precio_publico;
    }

    public int getQuiebre() {
        return quiebre;
    }

    public void setQuiebre(int quiebre) {
        this.quiebre = quiebre;
    }

    public int getId_territorio() {
        return id_territorio;
    }

    public void setId_territorio(int id_territorio) {
        this.id_territorio = id_territorio;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getCiudad() {
        return ciudad;
    }

    public void setCiudad(int ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigo_cum() {
        return codigo_cum;
    }

    public void setCodigo_cum(String codigo_cum) {
        this.codigo_cum = codigo_cum;
    }

    public int getDepto() {
        return depto;
    }

    public void setDepto(int depto) {
        this.depto = depto;
    }

    public int getDistrito() {
        return distrito;
    }

    public void setDistrito(int distrito) {
        this.distrito = distrito;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEstado_com() {
        return estado_com;
    }

    public void setEstado_com(int estado_com) {
        this.estado_com = estado_com;
    }

    public int getIdpos() {
        return idpos;
    }

    public void setIdpos(int idpos) {
        this.idpos = idpos;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNombre_punto() {
        return nombre_punto;
    }

    public void setNombre_punto(String nombre_punto) {
        this.nombre_punto = nombre_punto;
    }

    public String getRef_direccion() {
        return ref_direccion;
    }

    public void setRef_direccion(String ref_direccion) {
        this.ref_direccion = ref_direccion;
    }

    public int getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(int subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getTerritorio() {
        return territorio;
    }

    public void setTerritorio(int territorio) {
        this.territorio = territorio;
    }

    public String getTexto_direccion() {
        return texto_direccion;
    }

    public void setTexto_direccion(String texto_direccion) {
        this.texto_direccion = texto_direccion;
    }

    public int getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(int tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public int getVende_recargas() {
        return vende_recargas;
    }

    public void setVende_recargas(int vende_recargas) {
        this.vende_recargas = vende_recargas;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getTipo_via() {
        return tipo_via;
    }

    public void setTipo_via(int tipo_via) {
        this.tipo_via = tipo_via;
    }

    public String getNombre_via() {
        return nombre_via;
    }

    public void setNombre_via(String nombre_via) {
        this.nombre_via = nombre_via;
    }

    public String getNro_via() {
        return nro_via;
    }

    public void setNro_via(String nro_via) {
        this.nro_via = nro_via;
    }

    public String getNombre_mzn() {
        return nombre_mzn;
    }

    public void setNombre_mzn(String nombre_mzn) {
        this.nombre_mzn = nombre_mzn;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public int getTipo_vivienda() {
        return tipo_vivienda;
    }

    public void setTipo_vivienda(int tipo_vivienda) {
        this.tipo_vivienda = tipo_vivienda;
    }

    public String getDescripcion_vivienda() {
        return descripcion_vivienda;
    }

    public void setDescripcion_vivienda(String descripcion_vivienda) {
        this.descripcion_vivienda = descripcion_vivienda;
    }

    public int getTipo_interior() {
        return tipo_interior;
    }

    public void setTipo_interior(int tipo_interior) {
        this.tipo_interior = tipo_interior;
    }

    public String getNro_interior() {
        return nro_interior;
    }

    public void setNro_interior(String nro_interior) {
        this.nro_interior = nro_interior;
    }

    public int getTipo_urbanizacion() {
        return tipo_urbanizacion;
    }

    public void setTipo_urbanizacion(int tipo_urbanizacion) {
        this.tipo_urbanizacion = tipo_urbanizacion;
    }

    public String getNum_int_urbanizacion() {
        return num_int_urbanizacion;
    }

    public void setNum_int_urbanizacion(String num_int_urbanizacion) {
        this.num_int_urbanizacion = num_int_urbanizacion;
    }

    public int getTipo_ciudad() {
        return tipo_ciudad;
    }

    public void setTipo_ciudad(int tipo_ciudad) {
        this.tipo_ciudad = tipo_ciudad;
    }

    public String getDes_tipo_ciudad() {
        return des_tipo_ciudad;
    }

    public void setDes_tipo_ciudad(String des_tipo_ciudad) {
        this.des_tipo_ciudad = des_tipo_ciudad;
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

    public double getEstadoVisita() {
        return estadoVisita;
    }

    public void setEstadoVisita(double estadoVisita) {
        this.estadoVisita = estadoVisita;
    }

}
