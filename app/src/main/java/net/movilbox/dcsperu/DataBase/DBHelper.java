package net.movilbox.dcsperu.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import net.movilbox.dcsperu.Entry.CategoriasEstandar;
import net.movilbox.dcsperu.Entry.Ciudad;
import net.movilbox.dcsperu.Entry.Departamentos;
import net.movilbox.dcsperu.Entry.DetallePedido;
import net.movilbox.dcsperu.Entry.Distrito;
import net.movilbox.dcsperu.Entry.EntEstandar;
import net.movilbox.dcsperu.Entry.EntIndicadores;
import net.movilbox.dcsperu.Entry.EntLisSincronizar;
import net.movilbox.dcsperu.Entry.EntLoginR;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.EntNoticia_Repartidor;
import net.movilbox.dcsperu.Entry.EntRuteroList;
import net.movilbox.dcsperu.Entry.GrupoCombos;
import net.movilbox.dcsperu.Entry.GrupoSims;
import net.movilbox.dcsperu.Entry.GrupoCombos;
import net.movilbox.dcsperu.Entry.GrupoSims;
import net.movilbox.dcsperu.Entry.ListaGrupos;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.ListaPaquete;
import net.movilbox.dcsperu.Entry.Motivos;
import net.movilbox.dcsperu.Entry.NoVisita;
import net.movilbox.dcsperu.Entry.Nomenclatura;
import net.movilbox.dcsperu.Entry.PedidosEntrega;
import net.movilbox.dcsperu.Entry.PedidosEntregaSincronizar;
import net.movilbox.dcsperu.Entry.Referencia;
import net.movilbox.dcsperu.Entry.Referencia_equipo;
import net.movilbox.dcsperu.Entry.ReferenciasCombos;
import net.movilbox.dcsperu.Entry.ReferenciasEquipos;
import net.movilbox.dcsperu.Entry.ReferenciasSims;
import net.movilbox.dcsperu.Entry.RequestGuardarEditarPunto;
import net.movilbox.dcsperu.Entry.ResponseCreatePunt;
import net.movilbox.dcsperu.Entry.ResponseEntregarPedido;
import net.movilbox.dcsperu.Entry.ResponseHome;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.Entry.ResponseUser;
import net.movilbox.dcsperu.Entry.Serie;
import net.movilbox.dcsperu.Entry.Sincronizar;
import net.movilbox.dcsperu.Entry.SincronizarPedidos;
import net.movilbox.dcsperu.Entry.Subcategorias;
import net.movilbox.dcsperu.Entry.Territorio;
import net.movilbox.dcsperu.Entry.TimeService;
import net.movilbox.dcsperu.Entry.TipoCiudad;
import net.movilbox.dcsperu.Entry.TipoInterior;
import net.movilbox.dcsperu.Entry.TipoUrbanizacion;
import net.movilbox.dcsperu.Entry.TipoVia;
import net.movilbox.dcsperu.Entry.TipoVivienda;
import net.movilbox.dcsperu.Entry.Tracing;
import net.movilbox.dcsperu.Entry.Zona;
import net.movilbox.dcsperu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MydbDealerPeru.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 25);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlConnectManual = "CREATE TABLE ConnectManual (id integer primary key AUTOINCREMENT, status int )";

        String sqlNoticias="CREATE TABLE ListaNoticias (id INT, tipo TEXT, title TEXT, contenido TEXT, url TEXT, url_image TEXT, fecha TEXT,  fileName TEXT, fileUrl TEXT, estado int, fecha_lectura TEXT, sincronizado int, keys TEXT)";

        String sqlManualConnect="CREATE TABLE ManualConnect (id_auto integer primary key AUTOINCREMENT, id_user INT, id_distri INT,type TEXT, date TEXT, network_type TEXT )";

        String sqlGrupoCombos = "CREATE TABLE grupo_combos (id INT, nombre_grupo_combos TEXT, cant_cumplimiento_grupo_combos INT, cant_ventas_grupo_combos INT )";

        String sqlGrupoSims = "CREATE TABLE grupo_sims (id INT, nombre_grupo_sims TEXT, cant_cumplimiento_grupo_sims INT, cant_ventas_grupo_sims INT)";

        String sqlIndicadores = "CREATE TABLE indicadoresdas (id_auto integer primary key AUTOINCREMENT, cant_ventas_sim INT, cant_ventas_combo INT, cant_cumplimiento_sim INT, cant_cumplimiento_combo INT, cant_quiebre_sim_mes INT, id_vendedor INT, id_distri INT )";

        String sqlIntro = "CREATE TABLE intro (id integer primary key AUTOINCREMENT, idintro text )";

        String sqlTimeServices = "CREATE TABLE time_services (id integer primary key AUTOINCREMENT, idUser int, traking int, timeservice int, idDistri int, dataBase TEXT, fechainicial TEXT, fechafinal TEXT )";

        String sqlTraking = "CREATE TABLE tracing (id integer primary key AUTOINCREMENT, idUser INT, imei TEXT, dateTime TEXT, batteryLavel INT, temperatura INT, latitud REAL, " +
                            " longitud REAL, idDistri INT, dataBase TEXT, fechainicial TEXT, fechafinal TEXT )";

        String sqlCarrito = "CREATE TABLE carrito_pedido (id_carrito integer primary key AUTOINCREMENT, id_producto INT, pn_pro TEXT, stock INT, producto TEXT, dias_inve REAL, ped_sugerido TEXT, " +
                " cantidad_pedida INT, id_usuario TEXT, id_punto INT, latitud TEXT, longitud TEXT, tipo_producto INT, producto_img TEXT, precio_referencia REAL, precio_publico REAL, referencia INT)";

        String sqlLoginUsuario = "CREATE TABLE login (id INT, cedula INT, nombre TEXT, apellido TEXT, user TEXT, estado INT, bd TEXT, id_distri TEXT, perfil INT, igv REAL, intervalo INT, hora_inicial TEXT, " +
                " hora_final TEXT, cantidad_envios INT, fechaSincro TEXT, fechaSincroOffline TEXT, password TEXT)";

        String sqltTrritorios = "CREATE TABLE territorio (id INT, descripcion TEXT, estado INT )";

        String sqlZonas = "CREATE TABLE zona (id INT, descripcion TEXT, id_territorio INT, estado INT )";

        String sqlCategoria = "CREATE TABLE categoria (id INT, descripcion TEXT, estado_accion INT )";

        String sqlDepartamento = "CREATE TABLE departamento (id INT, descripcion TEXT, estado_accion INT )";

        String sqlIPunto = "CREATE TABLE punto (id_auto_incre_punto integer primary key AUTOINCREMENT, id_tabla TEXT, accion TEXT, categoria INT, cedula TEXT, celular TEXT, ciudad INT, codigo_cum TEXT, depto INT, des_tipo_ciudad TEXT, " +
                " descripcion_vivienda TEXT, distrito INT, email TEXT, estado_com INT, idpos INT, lote TEXT, nombre_cliente TEXT, nombre_mzn TEXT, nombre_punto TEXT, nombre_via TEXT, " +
                " nro_interior TEXT, nro_via INT, num_int_urbanizacion TEXT, ref_direccion TEXT, subcategoria INT, telefono INT, territorio_punto INT, texto_direccion TEXT, tipo_ciudad INT, " +
                " tipo_documento INT, tipo_interior INT, tipo_urbanizacion INT, tipo_via INT, tipo_vivienda INT, vende_recargas INT, zona INT, latitud REAL, longitud REAL, estado_visita INT, detalle TEXT, nro_movil INT) ";

        String sqlEstadoComercial = "CREATE TABLE estado_comercial (id INT, descripcion TEXT, estado_accion INT)";

        String sqlMunicipios = "CREATE TABLE municipios (id_muni INT, descripcion TEXT, departamento INT, estado_accion INT)";

        String sqlNomenclaturas = "CREATE TABLE nomenclaturas (id INT, nombre INT, letras TEXT, tipo_nom INT, estado_accion INT)";

        String sqlSubcategoriasPuntos = "CREATE TABLE subcategorias_puntos (id INT, descripcion TEXT, id_categoria INT, estado_accion INT)";

        String sqlDistritos = "CREATE TABLE distritos (id INT, descripcion TEXT, id_muni INT, id_depto INT, estado_accion INT)";

        String sqlRefesSim = "CREATE TABLE referencia_simcard (id INT, pn TEXT, stock INT, producto TEXT, dias_inve REAL, ped_sugerido TEXT, precio_referencia REAL, precio_publico REAL, quiebre INT, estado_accion INT )";

        String sqlRefesEquipo = "CREATE TABLE referencia_equipo(id INT, descripcion TEXT, precioventa REAL, speech TEXT, pantalla TEXT, cam_frontal TEXT, cam_tras TEXT, flash TEXT, banda TEXT, " +
                " memoria TEXT, expandible TEXT, bateria TEXT, bluetooth TEXT, tactil TEXT, tec_fisico TEXT, carrito_compras TEXT, correo TEXT, enrutador TEXT, radio TEXT, wifi TEXT, gps TEXT, so TEXT, " +
                " web TEXT, precio_referencia REAL, precio_publico REAL, img TEXT, estado_accion INT  )";

        String sqlRefesCombo = "CREATE TABLE referencia_combo (id INT, descripcion TEXT, precioventa REAL, speech TEXT, pantalla TEXT, cam_frontal TEXT, cam_tras TEXT, flash TEXT, banda TEXT, " +
                " memoria TEXT, expandible TEXT, bateria TEXT, bluetooth TEXT, tactil TEXT, tec_fisico TEXT, carrito_compras TEXT, correo TEXT, enrutador TEXT, radio TEXT, wifi TEXT, gps TEXT, so TEXT, " +
                " web TEXT, precio_referencia REAL, precio_publico REAL, img TEXT, estado_accion INT  )";

        String sqlListaPrecio = "CREATE TABLE lista_precios (id_referencia INT, idpos INT, valor_referencia REAl, valor_directo REAL, tipo_pro INT, estado_accion INT )";

        String sqlReferencia = "CREATE TABLE detalle_combo (id INT, pn TEXT, producto TEXT, descripcion TEXT, precio_referencia REAL, precio_publico REAL, dias_inve REAL, stock INT, ped_sugerido REAL, img TEXT, estado_accion INT, id_padre INT )";

        String sqlReferenciaequipo = "CREATE TABLE detalle_equipo (id INT, pn TEXT, producto TEXT, descripcion TEXT, precio_referencia REAL, precio_publico REAL, dias_inve REAL, stock INT, ped_sugerido REAL, img TEXT, estado_accion INT, id_padre INT )";

        String sqlNoVisita = "CREATE TABLE no_visita (idpos INT, motivo INT, observacion TEXT, latitud REAL, longitud REAL, iduser INT, iddis INT, db TEXT, perfil INT, fecha TEXT, hora TEXT)";

        String sqlCabezaPedido = "CREATE TABLE cabeza_pedido (id integer primary key AUTOINCREMENT,  iduser INT, iddistri TEXT, db TEXT, idpos INT, latitud REAL, longitud REAL, fecha TEXT, hora TEXT, comprobante INT)";

        String sqlDetallePedido = "CREATE TABLE detalle_pedido (id integer primary key AUTOINCREMENT, idCabeza INT, id_producto INT, cantidad_pedida INT, tipo_producto INT, referencia INT) ";

        String sqlPedidosEntrega= "CREATE TABLE pedido_entrega (idpos INT, razon TEXT, nombre_cli TEXT, barrio TEXT, cel TEXT, estado_comercial TEXT, email TEXT, direccion TEXT, departamento TEXT, munucipio TEXT, " +
                " distrito TEXT, id_circuito TEXT, circuito TEXT, idruta TEXT, ruta TEXT, tel TEXT, detalle TEXT, tipo_visita INT, rutero INT, latitud REAL, longitud REAL, fecha_ult TEXT, hora_ult TEXT, persona_ultima TEXT) ";

        String sqlDetallepedidoEntregar = "CREATE TABLE pedido_repartidor (idpos INT, razon_social TEXT, territorio TEXT, zona TEXT, direccion TEXT) ";

        String sqlDetallepedidoEntregarROW = "CREATE TABLE pedidos_grupo (nroPedido INT, cant_pedido INT, cant_pedido_p INT, total_pedido_p INT, fecha_pedido TEXT, " +
                "  hora_pedido TEXT, estado TEXT, fecha_entrega TEXT, igv REAL, sub_total REAL, nombre_usu TEXT, idpos INT, total_impueto_igv REAL) ";

        String sqlDetallepedidoEntregarNUMERO = "CREATE TABLE deta_pedido (nroPedido INT, nombre_sku TEXT, id_sku INT, cant_pedido INT, total_pedido RELA, cant_pedido_p INT, total_pedido_p REAL, tipo_pro TEXT) ";

        String sqlEntregaPedido = "CREATE TABLE entrega_pedido (id integer primary key AUTOINCREMENT, latitud REAL, longitud REAL, iduser INT, iddis INT, db TEXT, idpos INT, obs TEXT, idpedido INT, indicador INT, fecha_pedido fecha TEXT, hora_pedido TEXT) ";


        String sqlIndicadores_detalle = "CREATE TABLE indicadoresdas_detalle (id_auto integer primary key AUTOINCREMENT, idpos INT, tipo_visita INT, stock_sim INT, stock_combo INT, stock_seguridad_sim INT," +
                "  stock_seguridad_combo INT, dias_inve_sim REAL, dias_inve_combo REAL, id_vendedor INT, id_distri INT, fecha_dia TEXT, fecha_ult TEXT, hora_ult TEXT, orden INT)";

        String sqlMotivos = "CREATE TABLE motivos (id INT, descripcion TEXT)";

        String sqlInventario = "CREATE TABLE inventario (id INT, id_referencia INT, serie TEXT, paquete INT, id_vendedor INT, distri INT, tipo_pro INT, tipo_tabla INT, estado_accion INT, accion TEXT, combo INT )";

        String sqlCarritoAutoVenta = "CREATE TABLE carrito_autoventa (id_auto_carrito integer primary key AUTOINCREMENT, id_referencia INT, tipo_product INT, valor_refe REAL, valor_directo REAL, serie TEXT, id_punto INT, id_paquete INT, tipo_venta INT, id_producto INT,cantidad_soli INT)";

        String sqlCabezaAutoventa = "CREATE TABLE cabeza_autoventa_offline ( id INTEGER PRIMARY KEY AUTOINCREMENT,iduser INTEGER,iddistri INTEGER,db TEXT, idpos INTEGER,latitud REAL,longitud REAL,fecha TEXT,hora INTEGER,comprobante INTEGER );";

        String sqlDetalleAutoventa = "CREATE TABLE detalle_autoventa_offline (id INTEGER PRIMARY KEY AUTOINCREMENT,id_cabeza INTEGER,id_producto INTEGER,id_referencia INTEGER, valor_referencia REAL,serie TEXT,id_paquete INTEGER, tipo_venta INTEGER,tipo_producto INTEGER);";

        db.execSQL(sqlNoticias);
        db.execSQL(sqlConnectManual);
        db.execSQL(sqlManualConnect);
        db.execSQL(sqlGrupoCombos);
        db.execSQL(sqlGrupoSims);
        db.execSQL(sqlDetallepedidoEntregar);
        db.execSQL(sqlDetallepedidoEntregarROW);
        db.execSQL(sqlDetallepedidoEntregarNUMERO);

        db.execSQL(sqlIntro);
        db.execSQL(sqlCarrito);
        db.execSQL(sqlTimeServices);
        db.execSQL(sqlTraking);
        db.execSQL(sqlLoginUsuario);

        db.execSQL(sqltTrritorios);
        db.execSQL(sqlZonas);
        db.execSQL(sqlCategoria);
        db.execSQL(sqlDepartamento);
        db.execSQL(sqlIPunto);

        db.execSQL(sqlEstadoComercial);
        db.execSQL(sqlMunicipios);
        db.execSQL(sqlNomenclaturas);
        db.execSQL(sqlSubcategoriasPuntos);
        db.execSQL(sqlDistritos);

        db.execSQL(sqlRefesSim);
        db.execSQL(sqlListaPrecio);
        db.execSQL(sqlRefesCombo);
        db.execSQL(sqlReferencia);
        db.execSQL(sqlRefesEquipo);
        db.execSQL(sqlReferenciaequipo);
        db.execSQL(sqlNoVisita);
        db.execSQL(sqlCabezaPedido);
        db.execSQL(sqlDetallePedido);
        db.execSQL(sqlPedidosEntrega);
        db.execSQL(sqlEntregaPedido);
        db.execSQL(sqlIndicadores);
        db.execSQL(sqlIndicadores_detalle);
        db.execSQL(sqlMotivos);
        db.execSQL(sqlInventario);
        db.execSQL(sqlCarritoAutoVenta);
        db.execSQL(sqlCabezaAutoventa);
        db.execSQL(sqlDetalleAutoventa);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS ManualConnect");
        db.execSQL("DROP TABLE IF EXISTS ListaNoticias");
        db.execSQL("DROP TABLE IF EXISTS intro");
        db.execSQL("DROP TABLE IF EXISTS carrito_pedido");
        db.execSQL("DROP TABLE IF EXISTS time_services");
        db.execSQL("DROP TABLE IF EXISTS tracing");
        db.execSQL("DROP TABLE IF EXISTS login");
        db.execSQL("DROP TABLE IF EXISTS territorio");
        db.execSQL("DROP TABLE IF EXISTS zona");
        db.execSQL("DROP TABLE IF EXISTS categoria");
        db.execSQL("DROP TABLE IF EXISTS departamento");
        db.execSQL("DROP TABLE IF EXISTS punto");
        db.execSQL("DROP TABLE IF EXISTS no_visita");
        db.execSQL("DROP TABLE IF EXISTS cabeza_pedido");
        db.execSQL("DROP TABLE IF EXISTS detalle_pedido");
        db.execSQL("DROP TABLE IF EXISTS pedido_entrega");
        db.execSQL("DROP TABLE IF EXISTS pedido_repartidor");
        db.execSQL("DROP TABLE IF EXISTS pedidos_grupo");
        db.execSQL("DROP TABLE IF EXISTS deta_pedido");
        db.execSQL("DROP TABLE IF EXISTS entrega_pedido");
        db.execSQL("DROP TABLE IF EXISTS indicadoresdas");
        db.execSQL("DROP TABLE IF EXISTS ConnectManual");
        db.execSQL("DROP TABLE IF EXISTS motivos");
        db.execSQL("DROP TABLE IF EXISTS estado_comercial");
        db.execSQL("DROP TABLE IF EXISTS municipios");
        db.execSQL("DROP TABLE IF EXISTS nomenclaturas");
        db.execSQL("DROP TABLE IF EXISTS subcategorias_puntos");
        db.execSQL("DROP TABLE IF EXISTS distritos");
        db.execSQL("DROP TABLE IF EXISTS referencia_simcard");
        db.execSQL("DROP TABLE IF EXISTS lista_precios");
        db.execSQL("DROP TABLE IF EXISTS referencia_combo");
        db.execSQL("DROP TABLE IF EXISTS referencia_equipo");
        db.execSQL("DROP TABLE IF EXISTS detalle_combo");
        db.execSQL("DROP TABLE IF EXISTS detalle_equipo");
        db.execSQL("DROP TABLE IF EXISTS grupo_combos");
        db.execSQL("DROP TABLE IF EXISTS grupo_sims");
        db.execSQL("DROP TABLE IF EXISTS inventario");
        db.execSQL("DROP TABLE IF EXISTS carrito_autoventa");
        db.execSQL("DROP TABLE IF EXISTS Indicadores_detalle");
        db.execSQL("DROP TABLE IF EXISTS indicadoresdas_detalle");
        db.execSQL("DROP TABLE IF EXISTS cabeza_autoventa_offline");
        db.execSQL("DROP TABLE IF EXISTS detalle_autoventa_offline");
        this.onCreate(db);

    }

    public boolean updateConnect(int connect){

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("status", connect);
        int p = db.update("ConnectManual",valores,null,null);
        //db.close();
        return p > 0;

    }

    public boolean InsertConnect(int connect){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String count = "SELECT count(*) FROM ConnectManual";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0) {
            updateConnect(1);
        } else{
            try {
                values.put("status", connect);
                db.insert("ConnectManual", null, values);
            } catch (SQLiteConstraintException e) {
                Log.d("data", "failure to insert Manual Connect,", e);
                return false;
            } finally {
                db.close();
            }}
        return true;

    }

    public int getConnect(){
        int p=0;
        String sql = "SELECT status FROM ConnectManual";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            p=cursor.getInt(0);
        }
        return p;
    }


    public int validarTablas() {

        Cursor cursor;
        int indicador = 0;

        String sql = "SELECT * FROM departamento";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = 1;
        }

        return indicador;

    }

    public boolean validarPedidosDuplicados(int idPedido, int idPos) {
        Cursor cursor;
        boolean indicador = false;

        String[] args = new String[] {String.valueOf(idPos), String.valueOf(idPedido)};

        String sql = "SELECT * FROM entrega_pedido WHERE idpos = ? AND idpedido = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, args);
        if (cursor.moveToFirst()) {
            indicador = true;
        }

        return indicador;

    }

    public boolean deleteAllPedidosEntrega(int id_pos, int id_pedidos) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("entrega_pedido", "idpos = ? AND idpedido = ?", new String[]{String.valueOf(id_pos), String.valueOf(id_pedidos)});

        db.close();
        return a > 0;

    }

    public boolean insetEntregaPedido(double latitud, double longitud, int iduser, int iddis, String db_web, int idpos, String obs,  int idpedido, String indicador) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            values.put("latitud", latitud);
            values.put("longitud", longitud);
            values.put("iduser", iduser);
            values.put("iddis", iddis);
            values.put("db", db_web);
            values.put("idpos", idpos);
            values.put("obs", obs);
            values.put("idpedido", idpedido);
            values.put("indicador", indicador);
            values.put("fecha_pedido", getDatePhoneFecha());
            values.put("hora_pedido", getDatePhoneHora());

            db.insert("entrega_pedido", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertManualConnect( String type, int id_user, int id_distri, String Network_type) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            values.put("type", type);
            values.put("id_user", id_user);
            values.put("id_distri", id_distri);
            values.put("date", getDatePhoneFecha()+" "+getDatePhoneHora());
            values.put("Network_type", Network_type);

            db.insert("ManualConnect", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert Manual Connect,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public List<PedidosEntregaSincronizar> sincronizarsEntregaPedido() {

        List<PedidosEntregaSincronizar> pedidosEntregaSincronizars = new ArrayList<>();
        String sql = "SELECT * FROM entrega_pedido ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        PedidosEntregaSincronizar pedidosEntregaSincronizar;
        if (cursor.moveToFirst()) {
            do {

                pedidosEntregaSincronizar = new PedidosEntregaSincronizar();
                pedidosEntregaSincronizar.setLatitud(cursor.getDouble(1));
                pedidosEntregaSincronizar.setLongitud(cursor.getDouble(2));
                pedidosEntregaSincronizar.setIduser(cursor.getInt(3));
                pedidosEntregaSincronizar.setIddis(cursor.getInt(4));
                pedidosEntregaSincronizar.setDb(cursor.getString(5));
                pedidosEntregaSincronizar.setIdpos(cursor.getInt(6));
                pedidosEntregaSincronizar.setObs(cursor.getString(7));
                pedidosEntregaSincronizar.setIdpedido(cursor.getInt(8));
                pedidosEntregaSincronizar.setIndicador(cursor.getString(9));
                pedidosEntregaSincronizar.setFecha_pedido(cursor.getString(10));
                pedidosEntregaSincronizar.setHora_pedido(cursor.getString(11));

                pedidosEntregaSincronizars.add(pedidosEntregaSincronizar);
            } while(cursor.moveToNext());
        }
        return pedidosEntregaSincronizars;
    }

    public boolean insertEntregaPedidos(List<ResponseHome> data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();

        try {

            for (int i = 0; i < data.size(); i++) {

                values.put("idpos", data.get(i).getIdpos());
                values.put("razon", data.get(i).getRazon());
                values.put("nombre_cli", data.get(i).getNombre_cli());
                values.put("barrio", data.get(i).getBarrio());
                values.put("cel", data.get(i).getCel());
                values.put("estado_comercial", data.get(i).getEstado_comercial());
                values.put("email", data.get(i).getEmail());
                values.put("direccion", data.get(i).getDireccion());
                values.put("departamento", data.get(i).getDepartamento());
                values.put("munucipio", data.get(i).getMunucipio());
                values.put("distrito", data.get(i).getDistrito());
                values.put("id_circuito", data.get(i).getId_circuito());
                values.put("circuito", data.get(i).getCircuito());
                values.put("idruta", data.get(i).getIdruta());
                values.put("ruta", data.get(i).getRuta());
                values.put("tel", data.get(i).getTel());
                values.put("detalle", data.get(i).getDetalle());
                values.put("tipo_visita", data.get(i).getTipo_visita());
                values.put("rutero", data.get(i).getRutero());
                values.put("latitud", data.get(i).getLatitud());
                values.put("longitud", data.get(i).getLongitud());
                values.put("hora_ult", data.get(i).getHora_ult());
                values.put("persona_ultima", data.get(i).getPersona_ultima());

                db.insert("pedido_entrega", null, values);

                values1.put("idpos", data.get(i).getList().getIdpos());
                values1.put("razon_social", data.get(i).getList().getRazon_social());
                values1.put("territorio",  data.get(i).getList().getTerritorio());
                values1.put("zona", data.get(i).getList().getZona());
                values1.put("direccion", data.get(i).getList().getDireccion());

                db.insert("pedido_repartidor", null, values1);

                for (int g = 0; g < data.get(i).getList().getPedidosEntregaList().size(); g++) {

                    values2.put("nroPedido", data.get(i).getList().getPedidosEntregaList().get(g).getNroPedido());
                    values2.put("cant_pedido", data.get(i).getList().getPedidosEntregaList().get(g).getCant_pedido());
                    values2.put("cant_pedido_p", data.get(i).getList().getPedidosEntregaList().get(g).getCant_pedido_p());
                    values2.put("total_pedido_p", data.get(i).getList().getPedidosEntregaList().get(g).getTotal_pedido_p());
                    values2.put("fecha_pedido", data.get(i).getList().getPedidosEntregaList().get(g).getFecha_pedido());
                    values2.put("hora_pedido", data.get(i).getList().getPedidosEntregaList().get(g).getHora_pedido());
                    values2.put("estado", data.get(i).getList().getPedidosEntregaList().get(g).getEstado());
                    values2.put("fecha_entrega", data.get(i).getList().getPedidosEntregaList().get(g).getFecha_entrega());
                    values2.put("igv", data.get(i).getList().getPedidosEntregaList().get(g).getIgv());
                    values2.put("sub_total", data.get(i).getList().getPedidosEntregaList().get(g).getSub_total());
                    values2.put("nombre_usu", data.get(i).getList().getPedidosEntregaList().get(g).getNombre_usu());
                    values2.put("idpos", data.get(i).getList().getPedidosEntregaList().get(g).getIdpos());
                    values2.put("total_impueto_igv", data.get(i).getList().getPedidosEntregaList().get(g).getTotal_impueto_igv());

                    db.insert("pedidos_grupo", null, values2);

                    for (int a = 0; a < data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().size(); a++) {

                        values3.put("nroPedido", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getNroPedido());
                        values3.put("nombre_sku", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getNombre_sku());
                        values3.put("id_sku", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getId_sku());
                        values3.put("cant_pedido", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getCant_pedido());
                        values3.put("total_pedido", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getTotal_pedido());
                        values3.put("cant_pedido_p", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getCant_pedido_p());
                        values3.put("total_pedido_p", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getTotal_pedido_p());
                        values3.put("tipo_pro", data.get(i).getList().getPedidosEntregaList().get(g).getDetallePedidoList().get(a).getTipo_pro());

                        db.insert("deta_pedido", null, values3);
                    }
                }
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public List<ResponseHome> getVisitasRutero() {

        List<ResponseHome> responseHomeList = new ArrayList<>();

        String sql = "SELECT * FROM pedido_entrega ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        ResponseHome responseHome;

        if (cursor.moveToFirst()) {
            do {
                responseHome = new ResponseHome();
                responseHome.setIdpos(cursor.getInt(0));
                responseHome.setRazon(cursor.getString(1));
                responseHome.setNombre_cli(cursor.getString(2));
                responseHome.setBarrio(cursor.getString(3));
                responseHome.setCel(cursor.getString(4));
                responseHome.setEstado_comercial(cursor.getString(5));
                responseHome.setEmail(cursor.getString(6));
                responseHome.setDireccion(cursor.getString(7));
                responseHome.setDepartamento(cursor.getString(8));
                responseHome.setMunucipio(cursor.getString(9));
                responseHome.setDistrito(cursor.getString(10));
                responseHome.setId_circuito(cursor.getString(11));
                responseHome.setCircuito(cursor.getString(12));
                responseHome.setIdruta(cursor.getString(13));
                responseHome.setRuta(cursor.getString(14));
                responseHome.setTel(cursor.getString(15));
                responseHome.setDetalle(cursor.getString(16));
                responseHome.setTipo_visita(cursor.getInt(17));
                responseHome.setRutero(cursor.getInt(18));
                responseHome.setLatitud(cursor.getDouble(19));
                responseHome.setLongitud(cursor.getDouble(20));
                responseHome.setFecha_ult(cursor.getString(21));
                responseHome.setHora_ult(cursor.getString(22));
                responseHome.setPersona_ultima(cursor.getString(23));

                String sqlpedido_repartidor = "SELECT idpos, razon_social, territorio, zona, direccion  FROM pedido_repartidor WHERE idpos = ? ";
                Cursor cursor_repartidor = db.rawQuery(sqlpedido_repartidor, new String[]{String.valueOf(cursor.getInt(0))});

                ResponseEntregarPedido responseEntregarPedido;
                if (cursor_repartidor.moveToFirst()) {
                    do {
                        responseEntregarPedido = new ResponseEntregarPedido();
                        responseEntregarPedido.setIdpos(cursor_repartidor.getInt(0));
                        responseEntregarPedido.setRazon_social(cursor_repartidor.getString(1));
                        responseEntregarPedido.setTerritorio(cursor_repartidor.getString(2));
                        responseEntregarPedido.setZona(cursor_repartidor.getString(3));
                        responseEntregarPedido.setDireccion(cursor_repartidor.getString(4));

                    } while(cursor_repartidor.moveToNext());

                    String sqlpedido_grupo = "SELECT nroPedido, cant_pedido, cant_pedido_p, total_pedido_p, fecha_pedido, hora_pedido, estado, fecha_entrega, igv, sub_total, nombre_usu, idpos, total_impueto_igv FROM pedidos_grupo WHERE idpos = ? ";
                    Cursor cursor_grupo = db.rawQuery(sqlpedido_grupo, new String[]{String.valueOf(cursor.getInt(0))});
                    List<PedidosEntrega> pedidosEntregaList = new ArrayList<>();
                    PedidosEntrega pedidosEntrega;
                    if (cursor_grupo.moveToFirst()) {
                        do {
                            pedidosEntrega = new PedidosEntrega();
                            pedidosEntrega.setNroPedido(cursor_grupo.getInt(0));
                            pedidosEntrega.setCant_pedido(cursor_grupo.getInt(1));
                            pedidosEntrega.setCant_pedido_p(cursor_grupo.getInt(2));
                            pedidosEntrega.setTotal_pedido_p(cursor_grupo.getDouble(3));
                            pedidosEntrega.setFecha_pedido(cursor_grupo.getString(4));
                            pedidosEntrega.setHora_pedido(cursor_grupo.getString(5));
                            pedidosEntrega.setEstado(cursor_grupo.getString(6));
                            pedidosEntrega.setFecha_entrega(cursor_grupo.getString(7));
                            pedidosEntrega.setIgv(cursor_grupo.getDouble(8));
                            pedidosEntrega.setSub_total(cursor_grupo.getDouble(9));
                            pedidosEntrega.setNombre_usu(cursor_grupo.getString(10));
                            pedidosEntrega.setIdpos(cursor_grupo.getInt(11));
                            pedidosEntrega.setTotal_impueto_igv(cursor_grupo.getDouble(12));

                            String sqldeta_pedido = "SELECT nroPedido, nombre_sku, id_sku, cant_pedido, total_pedido, cant_pedido_p, total_pedido_p, tipo_pro FROM deta_pedido WHERE nroPedido = ? ";
                            Cursor cursor_deta_pedido = db.rawQuery(sqldeta_pedido, new String[]{String.valueOf(cursor_grupo.getInt(0))});
                            List<DetallePedido> detallePedidoList = new ArrayList<>();
                            DetallePedido detallePedido;
                            if (cursor_deta_pedido.moveToFirst()) {
                                do {
                                    detallePedido = new DetallePedido();

                                    detallePedido.setNroPedido(cursor_deta_pedido.getInt(0));
                                    detallePedido.setNombre_sku(cursor_deta_pedido.getString(1));
                                    detallePedido.setCant_pedido(cursor_deta_pedido.getInt(2));
                                    detallePedido.setTotal_pedido(cursor_deta_pedido.getDouble(3));
                                    detallePedido.setCant_pedido_p(cursor_deta_pedido.getInt(4));
                                    detallePedido.setTotal_pedido_p(cursor_deta_pedido.getDouble(5));
                                    detallePedido.setTipo_pro(cursor_deta_pedido.getString(6));

                                    detallePedidoList.add(detallePedido);

                                } while(cursor_deta_pedido.moveToNext());

                                pedidosEntrega.setDetallePedidoList(detallePedidoList);
                            }

                            pedidosEntregaList.add(pedidosEntrega);

                        } while(cursor_grupo.moveToNext());
                    }

                    responseEntregarPedido.setPedidosEntregaList(pedidosEntregaList);

                    responseHome.setList(responseEntregarPedido);
                }

                responseHomeList.add(responseHome);

            } while(cursor.moveToNext());

        }

        return responseHomeList;

    }

    public List<ResponseHome> getBuscarPuntoLocal(String nit_punto, String nombre_punto, String responsable, int depto, int ciudad, int distrito, int circuito, int ruta, int est_comercial) {

        List<ResponseHome> responseHomeList = new ArrayList<>();

        String condicion = "";

        String sql = "SELECT mpt.idpos, mpt.nombre_punto AS razon, mpt.nombre_cliente AS nombre_cli, mpt.texto_direccion direccion FROM punto AS mpt " +
                "   WHERE mpt.idpos >= 0 %1$s ORDER BY mpt.idpos" ;

        if(!nit_punto.isEmpty()) {
            condicion += " AND mpt.cedula = '"+nit_punto+"' ";
        }
        if(!responsable.isEmpty()) {
            condicion += " AND mpt.nombre_cliente = '"+responsable+"' ";
        }

        if(!nombre_punto.isEmpty()) {
            condicion += " AND mpt.nombre_punto LIKE '%"+nombre_punto+"%' ";
        }

        if(depto != 0) {
            condicion += " AND mpt.depto = "+depto;
        }

        if(ciudad != 0) {
            condicion += " AND mpt.ciudad = "+ciudad;
        }

        if(distrito != 0) {
            condicion += " AND mpt.distrito = "+distrito;
        }

        if(circuito != 0) {
            condicion += " AND mpt.territorio_punto = "+circuito;
        }

        if(ruta != 0) {
            condicion += " AND mpt.zona = "+ruta;
        }

        if(est_comercial != 0) {
            condicion += " AND mpt.estado_com = "+est_comercial;
        }

        String sqlFinal = String.format(sql, condicion);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlFinal, null);

        ResponseHome responseHome;

        if (cursor.moveToFirst()) {
            do {

                responseHome = new ResponseHome();

                responseHome.setIdpos(cursor.getInt(0));
                responseHome.setRazon(cursor.getString(1));
                responseHome.setDireccion(cursor.getString(3));

                responseHomeList.add(responseHome);

            } while (cursor.moveToNext());

        }
        return responseHomeList;
    }

    public int ultimoRegistro(String table){
        int _id = 0;
        String sql = "SELECT id FROM "+ table +" ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return _id;
    }

    public boolean insertPedidoOffLine(List<ReferenciasSims> data, int iduser, String iddistri, String bd, int idpos, double latitud, double longitud, int comprobante) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        try {

            values.put("iduser", iduser);
            values.put("iddistri", iddistri);
            values.put("db", bd);
            values.put("idpos", idpos);
            values.put("latitud", latitud);
            values.put("longitud", longitud);
            values.put("fecha", getDatePhoneFecha());
            values.put("hora", getDatePhoneHora());
            values.put("comprobante", comprobante);

            db.insert("cabeza_pedido", null, values);

            int id_auto = ultimoRegistro("cabeza_pedido");

            for (int i = 0; i < data.size(); i++) {

                values2.put("idCabeza", id_auto);
                values2.put("id_producto", data.get(i).getId());
                values2.put("cantidad_pedida", data.get(i).getCantidadPedida());
                values2.put("tipo_producto", data.get(i).getTipo_producto());
                values2.put("referencia", data.get(i).getId());

                db.insert("detalle_pedido", null, values2);
            }

            updateTipoVisita(idpos, 1);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertAutoventaOffLine(List<ReferenciasSims> data, int iduser, String iddistri, String bd, int idpos, double latitud, double longitud, int comprobante) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        try {

            values.put("iduser", iduser);
            values.put("iddistri", iddistri);
            values.put("db", bd);
            values.put("idpos", idpos);
            values.put("latitud", latitud);
            values.put("longitud", longitud);
            values.put("fecha", getDatePhoneFecha());
            values.put("hora", getDatePhoneHora());
            values.put("comprobante", comprobante);

            long id_auto  = db.insert("cabeza_autoventa_offline", null, values);

            //int id_auto = ultimoRegistro("cabeza_pedido");

            for (int i = 0; i < data.size(); i++) {

                values2.put("id_cabeza", id_auto);
                values2.put("id_producto", data.get(i).getId_producto());
                values2.put("id_referencia", data.get(i).getId());
                values2.put("valor_referencia", data.get(i).getPrecio_referencia());
                values2.put("serie", data.get(i).getSerie());
                values2.put("id_paquete", data.get(i).getId_paquete());
                values2.put("tipo_venta", data.get(i).getTipo_venta());
                values2.put("tipo_producto", data.get(i).getTipo_producto());

                db.insert("detalle_autoventa_offline", null, values2);
            }

            updateTipoVisita(idpos, 1);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateTipoVisita(int idpos, int valor){

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("tipo_visita", valor);

        String[] args = new String[]{String.valueOf(idpos)};
        int p = db.update("indicadoresdas_detalle", valores, "idpos = ?", args);
        //db.close();
        return p > 0;

    }

    private String getDatePhoneFecha() {

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);

        return formatteDate;

    }

    private String getDatePhoneHora() {

        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteDate = df.format(date);

        return formatteDate;

    }

    public boolean uptadeCabezaPedidoLocal(int idPosorigen, String idPosFinal){

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("idpos", idPosFinal);

        String[] args = new String[]{String.valueOf(idPosorigen)};
        int p = db.update("cabeza_pedido", valores, "idpos = ?", args);
        db.close();
        return p > 0;
    }

    public List<SincronizarPedidos> sincronizarPedido() {

        List<SincronizarPedidos> sincronizarPedidosArrayList = new ArrayList<>();

        String sql = "SELECT id, iduser, iddistri, db, idpos, latitud, longitud, fecha, hora, comprobante FROM cabeza_pedido";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        SincronizarPedidos sincronizarPedidos;

        if (cursor.moveToFirst()) {
            do {

                sincronizarPedidos = new SincronizarPedidos();

                sincronizarPedidos.setAutoincrement(cursor.getInt(0));
                sincronizarPedidos.setIduser(cursor.getInt(1));
                sincronizarPedidos.setIddistri(cursor.getString(2));
                sincronizarPedidos.setBd(cursor.getString(3));
                sincronizarPedidos.setIdpos(cursor.getInt(4));
                sincronizarPedidos.setLatitud(cursor.getDouble(5));
                sincronizarPedidos.setLongitud(cursor.getDouble(6));
                sincronizarPedidos.setFecha_visita(cursor.getString(7));
                sincronizarPedidos.setHora_visita(cursor.getString(8));
                sincronizarPedidos.setComprobante(cursor.getInt(9));

                String sql_detalle = "SELECT idCabeza, id_producto, cantidad_pedida, tipo_producto, referencia FROM detalle_pedido WHERE idCabeza = ?";

                Cursor cursor_detall = db.rawQuery(sql_detalle, new String[] {String.valueOf(cursor.getInt(0))});
                List<ReferenciasSims> referenciasSimsList = new ArrayList<>();
                ReferenciasSims referenciasSims;

                if (cursor_detall.moveToFirst()) {
                    do {
                        referenciasSims = new ReferenciasSims();
                        referenciasSims.setId_auto_carrito(cursor_detall.getInt(0));
                        referenciasSims.setId(cursor_detall.getInt(1));

                        referenciasSims.setCantidadPedida(cursor_detall.getInt(2));
                        referenciasSims.setTipo_producto(cursor_detall.getInt(3));

                        referenciasSimsList.add(referenciasSims);
                    } while (cursor_detall.moveToNext());

                    sincronizarPedidos.setReferenciasSimsList(referenciasSimsList);
                }

                sincronizarPedidosArrayList.add(sincronizarPedidos);

            } while (cursor.moveToNext());

        }
        return sincronizarPedidosArrayList;
    }

    public List<SincronizarPedidos> sincronizarAutoventa() {

        List<SincronizarPedidos> sincronizarPedidosArrayList = new ArrayList<>();

        String sql = "SELECT id,iduser,iddistri,db,idpos,latitud,longitud,fecha,hora,comprobante FROM cabeza_autoventa_offline;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        SincronizarPedidos sincronizarPedidos;

        if (cursor.moveToFirst()) {
            do {

                sincronizarPedidos = new SincronizarPedidos();

                sincronizarPedidos.setAutoincrement(cursor.getInt(0));
                sincronizarPedidos.setIduser(cursor.getInt(1));
                sincronizarPedidos.setIddistri(cursor.getString(2));
                sincronizarPedidos.setBd(cursor.getString(3));
                sincronizarPedidos.setIdpos(cursor.getInt(4));
                sincronizarPedidos.setLatitud(cursor.getDouble(5));
                sincronizarPedidos.setLongitud(cursor.getDouble(6));
                sincronizarPedidos.setFecha_visita(cursor.getString(7));
                sincronizarPedidos.setHora_visita(cursor.getString(8));
                sincronizarPedidos.setComprobante(cursor.getInt(9));

                String sql_detalle = "SELECT id_cabeza,id_producto,id_referencia,valor_referencia,serie,id_paquete,tipo_venta,tipo_producto FROM detalle_autoventa_offline WHERE id_cabeza = ?;";

                Cursor cursor_detall = db.rawQuery(sql_detalle, new String[] {String.valueOf(cursor.getInt(0))});
                List<ReferenciasSims> referenciasSimsList = new ArrayList<>();
                ReferenciasSims referenciasSims;

                if (cursor_detall.moveToFirst()) {
                    do {
                        referenciasSims = new ReferenciasSims();
                        referenciasSims.setId_auto_carrito(cursor_detall.getInt(0));
                        referenciasSims.setId_producto(cursor_detall.getInt(1));
                        referenciasSims.setId(cursor_detall.getInt(2));
                        referenciasSims.setPrecio_referencia(cursor_detall.getDouble(3));
                        referenciasSims.setSerie(cursor_detall.getString(4));
                        referenciasSims.setId_paquete(cursor_detall.getInt(5));
                        referenciasSims.setTipo_venta(cursor_detall.getInt(6));
                        referenciasSims.setTipo_producto(cursor_detall.getInt(7));

                        referenciasSimsList.add(referenciasSims);
                    } while (cursor_detall.moveToNext());

                    sincronizarPedidos.setReferenciasSimsList(referenciasSimsList);
                }

                sincronizarPedidosArrayList.add(sincronizarPedidos);

            } while (cursor.moveToNext());

        }
        return sincronizarPedidosArrayList;
    }

    public List<NoVisita> sincronizarNoVisita() {

        List<NoVisita> noVisitaList = new ArrayList<>();
        String sql = "SELECT idpos, motivo, observacion, latitud, longitud, iduser, iddis, fecha, hora FROM no_visita ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        NoVisita noVisita;
        if (cursor.moveToFirst()) {
            do {
                noVisita = new NoVisita();
                noVisita.setIdpos(cursor.getInt(0));
                noVisita.setMotivo(cursor.getInt(1));
                noVisita.setObservacion(cursor.getString(2));
                noVisita.setLatitud(cursor.getDouble(3));
                noVisita.setLongitud(cursor.getDouble(4));
                noVisita.setIduser(cursor.getInt(5));
                noVisita.setIdids(cursor.getInt(6));
                noVisita.setFecha_visita(cursor.getString(7));
                noVisita.setHora_visita(cursor.getString(8));
                noVisitaList.add(noVisita);
            } while(cursor.moveToNext());
        }
        return noVisitaList;
    }






    public boolean insertNoVisita(NoVisita data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {

            values.put("idpos", data.getIdpos());
            values.put("motivo", data.getMotivo());
            values.put("observacion", data.getObservacion());
            values.put("latitud", data.getLatitud());
            values.put("longitud", data.getLongitud());
            values.put("iduser", data.getIduser());
            values.put("iddis", data.getIdids());
            values.put("db", data.getDb());
            values.put("perfil", data.getPerfil());
            values.put("fecha", getDatePhoneFecha());
            values.put("hora", getDatePhoneHora());

            db.insert("no_visita", null, values);

            updateTipoVisita(data.getIdpos(), 2);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean deleteObjectPuntos(String name_database) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete(name_database, "accion = ?", new String[]{""});

        db.close();
        return a > 0;

    }

    public boolean deleteObject(String name_database) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete(name_database, null, null);

        db.close();
        return a > 0;

    }

    public String ultimoRegistroPunto(String table){
        String _id = "";
        String sql = "SELECT id_tabla FROM "+ table +" ORDER BY id_auto_incre_punto DESC LIMIT 1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                _id = cursor.getString(0);
            } while(cursor.moveToNext());
        }

        return _id;
    }

    public boolean insertPunto(Sincronizar data, int indicardor) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getPuntosList().size(); i++) {

                values.put("id_tabla", UUID.randomUUID().toString());
                values.put("accion", data.getPuntosList().get(i).getAccion());
                values.put("categoria", data.getPuntosList().get(i).getCategoria());
                values.put("cedula", data.getPuntosList().get(i).getCedula());
                values.put("celular", data.getPuntosList().get(i).getCelular());
                values.put("ciudad", data.getPuntosList().get(i).getCiudad());
                values.put("codigo_cum", data.getPuntosList().get(i).getCodigo_cum());
                values.put("depto", data.getPuntosList().get(i).getDepto());
                values.put("des_tipo_ciudad", data.getPuntosList().get(i).getDes_tipo_ciudad());
                values.put("descripcion_vivienda", data.getPuntosList().get(i).getDescripcion_vivienda());
                values.put("distrito", data.getPuntosList().get(i).getDistrito());
                values.put("email", data.getPuntosList().get(i).getEmail());
                values.put("estado_com", data.getPuntosList().get(i).getEstado_com());

                if (indicardor == 1)
                    values.put("idpos", (int) (Math.random()*100+1));
                else
                    values.put("idpos",  data.getPuntosList().get(i).getIdpos());

                values.put("lote", data.getPuntosList().get(i).getLote());
                values.put("nombre_cliente", data.getPuntosList().get(i).getNombre_cliente());
                values.put("nombre_mzn", data.getPuntosList().get(i).getNombre_mzn());
                values.put("nombre_punto", data.getPuntosList().get(i).getNombre_punto());
                values.put("nombre_via", data.getPuntosList().get(i).getNombre_via());
                values.put("nro_interior", data.getPuntosList().get(i).getNro_interior());
                values.put("nro_via", data.getPuntosList().get(i).getNro_via());
                values.put("num_int_urbanizacion", data.getPuntosList().get(i).getNum_int_urbanizacion());
                values.put("ref_direccion", data.getPuntosList().get(i).getRef_direccion());
                values.put("subcategoria", data.getPuntosList().get(i).getSubcategoria());
                values.put("telefono", data.getPuntosList().get(i).getTelefono());
                values.put("territorio_punto", data.getPuntosList().get(i).getTerritorio());
                values.put("texto_direccion", data.getPuntosList().get(i).getTexto_direccion());
                values.put("tipo_ciudad", data.getPuntosList().get(i).getTipo_ciudad());
                values.put("tipo_documento", data.getPuntosList().get(i).getTipo_documento());
                values.put("tipo_interior", data.getPuntosList().get(i).getTipo_interior());
                values.put("tipo_urbanizacion", data.getPuntosList().get(i).getTipo_urbanizacion());
                values.put("tipo_via", data.getPuntosList().get(i).getTipo_via());
                values.put("tipo_vivienda", data.getPuntosList().get(i).getTipo_vivienda());
                values.put("vende_recargas", data.getPuntosList().get(i).getVende_recargas());
                values.put("zona", data.getPuntosList().get(i).getZona());
                values.put("latitud", data.getPuntosList().get(i).getLatitud());
                values.put("longitud", data.getPuntosList().get(i).getLongitud());
                values.put("estado_visita", data.getPuntosList().get(i).getEstadoVisita());
                values.put("nro_movil", data.getPuntosList().get(i).getNro_movil());

                db.insert("punto", null, values);

            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean deletePuntoError(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("punto", "id_tabla = ? ", new String[]{id});

        db.close();
        return a > 0;

    }

    public List<ReferenciasSims> getSimcardLocal(String indicardor) {

        List<ReferenciasSims> referenciasSimses = new ArrayList<>();

        String sql = "SELECT refe.id, refe.pn, 0 stock, refe.producto, 0 dias_inve, 0 ped_sugerido, lprecio.valor_referencia, lprecio.valor_directo, 0 quiebre " +
                " FROM " +
                "  referencia_simcard refe INNER JOIN lista_precios lprecio ON lprecio.id_referencia = refe.id AND lprecio.idpos = ? GROUP BY refe.id";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {indicardor});
        ReferenciasSims referenciasSims;

        if (cursor.moveToFirst()) {
            do {

                referenciasSims = new ReferenciasSims();

                referenciasSims.setId(cursor.getInt(0));
                referenciasSims.setPn(cursor.getString(1));
                referenciasSims.setStock(cursor.getInt(2));
                referenciasSims.setProducto(cursor.getString(3));
                referenciasSims.setDias_inve(cursor.getInt(4));
                referenciasSims.setPed_sugerido(cursor.getString(5));
                referenciasSims.setPrecio_referencia(cursor.getDouble(6));
                referenciasSims.setPrecio_publico(cursor.getDouble(7));
                referenciasSims.setQuiebre(cursor.getInt(8));

                referenciasSimses.add(referenciasSims);

            } while (cursor.moveToNext());

        }
        return referenciasSimses;
    }

    public List<ReferenciasSims> getSimcardLocalVenta(String indicardor, int idpos) {

        List<ReferenciasSims> referenciasSimses = new ArrayList<>();
        String sql = "SELECT inv.id_referencia AS id,rs.producto,count(inv.id_referencia) AS stock,rs.dias_inve,lp.valor_referencia AS precio_referencia,lp.valor_directo AS precio_publico,rs.quiebre,inv.tipo_pro,\n" +
                    "(select sum(cantidad_soli) from carrito_autoventa where carrito_autoventa.id_referencia = inv.id_referencia) AS cantidad_soli\n" +
                    "FROM inventario inv \n" +
                    "INNER JOIN lista_precios lp ON lp.id_referencia = inv.id_referencia\n" +
                    "INNER JOIN referencia_simcard rs ON rs.id = inv.id_referencia\n" +
                    "WHERE inv.combo = 0 AND lp.idpos = ?\n" +
                    "GROUP BY inv.id_referencia";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {indicardor});
        ReferenciasSims referenciasSims;

        if (cursor.moveToFirst()) {
            do {

                referenciasSims = new ReferenciasSims();

                referenciasSims.setId(cursor.getInt(0));
                referenciasSims.setProducto(cursor.getString(1));
                referenciasSims.setStock(cursor.getInt(2));
                referenciasSims.setDias_inve(cursor.getDouble(3));
                referenciasSims.setPrecio_referencia(cursor.getDouble(4));
                referenciasSims.setPrecio_publico(cursor.getDouble(5));
                referenciasSims.setQuiebre(cursor.getInt(6));
                referenciasSims.setId_punto(idpos);
                referenciasSims.setTipo_producto(cursor.getInt(7));
                referenciasSims.setCantidadPedida(cursor.getInt(8));
                referenciasSimses.add(referenciasSims);

            } while (cursor.moveToNext());

        }
        return referenciasSimses;
    }

    public ReferenciasSims getPaqueteSims(ReferenciasSims data) {

        String idReferencia = String.valueOf(data.getId());
        List<ReferenciasSims> referenciasSimsPaquete = new ArrayList<>();
        List<ListaPaquete> listaPaquete = new ArrayList<>();
        String sql = "SELECT inv.paquete, count(inv.paquete) AS cantidad,(select sum(cantidad_soli) from carrito_autoventa where carrito_autoventa.id_paquete = inv.paquete AND carrito_autoventa.id_referencia = inv.id_referencia) AS cantidad_soli\n" +
                "FROM inventario inv \n" +
                "INNER JOIN referencia_simcard rs ON rs.id = inv.id_referencia\n" +
                "WHERE inv.id_referencia = ? GROUP BY inv.paquete;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {idReferencia});
        ListaPaquete paquete;

        if (cursor.moveToFirst()) {
            do {
                //data.getListaPaquete().setIdPaquete(cursor.getInt(0));
                paquete = new ListaPaquete();
                paquete.setIdPaquete(cursor.getInt(0));
                paquete.setCantidadPaquete(cursor.getInt(1));
                paquete.setCantidadSoli(cursor.getInt(2));
                listaPaquete.add(paquete);

            } while (cursor.moveToNext());
            data.setListaPaquete(listaPaquete);
        }
        return data;
    }

    public ListaPaquete getSerieSims(ListaPaquete data,int idReferencia) {

        String idPaquete = String.valueOf(data.getIdPaquete());
        List<Serie> listaSerie = new ArrayList<>();
        String sql = "SELECT inv.serie, inv.id ,CASE WHEN ca.serie IS NULL THEN 0 ELSE 1 END AS chekeado\n" +
                "FROM inventario inv \n" +
                "LEFT JOIN carrito_autoventa ca ON ca.serie = inv.serie\n" +
                "WHERE inv.id_referencia = ? AND paquete = ? AND tipo_pro = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(idReferencia),idPaquete});
        Serie serie;

        if (cursor.moveToFirst()) {
            do {
                //data.getListaPaquete().setIdPaquete(cursor.getInt(0));
                serie = new Serie();
                serie.setSerie(cursor.getString(0));
                serie.setId_pro(cursor.getInt(1));
                serie.setCheck(cursor.getInt(2));
                listaSerie.add(serie);

            } while (cursor.moveToNext());
            data.setListaSerie(listaSerie);
        }
        return data;
    }


    public List<ReferenciasCombos> getProductosCombos(String indicardor) {

        List<ReferenciasCombos> referenciasComboses = new ArrayList<>();

        String sql = "SELECT refe.id, refe.descripcion, refe.precioventa, refe.speech, refe.pantalla, refe.cam_frontal, refe.cam_tras, refe.flash, refe.banda, refe.memoria, " +
                " refe.expandible, refe.bateria, refe.bluetooth, refe.tactil, refe.tec_fisico, refe.carrito_compras, refe.correo, refe.enrutador, refe.radio, refe.wifi, refe.gps, " +
                " refe.so, refe.web, 0 quiebre, lprecio.valor_referencia, lprecio.valor_directo, refe.img " +
                " FROM " +
                "   referencia_combo refe INNER JOIN lista_precios lprecio ON lprecio.id_referencia = refe.id AND lprecio.idpos = ? AND " +
                " lprecio.tipo_pro = 2 GROUP BY refe.id";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {indicardor});
        ReferenciasCombos referenciasCombos;

        if (cursor.moveToFirst()) {
            do {

                referenciasCombos = new ReferenciasCombos();

                referenciasCombos.setId(cursor.getInt(0));
                referenciasCombos.setDescripcion(cursor.getString(1));
                referenciasCombos.setPrecio_referencia(cursor.getDouble(24));
                referenciasCombos.setPrecio_publico(cursor.getDouble(25));
                referenciasCombos.setImg(cursor.getString(26));

                String sqlDestall = "SELECT deta.id, deta.pn, 0 stock, deta.producto, 0 dias_inve, 0 ped_sugerido, deta.descripcion, 0 stock_seguridad, lprecio.valor_referencia, " +
                        " lprecio.valor_directo, deta.img, 0 quiebre " +
                        "   FROM " +
                        "     detalle_combo deta INNER JOIN lista_precios lprecio ON lprecio.id_referencia = deta.id " +
                        "   WHERE " +
                        "     deta.id_padre = ? AND " +
                        "     lprecio.tipo_pro = 2 GROUP BY deta.id";

                Cursor cursor_detalle = db.rawQuery(sqlDestall, new String[] {String.valueOf(cursor.getInt(0))});
                List<Referencia> referenciaList = new ArrayList<>();
                Referencia referencia;

                if (cursor_detalle.moveToFirst()) {

                    do {
                        referencia = new Referencia();
                        referencia.setId(cursor_detalle.getInt(0));
                        referencia.setPn(cursor_detalle.getString(1));
                        referencia.setStock(cursor_detalle.getInt(2));
                        referencia.setProducto(cursor_detalle.getString(3));
                        referencia.setDias_inve(cursor_detalle.getDouble(4));
                        referencia.setPed_sugerido(cursor_detalle.getString(5));

                        referencia.setPrecio_referencia(cursor_detalle.getDouble(8));
                        referencia.setPrecio_publico(cursor_detalle.getDouble(9));

                        referenciaList.add(referencia);

                    } while (cursor_detalle.moveToNext());
                }

                referenciasCombos.setReferenciaLis(referenciaList);

                referenciasComboses.add(referenciasCombos);

            } while (cursor.moveToNext());

        }

        return referenciasComboses;

    }

    public List<ReferenciasCombos> getCombosLocalVenta(String indicardor) {

        List<ReferenciasCombos> referenciasComboses = new ArrayList<>();

        String sql = "SELECT refe.id, refe.descripcion, refe.precioventa, refe.speech, refe.pantalla, refe.cam_frontal, refe.cam_tras, refe.flash, refe.banda, refe.memoria, \n" +
                "refe.expandible, refe.bateria, refe.bluetooth, refe.tactil, refe.tec_fisico, refe.carrito_compras, refe.correo, refe.enrutador, refe.radio, refe.wifi, refe.gps, \n" +
                "refe.so, refe.web, 0 quiebre, lprecio.valor_referencia, lprecio.valor_directo, refe.img,count(inv.id_referencia) AS stock \n" +
                "FROM inventario inv  \n" +
                "INNER JOIN lista_precios lprecio ON lprecio.id_referencia = inv.id_referencia\n" +
                "INNER JOIN detalle_combo dc ON inv.id_referencia = dc.id\n" +
                "INNER JOIN referencia_combo refe ON refe.id = dc.id_padre\n" +
                "WHERE inv.combo = 1 AND lprecio.idpos = ? AND lprecio.tipo_pro = 2 GROUP BY refe.id";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {indicardor});
        ReferenciasCombos referenciasCombos;

        if (cursor.moveToFirst()) {
            do {

                referenciasCombos = new ReferenciasCombos();

                referenciasCombos.setId(cursor.getInt(0));
                referenciasCombos.setDescripcion(cursor.getString(1));
                referenciasCombos.setPrecio_referencia(cursor.getDouble(24));
                referenciasCombos.setPrecio_publico(cursor.getDouble(25));
                referenciasCombos.setImg(cursor.getString(26));
                referenciasCombos.setStock(cursor.getInt(27));
                String sqlDestall = "SELECT deta.id, deta.pn, count(distinct inv.id) stock, deta.producto, deta.dias_inve, deta.ped_sugerido, deta.descripcion, 0 stock_seguridad, lprecio.valor_referencia, lprecio.valor_directo, deta.img, 0 quiebre\n" +
                        "FROM inventario inv\n" +
                        "INNER JOIN detalle_combo deta ON inv.id_referencia = deta.id\n" +
                        "INNER JOIN lista_precios lprecio ON lprecio.id_referencia = deta.id \n" +
                        "WHERE deta.id_padre = ? AND lprecio.tipo_pro = 2 GROUP BY deta.id";

                Cursor cursor_detalle = db.rawQuery(sqlDestall, new String[] {String.valueOf(cursor.getInt(0))});
                List<Referencia> referenciaList = new ArrayList<>();
                Referencia referencia;

                if (cursor_detalle.moveToFirst()) {

                    do {
                        referencia = new Referencia();
                        referencia.setId(cursor_detalle.getInt(0));
                        referencia.setPn(cursor_detalle.getString(1));
                        referencia.setStock(cursor_detalle.getInt(2));
                        referencia.setProducto(cursor_detalle.getString(3));
                        referencia.setDias_inve(cursor_detalle.getDouble(4));
                        referencia.setPed_sugerido(cursor_detalle.getString(5));
                        referencia.setPrecio_referencia(cursor_detalle.getDouble(8));
                        referencia.setPrecio_publico(cursor_detalle.getDouble(9));

                        referenciaList.add(referencia);

                    } while (cursor_detalle.moveToNext());
                }

                referenciasCombos.setReferenciaLis(referenciaList);

                referenciasComboses.add(referenciasCombos);

            } while (cursor.moveToNext());

        }

        return referenciasComboses;

    }

    public List<ReferenciasEquipos> getProductosEquipos(String indicardor) {

        List<ReferenciasEquipos> referenciasEquiposes = new ArrayList<>();

        String sql = "SELECT refe.id, refe.descripcion, refe.precioventa, refe.speech, refe.pantalla, refe.cam_frontal, refe.cam_tras, refe.flash, refe.banda, refe.memoria, " +
                " refe.expandible, refe.bateria, refe.bluetooth, refe.tactil, refe.tec_fisico, refe.carrito_compras, refe.correo, refe.enrutador, refe.radio, refe.wifi, refe.gps, " +
                " refe.so, refe.web, 0 quiebre, lprecio.valor_referencia, lprecio.valor_directo, refe.img " +
                " FROM " +
                "   referencia_equipo refe INNER JOIN lista_precios lprecio ON lprecio.id_referencia = refe.id AND lprecio.idpos = ? AND " +
                " lprecio.tipo_pro = 2 GROUP BY refe.id";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {indicardor});
        ReferenciasEquipos referenciasEquipos;

        if (cursor.moveToFirst()) {
            do {

                referenciasEquipos = new ReferenciasEquipos();

                referenciasEquipos.setId(cursor.getInt(0));
                referenciasEquipos.setDescripcion(cursor.getString(1));
                referenciasEquipos.setPrecio_referencia(cursor.getDouble(24));
                referenciasEquipos.setPrecio_publico(cursor.getDouble(25));
                referenciasEquipos.setImg(cursor.getString(26));

                String sqlDestall = "SELECT deta.id, deta.pn, 0 stock, deta.producto, 0 dias_inve, 0 ped_sugerido, deta.descripcion, 0 stock_seguridad, lprecio.valor_referencia, " +
                        " lprecio.valor_directo, deta.img, 0 quiebre " +
                        "   FROM " +
                        "     referenciasEquipos deta INNER JOIN lista_precios lprecio ON lprecio.id_referencia = deta.id " +
                        "   WHERE " +
                        "     deta.id_padre = ? AND " +
                        "     lprecio.tipo_pro = 2 GROUP BY deta.id";

                Cursor cursor_detalle = db.rawQuery(sqlDestall, new String[] {String.valueOf(cursor.getInt(0))});
                List<Referencia_equipo> referenciaList = new ArrayList<>();
                Referencia_equipo referencia;

                if (cursor_detalle.moveToFirst()) {

                    do {
                        referencia = new Referencia_equipo();
                        referencia.setId(cursor_detalle.getInt(0));
                        referencia.setPn(cursor_detalle.getString(1));
                        referencia.setStock(cursor_detalle.getInt(2));
                        referencia.setProducto(cursor_detalle.getString(3));
                        referencia.setDias_inve(cursor_detalle.getDouble(4));
                        referencia.setPed_sugerido(cursor_detalle.getString(5));

                        referencia.setPrecio_referencia(cursor_detalle.getDouble(8));
                        referencia.setPrecio_publico(cursor_detalle.getDouble(9));

                        referenciaList.add(referencia);

                    } while (cursor_detalle.moveToNext());
                }

                referenciasEquipos.setReferenciaLis(referenciaList);

                referenciasEquiposes.add(referenciasEquipos);

            } while (cursor.moveToNext());

        }

        return referenciasEquiposes;

    }

    public List<RequestGuardarEditarPunto> getPuntosSincronizar(String indicardor) {

        List<RequestGuardarEditarPunto> puntoList = new ArrayList<>();

        String[] args = new String[] {indicardor};
        String sql = "SELECT * FROM punto WHERE accion = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        RequestGuardarEditarPunto requestGuardarEditarPunto;

        if (cursor.moveToFirst()) {
            do {
                requestGuardarEditarPunto = new RequestGuardarEditarPunto();

                requestGuardarEditarPunto.setIdAuto(cursor.getString(1));
                requestGuardarEditarPunto.setAccion(cursor.getString(2));
                requestGuardarEditarPunto.setCategoria(cursor.getInt(3));
                requestGuardarEditarPunto.setCedula(cursor.getString(4));
                requestGuardarEditarPunto.setCelular(cursor.getString(5));
                requestGuardarEditarPunto.setCiudad(cursor.getInt(6));
                requestGuardarEditarPunto.setCodigo_cum(cursor.getString(7));
                requestGuardarEditarPunto.setDepto(cursor.getInt(8));
                requestGuardarEditarPunto.setDes_tipo_ciudad(cursor.getString(9));
                requestGuardarEditarPunto.setDescripcion_vivienda(cursor.getString(10));
                requestGuardarEditarPunto.setDistrito(cursor.getInt(11));
                requestGuardarEditarPunto.setEmail(cursor.getString(12));
                requestGuardarEditarPunto.setEstado_com(cursor.getInt(13));
                requestGuardarEditarPunto.setIdpos(cursor.getInt(14));
                requestGuardarEditarPunto.setLote(cursor.getString(15));
                requestGuardarEditarPunto.setNombre_cliente(cursor.getString(16));
                requestGuardarEditarPunto.setNombre_mzn(cursor.getString(17));
                requestGuardarEditarPunto.setNombre_punto(cursor.getString(18));
                requestGuardarEditarPunto.setNombre_via(cursor.getString(19));
                requestGuardarEditarPunto.setNro_interior(cursor.getString(20));
                requestGuardarEditarPunto.setNro_via(cursor.getString(21));
                requestGuardarEditarPunto.setNum_int_urbanizacion(cursor.getString(22));
                requestGuardarEditarPunto.setRef_direccion(cursor.getString(23));
                requestGuardarEditarPunto.setSubcategoria(cursor.getInt(24));

                requestGuardarEditarPunto.setTelefono(cursor.getString(25));
                requestGuardarEditarPunto.setTerritorio(cursor.getInt(26));
                requestGuardarEditarPunto.setTexto_direccion(cursor.getString(27));
                requestGuardarEditarPunto.setTipo_ciudad(cursor.getInt(28));
                requestGuardarEditarPunto.setTipo_documento(cursor.getInt(29));
                requestGuardarEditarPunto.setTipo_interior(cursor.getInt(30));
                requestGuardarEditarPunto.setTipo_urbanizacion(cursor.getInt(31));
                requestGuardarEditarPunto.setTipo_via(cursor.getInt(32));
                requestGuardarEditarPunto.setTipo_vivienda(cursor.getInt(33));
                requestGuardarEditarPunto.setVende_recargas(cursor.getInt(34));
                requestGuardarEditarPunto.setZona(cursor.getInt(35));
                requestGuardarEditarPunto.setLatitud(cursor.getDouble(36));
                requestGuardarEditarPunto.setLongitud(cursor.getDouble(37));
                requestGuardarEditarPunto.setNro_movil(cursor.getInt(40));

                puntoList.add(requestGuardarEditarPunto);

            } while (cursor.moveToNext());

        }

        return puntoList;

    }

    public ResponseCreatePunt getDepartamentos () {

        SQLiteDatabase db = this.getWritableDatabase();

        String sqlDepartamento = "SELECT 0 AS id, 'SELECCIONAR' AS descripcion UNION SELECT id, descripcion FROM departamento ";

        Cursor cursor_departamento = db.rawQuery(sqlDepartamento, null);

        ResponseCreatePunt responseCreatePunt = new ResponseCreatePunt();

        List<Departamentos> departamentosList = new ArrayList<>();
        Departamentos departamento;
        if (cursor_departamento.moveToFirst()) {
            do {

                departamento = new Departamentos(cursor_departamento.getInt(0), cursor_departamento.getString(1));

                String[] args = new String[] {String.valueOf(cursor_departamento.getInt(0))};
                String sqlProvicia = "SELECT 0 AS id_muni, 'SELECCIONAR' AS descripcion, 0 AS departamento UNION SELECT id_muni, descripcion, departamento FROM municipios WHERE departamento = ?";
                Cursor cursor_provincia = db.rawQuery(sqlProvicia, args);
                List<Ciudad> ciudadList = new ArrayList<>();
                Ciudad provincia;
                if (cursor_provincia.moveToFirst()) {
                    do {
                        provincia = new Ciudad(cursor_provincia.getInt(0), cursor_provincia.getString(1));
                        provincia.setDepartamento(cursor_provincia.getInt(2));

                        String[] args2 = new String[] {String.valueOf(cursor_provincia.getInt(0)), String.valueOf(cursor_departamento.getInt(0))};
                        String sqlDistrito = "SELECT 0 AS id, 'SELECCIONAR' AS descripcion, 0 AS id_muni, 0 AS id_depto UNION SELECT id, descripcion, id_muni, id_depto FROM distritos WHERE id_muni = ? AND id_depto = ? ";
                        Cursor cursor_distrito = db.rawQuery(sqlDistrito, args2);
                        List<Distrito> distritoList = new ArrayList<>();
                        Distrito distrito;
                        if (cursor_distrito.moveToFirst()) {
                            do {
                                distrito = new Distrito(cursor_distrito.getInt(0), cursor_distrito.getString(1));
                                distrito.setId_muni(cursor_distrito.getInt(2));
                                distrito.setId_depto(cursor_distrito.getInt(3));

                                distritoList.add(distrito);
                            } while (cursor_distrito.moveToNext());
                        }

                        provincia.setDistritoList(distritoList);

                        ciudadList.add(provincia);
                    } while (cursor_provincia.moveToNext());
                }

                departamento.setCiudadList(ciudadList);

                departamentosList.add(departamento);

            } while (cursor_departamento.moveToNext());

            responseCreatePunt.setDepartamentosList(departamentosList);
        }

        //Recuperamos los territorios
        String sqlTerritorio = "SELECT 0 AS id, 'SELECCIONAR' AS descripcion UNION SELECT id, descripcion FROM territorio ";
        Cursor cursor_Territorio = db.rawQuery(sqlTerritorio, null);
        List<Territorio> territorioList = new ArrayList<>();
        Territorio territorio;
        if (cursor_Territorio.moveToFirst()) {
            do {
                territorio = new Territorio();
                territorio.setId(cursor_Territorio.getInt(0));
                territorio.setDescripcion(cursor_Territorio.getString(1));

                String[] args3 = new String[] {String.valueOf(cursor_Territorio.getInt(0))};
                String sqlZona = "SELECT 0 AS id, 'SELECCIONAR' AS descripcion, 0 AS id_territorio, 0 AS estado UNION SELECT id, descripcion, id_territorio, estado FROM zona WHERE id_territorio = ? ";
                Cursor cursor_zona = db.rawQuery(sqlZona, args3);
                List<Zona> zonaList = new ArrayList<>();
                Zona zona;
                if (cursor_zona.moveToFirst()) {
                    do {
                        zona = new Zona();

                        zona.setId(cursor_zona.getInt(0));
                        zona.setDescripcion(cursor_zona.getString(1));
                        zona.setId_territorio(cursor_zona.getInt(2));

                        zonaList.add(zona);

                    } while (cursor_zona.moveToNext());
                }

                territorio.setZonaList(zonaList);

                territorioList.add(territorio);

            } while (cursor_Territorio.moveToNext());
        }

        responseCreatePunt.setTerritorioList(territorioList);

        // Recuperamos las nomesclaturas
        // tipo via = 0.
        // Tipo interior = 1.
        // tipo vivienda = 2.
        // Tipo urbanización = 3.
        // Tipo ciudad Poblado = 4.

        String sqlNomenclatura= "SELECT id, nombre, letras, tipo_nom, estado_accion FROM nomenclaturas";
        Cursor cursor_nomencla = db.rawQuery(sqlNomenclatura, null);

        Nomenclatura nomenclatura = new Nomenclatura();

        List<TipoVia> tipoViaList = new ArrayList<>();
        TipoVia tipoVia;

        List<TipoVivienda> tipoViviendaList = new ArrayList<>();
        TipoVivienda tipoVivienda;

        List<TipoInterior> tipoInteriorList = new ArrayList<>();
        TipoInterior tipoInterior;

        List<TipoUrbanizacion> tipoUrbanizacionList = new ArrayList<>();
        TipoUrbanizacion tipoUrbanizacion;

        List<TipoCiudad> tipoCiudadList = new ArrayList<>();
        TipoCiudad tipoCiudad;

        if (cursor_nomencla.moveToFirst()) {
            do {

                if (cursor_nomencla.getInt(3) == 0) {
                    tipoVia = new TipoVia(cursor_nomencla.getInt(0), cursor_nomencla.getString(1));
                    tipoVia.setSiglas(cursor_nomencla.getString(2));
                    tipoViaList.add(tipoVia);
                }

                if (cursor_nomencla.getInt(3) == 1) {
                    tipoInterior = new TipoInterior(cursor_nomencla.getInt(0), cursor_nomencla.getString(1));
                    tipoInterior.setSiglas(cursor_nomencla.getString(2));
                    tipoInteriorList.add(tipoInterior);
                }

                if (cursor_nomencla.getInt(3) == 2) {
                    tipoVivienda = new TipoVivienda(cursor_nomencla.getInt(0), cursor_nomencla.getString(1));
                    tipoVivienda.setSiglas(cursor_nomencla.getString(2));
                    tipoViviendaList.add(tipoVivienda);
                }

                if (cursor_nomencla.getInt(3) == 3) {
                    tipoUrbanizacion = new TipoUrbanizacion(cursor_nomencla.getInt(0), cursor_nomencla.getString(1));
                    tipoUrbanizacion.setSiglas(cursor_nomencla.getString(2));
                    tipoUrbanizacionList.add(tipoUrbanizacion);
                }

                if (cursor_nomencla.getInt(3) == 4) {
                    tipoCiudad = new TipoCiudad(cursor_nomencla.getInt(0), cursor_nomencla.getString(1));
                    tipoCiudad.setSiglas(cursor_nomencla.getString(2));
                    tipoCiudadList.add(tipoCiudad);
                }

            } while (cursor_nomencla.moveToNext());
        }

        tipoViaList.add(0, new TipoVia(0, "SELECCIONAR"));

        tipoViviendaList.add(0, new TipoVivienda(0, "SELECCIONAR"));
        tipoViviendaList.add(0, new TipoVivienda(0, "SELECCIONAR"));
        tipoInteriorList.add(0, new TipoInterior(0, "SELECCIONAR"));
        tipoUrbanizacionList.add(0, new TipoUrbanizacion(0, "SELECCIONAR"));
        tipoCiudadList.add(0, new TipoCiudad(0, "SELECCIONAR"));


        nomenclatura.setTipoViaList(tipoViaList);
        nomenclatura.setTipoViviendaList(tipoViviendaList);
        nomenclatura.setTipoInteriorList(tipoInteriorList);
        nomenclatura.setTipoUrbanizacionList(tipoUrbanizacionList);
        nomenclatura.setTipoCiudadList(tipoCiudadList);

        responseCreatePunt.setNomenclaturaList(nomenclatura);

        //Recuperamos las categorias.
        String sqlCategoria = "SELECT id, descripcion, estado_accion FROM categoria";
        Cursor cursor_categoria = db.rawQuery(sqlCategoria, null);
        List<CategoriasEstandar> categoriasEstandarList = new ArrayList<>();
        CategoriasEstandar categoriasEstandar;
        if (cursor_categoria.moveToFirst()) {
            do {
                categoriasEstandar = new CategoriasEstandar(cursor_categoria.getInt(0), cursor_categoria.getString(1));

                String[] args4 = new String[] {String.valueOf(cursor_categoria.getInt(0))};
                String sqlCategoriaSub = "SELECT id, descripcion, id_categoria, estado_accion FROM subcategorias_puntos WHERE id_categoria = ?";
                Cursor cursor_categoria_sub = db.rawQuery(sqlCategoriaSub, args4);
                List<Subcategorias> subcategoriasList = new ArrayList<>();
                Subcategorias subcategorias;
                if (cursor_categoria_sub.moveToFirst()) {
                    do {
                        subcategorias = new Subcategorias();
                        subcategorias.setId(cursor_categoria_sub.getInt(0));
                        subcategorias.setDescripcion(cursor_categoria_sub.getString(1));
                        subcategorias.setId_categoria(cursor_categoria_sub.getInt(2));
                        subcategoriasList.add(subcategorias);
                    } while (cursor_categoria_sub.moveToNext());
                }

                categoriasEstandar.setListSubCategoria(subcategoriasList);

                categoriasEstandarList.add(categoriasEstandar);
            } while (cursor_categoria.moveToNext());
        }

        responseCreatePunt.setCategoriasList(categoriasEstandarList);

        //Recuperamos los estados comerciales
        String sqlEstadoCom = "SELECT id, descripcion FROM estado_comercial";
        Cursor cursor_estado_com = db.rawQuery(sqlEstadoCom, null);
        List<CategoriasEstandar> categoriasEstandarList1 = new ArrayList<>();
        CategoriasEstandar categoriasEstandar1;
        if (cursor_estado_com.moveToFirst()) {
            do {
                categoriasEstandar1 = new CategoriasEstandar(cursor_estado_com.getInt(0), cursor_estado_com.getString(1));
                categoriasEstandarList1.add(categoriasEstandar1);
            } while (cursor_estado_com.moveToNext());
        }

        categoriasEstandarList1.add(0, new CategoriasEstandar(0, "SELECCIONAR"));
        responseCreatePunt.setEstadoComunList(categoriasEstandarList1);

        return responseCreatePunt;

    }

    // Nueva edición -------------------------------------------------------

    public boolean insertLisPrecios(EntLisSincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            values.put("id_referencia", data.getId_referencia());
            values.put("idpos", data.getIdpos());
            values.put("valor_referencia", data.getValor_referencia());
            values.put("valor_directo", data.getValor_directo());
            values.put("tipo_pro", data.getTipo_pro());
            values.put("estado_accion", data.getEstado_accion());

            db.insert("lista_precios", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }



    public boolean insertListMotivos(EntLisSincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            values.put("id", data.getId());
            values.put("descripcion", data.getDescripcion());

            db.insert("motivos", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertLisInventario(EntLisSincronizar data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            switch (data.getEstado_accion()) {
                case 1:
                    values.put("id", data.getId());
                    values.put("id_referencia", data.getId_referencia());
                    values.put("serie", data.getSerie());
                    values.put("paquete", data.getPaquete());
                    values.put("id_vendedor", data.getId_vendedor());
                    values.put("distri", data.getDistri());
                    values.put("tipo_pro", data.getTipo_pro());
                    values.put("tipo_tabla", data.getTipoTabla());
                    values.put("estado_accion", data.getEstado_accion());
                    values.put("combo", data.getCombo());
                    db.insert("inventario", null, values);

                    break;
                case 0:
                    db.delete("inventario", "id = ? AND tipo_pro = ? ", new String[]{String.valueOf(data.getId()), String.valueOf(data.getTipo_pro())});
                    break;
            }
        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;

    }



    public List<EntLisSincronizar> listReferenciasesReport(int indicador) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<EntLisSincronizar> referenciaList = new ArrayList<>();

        String sql = "";

        // 1. Reporte
        // 2. Spinner

        if (indicador == 1) {
            sql = "SELECT inv.paquete, inv.id_referencia, inv.tipo_pro, CASE WHEN rs.producto is null THEN d_co.producto ELSE rs.producto END producto, count(inv.id) cantidad FROM inventario inv LEFT JOIN referencia_simcard rs ON rs.id = inv.id_referencia LEFT JOIN detalle_combo d_co ON d_co.id = inv.id_referencia GROUP BY inv.id_referencia";
        } else if (indicador == 2) {
            sql = "SELECT 0 paquete, 0 id_referencia, 0 tipo_pro,'SELECCIONAR' producto, 0 cantidad UNION SELECT inv.paquete,inv.id_referencia, inv.tipo_pro, rs.producto,count(inv.id) cantidad FROM inventario inv INNER JOIN refes_sims rs ON rs.id = inv.id_referencia GROUP BY inv.id_referencia";
        }

        Cursor cursor = db.rawQuery(sql, null);

        EntLisSincronizar referencias;

        if (cursor.moveToFirst()) {

            do {
                referencias = new EntLisSincronizar();
                referencias.setPaquete(cursor.getInt(0));
                referencias.setId_referencia(cursor.getInt(1));
                referencias.setTipo_pro(cursor.getInt(2));
                referencias.setProducto(cursor.getString(3));
                referencias.setStock(cursor.getInt(4));

                referenciaList.add(referencias);

            } while (cursor.moveToNext());

        }

        return referenciaList;
    }

    public List<EntLisSincronizar> listPaqueteInvent(int id_referencia) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<EntLisSincronizar> referenciaList = new ArrayList<>();
        String sql = "SELECT paquete, id_referencia, tipo_pro, count(id) cantidad FROM inventario inv WHERE id_referencia = ? GROUP BY paquete ";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id_referencia)});

        if (cursor.moveToFirst()) {

            do {

                EntLisSincronizar referencias = new EntLisSincronizar();
                referencias.setPaquete(cursor.getInt(0));
                referencias.setId_referencia(cursor.getInt(1));
                referencias.setTipo_pro(cursor.getInt(2));
                referencias.setStock(cursor.getInt(3));

                String sql2 = "SELECT id, serie, tipo_pro FROM inventario WHERE paquete = ? AND tipo_pro = ? AND id_referencia = ? ";
                Cursor cursor2 = db.rawQuery(sql2, new String[]{String.valueOf(cursor.getInt(0)), String.valueOf(cursor.getInt(2)), String.valueOf(id_referencia) });
                List<EntEstandar> entEstandarList = new ArrayList<>();

                if (cursor2.moveToFirst()) {

                    do {
                        EntEstandar entEstandar = new EntEstandar();
                        entEstandar.setId(cursor2.getInt(0));
                        entEstandar.setDescripcion(cursor2.getString(1));
                        entEstandar.setTipo_prod(cursor2.getInt(2));
                        entEstandarList.add(entEstandar);
                    } while (cursor2.moveToNext());

                    referencias.setEntEstandarList(entEstandarList);
                }

                referenciaList.add(referencias);

            } while (cursor.moveToNext());

        }

        return referenciaList;
    }

    public boolean inventarioPropio() {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT paquete, id_referencia, tipo_pro, count(id) cantidad FROM inventario inv";

        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            return  true;
        }

        return false;
    }

    public boolean insertReferenciaCombos(EntLisSincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        try {

            values.put("id", data.getId());
            values.put("descripcion", data.getDescripcion());
            values.put("precioventa", data.getPrecioventa());
            values.put("speech", data.getSpeech());
            values.put("pantalla", data.getPantalla());
            values.put("cam_frontal", data.getCam_frontal());
            values.put("cam_tras", data.getCam_tras());
            values.put("flash", data.getFlash());
            values.put("banda", data.getBanda());
            values.put("memoria", data.getMemoria());
            values.put("expandible", data.getExpandible());
            values.put("bateria", data.getBateria());
            values.put("bluetooth", data.getBluetooth());
            values.put("tactil", data.getTactil());
            values.put("tec_fisico", data.getTec_fisico());
            values.put("carrito_compras", data.getCarrito_compras());
            values.put("correo", data.getCorreo());
            values.put("enrutador", data.getEnrutador());
            values.put("radio", data.getRadio());
            values.put("wifi", data.getWifi());
            values.put("gps", data.getGps());
            values.put("so", data.getSo());
            values.put("web", data.getWeb());
            values.put("precio_referencia", data.getPrecio_referencia());
            values.put("precio_publico", data.getPrecio_publico());
            values.put("img", data.getImg());
            values.put("estado_accion", data.getEstado_accion());

            db.insert("referencia_combo", null, values);

            for (int l = 0; l < data.getReferenciaLis().size(); l++) {

                values2.put("id", data.getReferenciaLis().get(l).getId());
                values2.put("pn", data.getReferenciaLis().get(l).getPn());
                values2.put("producto", data.getReferenciaLis().get(l).getProducto());
                values2.put("descripcion", data.getReferenciaLis().get(l).getDescripcion());
                values2.put("precio_referencia", data.getReferenciaLis().get(l).getPrecio_referencia());
                values2.put("precio_publico", data.getReferenciaLis().get(l).getPrecio_publico());
                values2.put("dias_inve", data.getReferenciaLis().get(l).getDias_inve());
                values2.put("stock", data.getReferenciaLis().get(l).getStock());
                values2.put("ped_sugerido", data.getReferenciaLis().get(l).getPed_sugerido());
                values2.put("img", data.getReferenciaLis().get(l).getUrl_imagen());
                values2.put("estado_accion", data.getReferenciaLis().get(l).getEstado_accion());
                values2.put("id_padre", data.getReferenciaLis().get(l).getId_padre());

                db.insert("detalle_combo", null, values2);

            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertReferenciaSim(EntLisSincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("id", data.getId());
            values.put("pn", data.getPn());
            values.put("stock", data.getStock());
            values.put("producto", data.getProducto());
            values.put("dias_inve", data.getDias_inve());
            values.put("ped_sugerido", data.getPed_sugerido());
            values.put("precio_referencia", data.getPrecio_referencia());
            values.put("precio_publico", data.getPrecio_publico());
            values.put("quiebre", data.getQuiebre());
            values.put("estado_accion", data.getEstado_accion());

            db.insert("referencia_simcard", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertPunto(EntLisSincronizar data, int indicardor) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {

            values.put("id_tabla", UUID.randomUUID().toString());
            values.put("accion", data.getAccion());
            values.put("categoria", data.getCategoria());
            values.put("cedula", data.getCedula());
            values.put("celular", data.getCelular());
            values.put("ciudad", data.getCiudad());
            values.put("codigo_cum", data.getCodigo_cum());
            values.put("depto", data.getDepto());
            values.put("des_tipo_ciudad", data.getDes_tipo_ciudad());
            values.put("descripcion_vivienda", data.getDescripcion_vivienda());
            values.put("distrito", data.getDistrito());
            values.put("email", data.getEmail());
            values.put("estado_com", data.getEstado_com());

            if (indicardor == 1)
                values.put("idpos", (int) (Math.random()*100+1));
            else
                values.put("idpos",  data.getIdpos());

            values.put("lote", data.getLote());
            values.put("nombre_cliente", data.getNombre_cliente());
            values.put("nombre_mzn", data.getNombre_mzn());
            values.put("nombre_punto", data.getNombre_punto());
            values.put("nombre_via", data.getNombre_via());
            values.put("nro_interior", data.getNro_interior());
            values.put("nro_via", data.getNro_via());
            values.put("num_int_urbanizacion", data.getNum_int_urbanizacion());
            values.put("ref_direccion", data.getRef_direccion());
            values.put("subcategoria", data.getSubcategoria());
            values.put("telefono", data.getTelefono());
            values.put("territorio_punto", data.getTerritorio());
            values.put("texto_direccion", data.getTexto_direccion());
            values.put("tipo_ciudad", data.getTipo_ciudad());
            values.put("tipo_documento", data.getTipo_documento());
            values.put("tipo_interior", data.getTipo_interior());
            values.put("tipo_urbanizacion", data.getTipo_urbanizacion());
            values.put("tipo_via", data.getTipo_via());
            values.put("tipo_vivienda", data.getTipo_vivienda());
            values.put("vende_recargas", data.getVende_recargas());
            values.put("zona", data.getZona());
            values.put("latitud", data.getLatitud());
            values.put("longitud", data.getLongitud());
            values.put("estado_visita", data.getEstadoVisita());
            values.put("detalle", data.getDetalle());

            db.insert("punto", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertTerritorio(EntLisSincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("id", data.getId());
            values.put("descripcion", data.getDescripcion());

            db.insert("territorio", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertZona(EntLisSincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("id", data.getId());
            values.put("descripcion", data.getDescripcion());
            values.put("id_territorio", data.getId_territorio());
            values.put("estado", "");

            db.insert("zona", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertCategoria(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("categoria", null, values);
                    break;

                case 2:
                    //Actualizar
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.update("categoria", values, String.format("id = %1$s", entEstandar.getId()), null);
                    break;

                case 0:
                    //Eliminar
                    db.delete("categoria", "id = ? ", new String[]{String.valueOf(entEstandar.getId())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            db.close();
            return false;
        } finally {
            db.close();
        }

        return true;

    }

    public boolean insertDepartamento(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("departamento", null, values);
                    break;

                case 2:
                    //Actualizar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.update("departamento", values, String.format("id = %1$s", entEstandar.getId()), null);
                    break;

                case 0:
                    //Eliminar
                    db.delete("departamento", "id = ? ", new String[]{String.valueOf(entEstandar.getId())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertDistritos(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("id_muni", entEstandar.getId_muni());
                    values.put("id_depto", entEstandar.getId_depto());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("distritos", null, values);
                    break;

                case 2:
                    //Actualizar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("id_muni", entEstandar.getId_muni());
                    values.put("id_depto", entEstandar.getId_depto());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.update("distritos", values, String.format("id = %1$s", entEstandar.getId()), null);
                    break;

                case 0:
                    //Eliminar
                    db.delete("distritos", "id = ? ", new String[]{String.valueOf(entEstandar.getId())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            db.close();
            return false;
        } finally {
            db.close();
        }

        return true;

    }

    public boolean insertEstadoComercial(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("estado_comercial", null, values);
                    break;

                case 2:
                    //Actualizar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.update("estado_comercial", values, String.format("id = %1$s", entEstandar.getId()), null);
                    break;

                case 0:
                    //Eliminar
                    db.delete("estado_comercial", "id = ? ", new String[]{String.valueOf(entEstandar.getId())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            db.close();
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertMunicipios(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id_muni", entEstandar.getId_muni());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("departamento", entEstandar.getDepartamento());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("municipios", null, values);
                    break;

                case 2:
                    //Actualizar.
                    values.put("id_muni", entEstandar.getId_muni());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("departamento", entEstandar.getDepartamento());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.update("municipios", values, String.format("id_muni = %1$s", entEstandar.getId_muni()), null);
                    break;

                case 0:
                    //Eliminar
                    db.delete("municipios", "id_muni = ? ", new String[]{String.valueOf(entEstandar.getId_muni())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            db.close();
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertNomenclaturas(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id", entEstandar.getId());
                    values.put("nombre", entEstandar.getNombre());
                    values.put("letras", entEstandar.getLetras());
                    values.put("tipo_nom", entEstandar.getTipo_nom());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("nomenclaturas", null, values);
                    break;

                case 2:
                    //Actualizar.
                    values.put("id", entEstandar.getId());
                    values.put("nombre", entEstandar.getNombre());
                    values.put("letras", entEstandar.getLetras());
                    values.put("tipo_nom", entEstandar.getTipo_nom());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.update("nomenclaturas", values, String.format("id = %1$s", entEstandar.getId()), null);
                    break;

                case 0:
                    //Eliminar
                    db.delete("nomenclaturas", "id = ? ", new String[]{String.valueOf(entEstandar.getId())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            db.close();
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertSubcategoriasPuntos(EntEstandar entEstandar) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            switch (entEstandar.getEstado_accion()) {

                case 1:
                    //Insertar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("id_categoria", entEstandar.getId_categoria());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    db.insert("subcategorias_puntos", null, values);
                    break;

                case 2:
                    //Actualizar.
                    values.put("id", entEstandar.getId());
                    values.put("descripcion", entEstandar.getDescripcion());
                    values.put("id_categoria", entEstandar.getId_categoria());
                    values.put("estado_accion", entEstandar.getEstado_accion());

                    int p = db.update("subcategorias_puntos", values, String.format("id = %1$s", entEstandar.getId()), null);
                    break;

                case 0:
                    //Eliminar
                    int a = db.delete("subcategorias_puntos", "id = ? ", new String[]{String.valueOf(entEstandar.getId())});
                    break;
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            db.close();
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean updateFechaSincroLogin(String data, int idUsert) {

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("fechaSincro", data);

        int p = db.update("login", valores, String.format("id = %1$s", idUsert), null);
        db.close();
        return p > 0;
    }

    public boolean updateFechaSincro(String data, int idUsert) {

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("fechaSincroOffline", data);

        int p = db.update("login", valores, String.format("id = %1$s", idUsert), null);
        db.close();
        return p > 0;

    }

    public boolean insertLoginUser(EntLoginR data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {

            values.put("id", data.getId());
            values.put("cedula", data.getCedula());
            values.put("nombre", data.getNombre());
            values.put("apellido", data.getApellido());
            values.put("user", data.getUser().toUpperCase());
            values.put("estado", data.getEstado());
            values.put("bd", data.getBd());
            values.put("id_distri", data.getId_distri());
            values.put("perfil", data.getPerfil());
            values.put("igv", data.getIgv());
            values.put("intervalo", data.getIntervalo());
            values.put("hora_inicial", data.getHora_inicial());
            values.put("hora_final", data.getHora_final());
            values.put("cantidad_envios", data.getCantidad_envios());
            values.put("fechaSincro", data.getFecha_sincroniza());
            values.put("fechaSincroOffline", "");
            values.put("password", data.getPassword());

            db.insert("login", null, values);
        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;

    }

    public boolean insertIndicadores(EntIndicadores data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("cant_ventas_sim", data.getCant_ventas_sim());
            values.put("cant_ventas_combo", data.getCant_ventas_combo());
            values.put("cant_cumplimiento_sim", data.getCant_cumplimiento_sim());
            values.put("cant_cumplimiento_combo", data.getCant_cumplimiento_combo());
            values.put("cant_quiebre_sim_mes", data.getCant_quiebre_sim_mes());
            values.put("id_vendedor", data.getId_vendedor());
            values.put("id_distri", data.getId_distri());

            db.insert("indicadoresdas", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }


    public boolean insertDetalleIndicadores(EntIndicadores data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getEntPuntoIndicadoList().size(); i++) {
                values.put("idpos", data.getEntPuntoIndicadoList().get(i).getIdpos());
                values.put("tipo_visita", data.getEntPuntoIndicadoList().get(i).getTipo_visita());
                values.put("stock_sim", data.getEntPuntoIndicadoList().get(i).getStock_sim());
                values.put("stock_combo", data.getEntPuntoIndicadoList().get(i).getStock_combo());
                values.put("stock_seguridad_sim", data.getEntPuntoIndicadoList().get(i).getStock_seguridad_sim());
                values.put("stock_seguridad_combo", data.getEntPuntoIndicadoList().get(i).getStock_seguridad_combo());
                values.put("dias_inve_sim", data.getEntPuntoIndicadoList().get(i).getDias_inve_sim());
                values.put("dias_inve_combo", data.getEntPuntoIndicadoList().get(i).getDias_inve_combo());
                values.put("id_vendedor", data.getEntPuntoIndicadoList().get(i).getId_vendedor());
                values.put("id_distri", data.getEntPuntoIndicadoList().get(i).getId_distri());
                values.put("fecha_dia", data.getEntPuntoIndicadoList().get(i).getFecha_dia());
                values.put("fecha_ult", data.getEntPuntoIndicadoList().get(i).getFecha_ult());
                values.put("hora_ult", data.getEntPuntoIndicadoList().get(i).getHora_ult());
                values.put("orden", data.getEntPuntoIndicadoList().get(i).getOrden());

                db.insert("indicadoresdas_detalle", null, values);
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public boolean insertDetalleIndicadoreslist(ListaGrupos data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            for (int i = 0; i < data.getEntPuntoIndicadoList().size(); i++) {
                values.put("idpos", data.getEntPuntoIndicadoList().get(i).getIdpos());
                values.put("tipo_visita", data.getEntPuntoIndicadoList().get(i).getTipo_visita());
                values.put("stock_sim", data.getEntPuntoIndicadoList().get(i).getStock_sim());
                values.put("stock_combo", data.getEntPuntoIndicadoList().get(i).getStock_combo());
                values.put("stock_seguridad_sim", data.getEntPuntoIndicadoList().get(i).getStock_seguridad_sim());
                values.put("stock_seguridad_combo", data.getEntPuntoIndicadoList().get(i).getStock_seguridad_combo());
                values.put("dias_inve_sim", data.getEntPuntoIndicadoList().get(i).getDias_inve_sim());
                values.put("dias_inve_combo", data.getEntPuntoIndicadoList().get(i).getDias_inve_combo());
                values.put("id_vendedor", data.getEntPuntoIndicadoList().get(i).getId_vendedor());
                values.put("id_distri", data.getEntPuntoIndicadoList().get(i).getId_distri());
                values.put("fecha_dia", data.getEntPuntoIndicadoList().get(i).getFecha_dia());
                values.put("fecha_ult", data.getEntPuntoIndicadoList().get(i).getFecha_ult());
                values.put("hora_ult", data.getEntPuntoIndicadoList().get(i).getHora_ult());
                values.put("orden", data.getEntPuntoIndicadoList().get(i).getOrden());

                db.insert("indicadoresdas_detalle", null, values);
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;
    }

    public List<GrupoSims> getGrupoSims() {

        GrupoSims grupoSims= new GrupoSims();
        String sql = "SELECT *  FROM grupo_sims ORDER BY nombre_grupo_sims";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        List<GrupoSims> listaGrupoSims =  new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {

                grupoSims = new  GrupoSims();

                grupoSims.setId(cursor.getInt(0));
                grupoSims.setNombre_grupo(cursor.getString(1));
                grupoSims.setCant_grupo_distri(cursor.getInt(2));
                grupoSims.setCant_grupo_vendedor(cursor.getInt(3));
                listaGrupoSims.add(grupoSims);

            } while (cursor.moveToNext());
        }

        return listaGrupoSims;

    }

    public boolean insert_grupo(ListaGrupos data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        try {

            for (int i = 0; i < data.getGrupoCombos().size(); i++) {
                values2.put("id",data.getGrupoCombos().get(i).getId());
                values2.put("nombre_grupo_combos", data.getGrupoCombos().get(i).getNombre_grupo());
                values2.put("cant_cumplimiento_grupo_combos", data.getGrupoCombos().get(i).getCant_grupo_distri());
                values2.put("cant_ventas_grupo_combos", data.getGrupoCombos().get(i).getCant_grupo_vendedor());

                db.insert("grupo_combos", null, values2);
            }
            for (int i = 0; i < data.getGrupoSims().size(); i++) {
                values1.put("id",data.getGrupoSims().get(i).getId());
                values1.put("nombre_grupo_sims", data.getGrupoSims().get(i).getNombre_grupo());
                values1.put("cant_cumplimiento_grupo_sims", data.getGrupoSims().get(i).getCant_grupo_distri());
                values1.put("cant_ventas_grupo_sims", data.getGrupoSims().get(i).getCant_grupo_vendedor());

                db.insert("grupo_sims", null, values1);
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public List<GrupoCombos> getGrupoCombos() {

        List<GrupoCombos> listaGrupoCombos = new ArrayList<>();

        String sql = "SELECT * FROM grupo_combos ORDER BY nombre_grupo_combos";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);


        if (cursor.moveToFirst()) {
            do {

                GrupoCombos grupoCombos = new GrupoCombos();

                grupoCombos.setId(cursor.getInt(0));
                grupoCombos.setNombre_grupo(cursor.getString(1));
                grupoCombos.setCant_grupo_distri(cursor.getInt(2));
                grupoCombos.setCant_grupo_vendedor(cursor.getInt(3));


                listaGrupoCombos.add(grupoCombos);


            } while (cursor.moveToNext());
        }

        return listaGrupoCombos;

    }

    public EntIndicadores getIndicadores(int id_vendedor, int id_distri) {

        Cursor cursor;
        EntIndicadores indicadores_val = new EntIndicadores();

        String sql = "SELECT cant_ventas_sim, cant_ventas_combo, cant_cumplimiento_sim, cant_cumplimiento_combo, cant_quiebre_sim_mes FROM indicadoresdas WHERE id_vendedor = ? AND id_distri = ?";


        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, new String[] {String.valueOf(id_vendedor), String.valueOf(id_distri)});

        if (cursor.moveToFirst()) {
            indicadores_val.setCant_ventas_sim(cursor.getInt(0));
            indicadores_val.setCant_ventas_combo(cursor.getInt(1));
            indicadores_val.setCant_cumplimiento_sim(cursor.getInt(2));
            indicadores_val.setCant_cumplimiento_combo(cursor.getInt(3));

        }

        return indicadores_val;
    }


    public boolean insertCategoria(Sincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {

            for (int i = 0; i < data.getCategoriasEstandarList().size(); i++) {

                if (data.getCategoriasEstandarList().get(i).getEstado_accion() == 1) {
                    //Insertar.
                    values.put("id", data.getCategoriasEstandarList().get(i).getId());
                    values.put("descripcion", data.getCategoriasEstandarList().get(i).getDescripcion());
                    values.put("estado_accion", data.getCategoriasEstandarList().get(i).getEstado_accion());

                    db.insert("categoria", null, values);
                } else if (data.getCategoriasEstandarList().get(i).getEstado_accion() == 2) {
                    //Actualizar.
                    values.put("id", data.getCategoriasEstandarList().get(i).getId());
                    values.put("descripcion", data.getCategoriasEstandarList().get(i).getDescripcion());
                    values.put("estado_accion", data.getCategoriasEstandarList().get(i).getEstado_accion());

                    int p = db.update("categoria", values, String.format("id = %1$s", data.getCategoriasEstandarList().get(i).getId()), null);
                } else if (data.getCategoriasEstandarList().get(i).getEstado_accion() == 0) {
                    //Eliminar.
                    int a = db.delete("categoria", "id = ? ", new String[]{String.valueOf(data.getCategoriasEstandarList().get(i).getId())});
                }
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertZona(Sincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            //deleteObject("zona");
            for (int i = 0; i < data.getZonaList().size(); i++) {
                values.put("id", data.getZonaList().get(i).getId());
                values.put("descripcion", data.getZonaList().get(i).getDescripcion());
                values.put("id_territorio", data.getZonaList().get(i).getId_territorio());
                values.put("estado", data.getZonaList().get(i).getEstado());

                db.insert("zona", null, values);
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean insertTerritorio(Sincronizar data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            //deleteObject("territorio");
            for (int i = 0; i < data.getTerritoriosList().size(); i++) {

                values.put("id", data.getTerritoriosList().get(i).getId());
                values.put("descripcion", data.getTerritoriosList().get(i).getDescripcion());
                values.put("estado", data.getTerritoriosList().get(i).getEstado());

                db.insert("territorio", null, values);
            }

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    public boolean updatePuntoId(String idOrigen, String idDestino){

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("id_tabla", idDestino);
        valores.put("idpos", idDestino);
        valores.put("accion", "");

        String[] args = new String[]{idOrigen};
        int p = db.update("punto", valores, "id_tabla = ?", args);
        db.close();
        return p > 0;
    }

    public ResponseMarcarPedido getPuntoLocal12(String responseUser) {

        Cursor cursor;
        ResponseMarcarPedido puntoPedido = new ResponseMarcarPedido();


        String sql = "SELECT pos.idpos, pos.nombre_punto, terri.descripcion terri_des, zona.descripcion zona_des, pos.texto_direccion, depa.descripcion depa_des, muni.descripcion provi_des, distri.descripcion distri_des, " +
                "            pos.estado_visita,  pos.codigo_cum, pos.zona, latitud, longitud" +
                "           FROM " +
                "               punto pos INNER JOIN territorio terri ON terri.id = pos.territorio_punto " +
                "               INNER JOIN zona ON zona.id = pos.zona AND zona.id_territorio = terri.id " +
                "               LEFT JOIN departamento depa ON depa.id = pos.depto " +
                "               LEFT JOIN municipios muni ON muni.id_muni = pos.ciudad AND muni.departamento = depa.id " +
                "               LEFT JOIN distritos distri ON distri.id = pos.distrito AND distri.id_depto = depa.id AND distri.id_muni = muni.id_muni " +
                "           WHERE " +
                "               pos.id_tabla = ? ";


        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, new String[] {responseUser});

        if (cursor.moveToFirst()) {
            puntoPedido.setId_pos(cursor.getInt(0));
            puntoPedido.setRazon_social(cursor.getString(1));
            puntoPedido.setTerritorio(cursor.getString(2));
            puntoPedido.setZona(cursor.getString(3));
            puntoPedido.setDireccion(cursor.getString(4));
            puntoPedido.setDepto(cursor.getString(5));
            puntoPedido.setProvincia(cursor.getString(6));
            puntoPedido.setDistrito(cursor.getString(7));
            puntoPedido.setEstado(cursor.getInt(8));
            puntoPedido.setCod_cum(cursor.getString(9));
            puntoPedido.setIdZona(cursor.getInt(10));
            puntoPedido.setLatitud(cursor.getDouble(11));
            puntoPedido.setLongitud(cursor.getDouble(12));
        }

        return puntoPedido;
    }

    public ResponseMarcarPedido getPuntoLocal(String responseUser) {

        Cursor cursor;
        ResponseMarcarPedido puntoPedido = new ResponseMarcarPedido();

        String sql = "SELECT pos.idpos, pos.nombre_punto, terri.descripcion terri_des, zona.descripcion zona_des, pos.texto_direccion, depa.descripcion depa_des, muni.descripcion provi_des, distri.descripcion distri_des, " +
                "     (CASE WHEN ind.tipo_visita = 1 OR ind.tipo_visita = 2 THEN 1 ELSE 0 END) estado_visita, pos.codigo_cum, pos.zona, latitud, longitud " +
                " FROM " +
                "   punto pos INNER JOIN " +
                "   indicadoresdas_detalle AS ind ON (ind.idpos = pos.idpos) INNER JOIN"+
                "   territorio terri ON terri.id = pos.territorio_punto " +
                " INNER JOIN zona ON zona.id = pos.zona AND zona.id_territorio = terri.id " +
                " LEFT JOIN departamento depa ON depa.id = pos.depto " +
                " LEFT JOIN municipios muni ON muni.id_muni = pos.ciudad AND muni.departamento = depa.id " +
                " LEFT JOIN distritos distri ON distri.id = pos.distrito AND distri.id_depto = depa.id AND distri.id_muni = muni.id_muni " +
                "  WHERE " +
                "    pos.idpos = ? OR pos.id_tabla = ? OR pos.codigo_cum = ? ";


        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, new String[] {responseUser, responseUser, responseUser});

        if (cursor.moveToFirst()) {
            puntoPedido.setEstado(cursor.getInt(8));
            puntoPedido.setId_pos(cursor.getInt(0));
            puntoPedido.setRazon_social(cursor.getString(1));
            puntoPedido.setDireccion(cursor.getString(4));
            puntoPedido.setZona(cursor.getString(3));
            puntoPedido.setTerritorio(cursor.getString(2));
            puntoPedido.setDepto(cursor.getString(5));
            puntoPedido.setProvincia(cursor.getString(6));
            puntoPedido.setDistrito(cursor.getString(7));
            puntoPedido.setCod_cum(cursor.getString(9));
            puntoPedido.setIdZona(cursor.getInt(10));
            puntoPedido.setLatitud(cursor.getDouble(11));
            puntoPedido.setLongitud(cursor.getDouble(12));
        }

        return puntoPedido;
    }

    public List<Motivos> getMotivos() {

        List<Motivos> arrayList = new ArrayList<>();

        String sql = "SELECT id, descripcion FROM motivos ORDER BY descripcion";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                arrayList.add(new Motivos(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        return arrayList;

    }

    public EntLoginR getUserLogin() {

        Cursor cursor;
        EntLoginR indicador = new EntLoginR();

        String sql = "SELECT * FROM login";

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            indicador.setId(cursor.getInt(0));
            indicador.setCedula(cursor.getInt(1));
            indicador.setNombre(cursor.getString(2));
            indicador.setApellido(cursor.getString(3));
            indicador.setUser(cursor.getString(4));
            indicador.setEstado(cursor.getInt(5));
            indicador.setBd(cursor.getString(6));
            indicador.setId_distri(cursor.getString(7));
            indicador.setPerfil(cursor.getInt(8));
            indicador.setIgv(cursor.getInt(9));
            indicador.setIntervalo(cursor.getInt(10));
            indicador.setHora_inicial(cursor.getString(11));
            indicador.setHora_final(cursor.getString(12));
            indicador.setCantidad_envios(cursor.getInt(13));
            indicador.setFecha_sincroniza(cursor.getString(14));
            indicador.setFecha_sincroniza_offline(cursor.getString(15));
            indicador.setPassword(cursor.getString(16));

        }

        return indicador;
    }

    public ResponseUser getUserLogin(String responseUser) {

        Cursor cursor;
        ResponseUser indicador = new ResponseUser();

        String[] args = new String[] {responseUser.toUpperCase()};

        String sql = "SELECT * FROM login WHERE user = ?";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, args);

        if (cursor.moveToFirst()) {
            indicador.setId(cursor.getInt(0));
            indicador.setCedula(cursor.getInt(1));
            indicador.setNombre(cursor.getString(2));
            indicador.setApellido(cursor.getString(3));
            indicador.setUser(cursor.getString(4));
            indicador.setEstado(cursor.getInt(5));
            indicador.setBd(cursor.getString(6));
            indicador.setId_distri(cursor.getString(7));
            indicador.setPerfil(cursor.getInt(8));
            indicador.setIgv(cursor.getInt(9));
            indicador.setIntervalo(cursor.getInt(10));
            indicador.setHora_inicial(cursor.getString(11));
            indicador.setHora_final(cursor.getString(12));
            indicador.setCantidad_envios(cursor.getInt(13));
            indicador.setFechaSincro(cursor.getString(14));
            indicador.setPassword(cursor.getString(15));
        }

        return indicador;
    }

    public boolean validateLoginUser(String user, String password ) {
        Cursor cursor;
        boolean indicador = false;
        String[] args = new String[] {user.trim(), password.trim()};
        String sql = "SELECT * FROM login WHERE user =? AND password =?";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, args);
        if (cursor.moveToFirst()) {
            indicador = true;
        }
        return indicador;
    }

    public boolean insertIntro(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("idintro", data);
            db.insert("intro", null, values);
        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;

    }

    public String insertCarritoPedidoCombos(Referencia data) {

        String resultado = "";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (validarProducto(data.getId(), data.getId_punto())) {

            values.put("cantidad_pedida", data.getCantidadPedida());

            int p = db.update("carrito_pedido", values, String.format("id_producto = %1$s", data.getId()), null);
            db.close();

            if (p > 0)
                resultado = "update";
            else
                resultado = "no update";

            return resultado;

        } else {

            try {
                values.put("id_producto", data.getId());
                values.put("pn_pro", data.getPn());
                values.put("stock", data.getStock());
                values.put("producto", data.getProducto());
                values.put("dias_inve", data.getDias_inve());
                values.put("ped_sugerido", data.getPed_sugerido());
                values.put("cantidad_pedida", data.getCantidadPedida());
                values.put("id_usuario", data.getId_usuario());
                values.put("id_punto", data.getId_punto());
                values.put("tipo_producto", data.getTipo_producto());
                values.put("producto_img", data.getUrl_imagen());
                values.put("precio_referencia", data.getPrecio_referencia());
                values.put("precio_publico", data.getPrecio_publico());
                values.put("referencia", data.getReferencia());
                values.put("latitud", data.getLatitud());
                values.put("longitud", data.getLongitud());

                db.insert("carrito_pedido", null, values);

            } catch (SQLiteConstraintException e) {
                Log.d("data", "failure to insert word,", e);
                return resultado = "no inserto";
            } finally {
                db.close();
            }
        }

        return resultado = "inserto";
    }

    public int countReferenceProduct(int idUsuario, int idpos, int idreferen) {

        Cursor cursor;
        int indicador = 0;

        String sql = "SELECT SUM(cantidad_pedida) FROM carrito_pedido WHERE referencia = "+idreferen+" AND  id_usuario = "+idUsuario+ " AND id_punto = "+idpos+" ";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = cursor.getInt(0);
        }
        return indicador;

    }

    public int countSimcardProduct(int idUsuario, int idpos, int idProduct) {

        Cursor cursor;
        int indicador = 0;

        String sql = "SELECT SUM(cantidad_pedida) FROM carrito_pedido WHERE id_producto = "+idProduct+" AND  id_usuario = "+idUsuario+ " AND id_punto = "+idpos+" ";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = cursor.getInt(0);
        }
        return indicador;

    }

    public String insertCarritoPedido(ReferenciasSims data) {

        String resultado = "";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (validarProducto(data.getId(), data.getId_punto())) {

            values.put("cantidad_pedida", data.getCantidadPedida());

            int p = db.update("carrito_pedido", values, String.format("id_producto = %1$s", data.getId()), null);
            db.close();

            if (p > 0)
                resultado = "update";
            else
                resultado = "no update";

            return resultado;

        } else {

            try {
                values.put("id_producto", data.getId());
                values.put("pn_pro", data.getPn());
                values.put("stock", data.getStock());
                values.put("producto", data.getProducto());
                values.put("dias_inve", data.getDias_inve());
                values.put("ped_sugerido", data.getPed_sugerido());
                values.put("cantidad_pedida", data.getCantidadPedida());
                values.put("id_usuario", data.getId_usuario());
                values.put("id_punto", data.getId_punto());
                values.put("tipo_producto", data.getTipo_producto());
                values.put("precio_referencia", data.getPrecio_referencia());
                values.put("precio_publico", data.getPrecio_publico());

                db.insert("carrito_pedido", null, values);

            } catch (SQLiteConstraintException e) {
                Log.d("data", "failure to insert word,", e);
                return resultado = "no inserto";
            } finally {
                db.close();
            }
        }

        return resultado = "inserto";
    }

    public int insertCarritoPaquete(ReferenciasSims data,int position,int tipo_venta) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int serie;
        int cantidadRefe = 0;

        serie = data.getListaPaquete().get(position).getIdPaquete();

        cantidadRefe = contarReferenciasPaquete(data.getId(),serie);//serie es el numero paquete


        try {
            values.put("id_referencia", data.getId());
            values.put("tipo_product", data.getTipo_producto());
            values.put("valor_refe", data.getPrecio_referencia());
            values.put("valor_directo", data.getPrecio_publico());
            values.put("id_punto", data.getId_punto());
            values.put("serie", "0");
            values.put("id_paquete", data.getListaPaquete().get(position).getIdPaquete());
            values.put("tipo_venta", tipo_venta);
            values.put("id_producto", 0);
            values.put("cantidad_soli", cantidadRefe);
            db.insert("carrito_autoventa", null, values);

        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            //no inserto
            return 1;
        } finally {
            db.close();
        }


        return 2;//Inserto correctamente
    }

    public int insertCarritoUnidad(ReferenciasSims data,int position,int tipo_venta) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        List<Serie> listaSerie;
        int count = 0;
        boolean indicador = true;
        listaSerie = data.getListaPaquete().get(position).getListaSerie();

        for (int i=0;i<listaSerie.size();i++){

            String serie = data.getListaPaquete().get(position).getListaSerie().get(i).getSerie();
            int id_pro = data.getListaPaquete().get(position).getListaSerie().get(i).getId_pro();

            Cursor cursor;
            indicador = true;
            String[] args = new String[] {serie};
            String sql = "SELECT * FROM carrito_autoventa WHERE serie = ?";


            cursor = db.rawQuery(sql, args);
            if (cursor.moveToFirst()) {
                indicador = false;
            }

            if(indicador){
                try {

                    if(data.getListaPaquete().get(position).getListaSerie().get(i).getCheck() == 1){

                        values.put("id_referencia", data.getId());
                        values.put("tipo_product", data.getTipo_producto());
                        values.put("valor_refe", data.getPrecio_referencia());
                        values.put("valor_directo", data.getPrecio_publico());
                        values.put("id_punto", data.getId_punto());
                        values.put("serie", data.getListaPaquete().get(position).getListaSerie().get(i).getSerie());
                        values.put("id_paquete", data.getListaPaquete().get(position).getIdPaquete());
                        values.put("tipo_venta", tipo_venta);
                        values.put("id_producto", id_pro);
                        values.put("cantidad_soli", 1);
                        db.insert("carrito_autoventa", null, values);
                        count = count + 1;
                    }
                } catch (SQLiteConstraintException e) {
                    Log.d("Serie", "No inserto el serial:"+data.getListaPaquete().get(position).getListaSerie().get(i).getSerie(), e);
                    return 1;
                }
            }
        }
        if(count > 0){
            return 2;//Inserto correctamente
        }else{
            return 3;//No mandaron nuevos elementos
        }

    }

    public int contarReferenciasPaquete(int id_referencia, int serie){

        Cursor cursor;
        int cantidad = 0;
        String sql = "SELECT count(id_referencia) AS cantidad_soli " +
                "FROM inventario " +
                "WHERE id_referencia = ? AND paquete = ?";

        String[] args = new String[] {String.valueOf(id_referencia),String.valueOf(serie)};

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, args);

        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }
        return cantidad;
    }

    /*public boolean validarCarritoUnidad(String serie) {

        Cursor cursor;
        boolean indicador = true;
        String[] args = new String[] {serie};
        String sql = "SELECT * FROM carrito_autoventa WHERE serie = ?";

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sql, args);
        if (cursor.moveToFirst()) {
            indicador = false;
        }
        return indicador;

    }*/

    public boolean validarProducto(int id_producto, int id_pos) {

        Cursor cursor;
        boolean indicador = false;

        String[] args = new String[] {String.valueOf(id_producto), String.valueOf(id_pos)};

        String sql = "SELECT id_producto FROM carrito_pedido WHERE id_producto = ? AND id_punto = ? LIMIT 1 ";

        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(sql, args);
        if (cursor.moveToFirst()) {
            indicador = true;
        }
        return indicador;

    }

    public boolean deleteCarritoProducto(int id, int id_pos, int id_usuario) {

        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("carrito_pedido", "id_carrito = ? AND id_punto = ? AND id_usuario = ?", new String[]{String.valueOf(id), String.valueOf(id_pos), String.valueOf(id_usuario)});

        db.close();
        return a > 0;

    }

    public boolean deleteCarritoProductoAutoventa(int id, int id_pos) {

        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("carrito_autoventa", "id_auto_carrito = ? AND id_punto = ? ", new String[]{String.valueOf(id), String.valueOf(id_pos)});

        db.close();
        return a > 0;

    }

    public boolean deleteAll(int id_pos, int id_usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("carrito_pedido", "id_punto = ? AND id_usuario = ?", new String[]{String.valueOf(id_pos), String.valueOf(id_usuario)});

        db.close();
        return a > 0;
    }

    public boolean deleteAllCarritoAutoventa(int id_pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("carrito_autoventa", "id_punto = ? ", new String[]{String.valueOf(id_pos)});

        db.close();
        return a > 0;
    }

    public boolean insertTimeServices(TimeService timeService) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {

            values.put("idUser", timeService.getIdUser());
            values.put("traking", timeService.getTraking());
            values.put("timeservice", timeService.getTimeservice());
            values.put("idDistri", timeService.getIdDistri());
            values.put("dataBase", timeService.getDataBase());
            values.put("fechainicial", timeService.getFechainicial());
            values.put("fechafinal", timeService.getFechaFinal());

            db.insert("time_services", null, values);
        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }

        return true;

    }

    public boolean deleteAllServiTime() {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("time_services", null, null);

        db.close();
        return a > 0;
    }

    public boolean deleteAllServiTimeTrace() {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("tracing", null, null);

        db.close();
        return a > 0;
    }

    public TimeService getTimeService () {

        Cursor cursor;
        TimeService indicador = new TimeService();

        String sql = "SELECT idUser, traking, timeservice, idDistri, dataBase, fechainicial, fechafinal FROM time_services";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            indicador = new TimeService();
            indicador.setIdUser(cursor.getInt(0));
            indicador.setTraking(cursor.getInt(1));
            indicador.setTimeservice(cursor.getInt(2));
            indicador.setIdDistri(cursor.getString(3));
            indicador.setDataBase(cursor.getString(4));
            indicador.setFechainicial(cursor.getString(5));
            indicador.setFechaFinal(cursor.getString(6));
        }

        return indicador;
    }

    public boolean insertTracing(Tracing tracing) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("idUser", tracing.getIdUser());
            values.put("imei", tracing.getImei());
            values.put("dateTime", tracing.getDateTime());
            values.put("batteryLavel", tracing.getBatteryLavel());
            values.put("temperatura", tracing.getTemperatura());
            values.put("latitud", tracing.getLatitud());
            values.put("longitud", tracing.getLongitud());
            values.put("idDistri", tracing.getIdDistri());
            values.put("dataBase", tracing.getDataBase());

            db.insert("tracing", null, values);
        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }



    public List<Tracing> getTracingService () {

        List<Tracing> tracingArrayList = new ArrayList<>();

        String sql = "SELECT idUser, imei, dateTime, batteryLavel, temperatura, latitud, longitud, idDistri, dataBase FROM tracing ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Tracing tracing;

        if (cursor.moveToFirst()) {
            do {
                tracing = new Tracing();
                tracing.setIdUser(cursor.getInt(0));
                tracing.setImei(cursor.getString(1));
                tracing.setDateTime(cursor.getString(2));
                tracing.setBatteryLavel(cursor.getInt(3));
                tracing.setTemperatura(cursor.getInt(4));
                tracing.setLatitud(cursor.getDouble(5));
                tracing.setLongitud(cursor.getDouble(6));
                tracing.setIdDistri(cursor.getString(7));
                tracing.setDataBase(cursor.getString(8));

                tracingArrayList.add(tracing);

            } while (cursor.moveToNext());
        }

        return tracingArrayList;

    }

    public List<ReferenciasSims> getCarrito(int id_pos, int id_usuario) {

        List<ReferenciasSims> referenciasSimsList = new ArrayList<>();

        String sql = "SELECT id_carrito, id_producto, pn_pro, stock, producto, dias_inve, ped_sugerido, cantidad_pedida, id_usuario, id_punto, latitud, longitud, tipo_producto, producto_img, precio_referencia, precio_publico FROM carrito_pedido WHERE id_punto = " + id_pos + " AND id_usuario = " + id_usuario + " ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ReferenciasSims referenciasSims;

        if (cursor.moveToFirst()) {
            do {
                referenciasSims = new ReferenciasSims();
                referenciasSims.setId_auto_carrito(cursor.getInt(0));
                referenciasSims.setId(cursor.getInt(1));
                referenciasSims.setPn(cursor.getString(2));
                referenciasSims.setStock(cursor.getInt(3));
                referenciasSims.setProducto(cursor.getString(4));
                referenciasSims.setDias_inve(cursor.getDouble(5));
                referenciasSims.setPed_sugerido(cursor.getString(6));
                referenciasSims.setCantidadPedida(cursor.getInt(7));
                referenciasSims.setId_usuario(cursor.getInt(8));
                referenciasSims.setId_punto(cursor.getInt(9));
                referenciasSims.setTipo_producto(cursor.getInt(12));
                referenciasSims.setUrl_imagen(cursor.getString(13));
                referenciasSims.setPrecio_referencia(cursor.getDouble(14));
                referenciasSims.setPrecio_publico(cursor.getDouble(15));

                referenciasSimsList.add(referenciasSims);

            } while (cursor.moveToNext());
        }

        return referenciasSimsList;

    }

    public List<ReferenciasSims> getCarritoAutoventa(int id_pos) {

        List<ReferenciasSims> referenciasSimsList = new ArrayList<>();

        String sql = "SELECT ca.id_auto_carrito,rs.id,rs.pn,ca.cantidad_soli,rs.producto,ca.id_punto,ca.tipo_product,\n" +
                "ca.valor_refe,ca.serie,ca.id_producto,ca.tipo_venta,ca.id_paquete\n" +
                "FROM carrito_autoventa ca\n" +
                "INNER JOIN referencia_simcard rs ON rs.id = ca.id_referencia\n" +
                "WHERE ca.id_punto = "+ id_pos;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ReferenciasSims referenciasSims;

        if (cursor.moveToFirst()) {
            do {
                referenciasSims = new ReferenciasSims();
                referenciasSims.setId_auto_carrito(cursor.getInt(0));
                referenciasSims.setId(cursor.getInt(1));
                referenciasSims.setPn(cursor.getString(2));
                referenciasSims.setCantidadPedida(cursor.getInt(3));
                referenciasSims.setProducto(cursor.getString(4));
                referenciasSims.setId_punto(cursor.getInt(5));
                referenciasSims.setTipo_producto(cursor.getInt(6));
                referenciasSims.setPrecio_referencia(cursor.getDouble(7));
                referenciasSims.setSerie(cursor.getString(8));
                referenciasSims.setId_producto(cursor.getInt(9));
                referenciasSims.setTipo_venta(cursor.getInt(10));
                referenciasSims.setId_paquete(cursor.getInt(11));

                referenciasSimsList.add(referenciasSims);

            } while (cursor.moveToNext());
        }

        return referenciasSimsList;

    }

    public boolean getIntro() {
        Cursor cursor;
        boolean indicador = false;
        String sql = "SELECT * FROM intro LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = true;
        }
        return indicador;
    }

    public int countReferenciaProducto(int id, int id_punto, int id_usuario) {
        Cursor cursor;
        int indicador = 0;

        String sql = "SELECT SUM(cantidad_pedida) FROM carrito_pedido WHERE id_producto = "+id+" AND  id_usuario = "+id_usuario+ " AND id_punto = "+id_punto+" ";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = cursor.getInt(0);
        }
        return indicador;
    }

    public List<EntRuteroList> getRuteroDia(int vendedor) {

        List<EntRuteroList> indicador = new ArrayList<>();

        String sql = "SELECT pos.idpos, pos.nombre_punto, pos.texto_direccion, " +
                "            (CASE WHEN ind.stock_combo < ind.stock_seguridad_combo THEN (CASE WHEN ind.stock_sim < ind.stock_seguridad_sim THEN (CASE WHEN ind.stock_seguridad_combo < ind.stock_seguridad_sim THEN ind.stock_seguridad_sim ELSE ind.stock_seguridad_combo END) ELSE (CASE WHEN ind.stock_combo < ind.stock_sim THEN ind.stock_sim ELSE ind.stock_combo END) END) ELSE (CASE WHEN ind.stock_sim < ind.stock_seguridad_sim THEN (CASE WHEN ind.stock_combo < ind.stock_sim THEN ind.stock_sim ELSE ind.stock_combo END) ELSE 0 END) END) stock,\n" +
                "            (CASE WHEN ind.dias_inve_combo < ind.dias_inve_sim THEN (CASE WHEN ind.dias_inve_combo = 0 THEN 'D. Inve N/A' ELSE ('D. Inve '||ind.dias_inve_combo) END) ELSE (CASE WHEN ind.dias_inve_sim = 0 THEN 'D. Inve N/A' ELSE ('D. Inve '||ind.dias_inve_sim) END) END) dias_inve,\n" +
                "            (CASE WHEN ind.stock_combo > ind.stock_seguridad_combo THEN 1 ELSE (CASE WHEN ind.stock_sim > ind.stock_seguridad_sim THEN 1 ELSE 0 END) END) quiebre,\n" +
                "            pos.codigo_cum,(CASE WHEN ind.tipo_visita = 1 OR ind.tipo_visita = 2 THEN 1 ELSE 0 END) estado_visita, " +
                "            dis.descripcion nom_distrito, pos.detalle," +
                "            (CASE WHEN ind.fecha_ult = '' THEN 'N/A' ELSE (ind.fecha_ult||' '||ind.hora_ult) END) ultima_visita,ind.stock_sim," +
                "            ind.stock_combo, ind.dias_inve_sim,ind.dias_inve_combo, ind.tipo_visita, pos.telefono, pos.latitud, " +
                "            pos.longitud,ind.stock_seguridad_combo,ind.stock_seguridad_sim \n"+
                "       FROM " +
                "           punto AS pos INNER JOIN indicadoresdas_detalle AS ind ON (ind.idpos = pos.idpos) " +
                "           LEFT JOIN departamento depa ON depa.id = pos.depto" +
                "           LEFT JOIN municipios muni ON muni.id_muni = pos.ciudad AND muni.departamento = depa.id " +
                "           LEFT JOIN distritos AS dis ON (dis.id = pos.distrito AND dis.id_depto = depa.id AND dis.id_muni = muni.id_muni) WHERE ind.id_vendedor = ?  GROUP BY pos.idpos ORDER BY ind.tipo_visita, ind.orden";

        String[] args = new String[] {String.valueOf(vendedor)};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, args);

        EntRuteroList entRuteroList;

        if (cursor.moveToFirst()) {

            do {
                entRuteroList = new EntRuteroList();
                entRuteroList.setIdpos(cursor.getInt(0));
                entRuteroList.setNombre_punto(cursor.getString(1));
                entRuteroList.setTexto_direccion(cursor.getString(2));
                entRuteroList.setStock(cursor.getInt(3));
                entRuteroList.setDias_inve(cursor.getString(4));
                entRuteroList.setQuiebre(cursor.getInt(5));
                entRuteroList.setCodigo_cum(cursor.getString(6));
                entRuteroList.setEstado_visita(cursor.getInt(7));
                entRuteroList.setNom_distrito(cursor.getString(8));
                entRuteroList.setDetalle(cursor.getString(9));
                entRuteroList.setUltima_visita(cursor.getString(10));
                entRuteroList.setStock_sim(cursor.getInt(11));
                entRuteroList.setStock_combo(cursor.getInt(12));
                entRuteroList.setDias_inve_sim(cursor.getInt(13));
                entRuteroList.setDias_inve_combo(cursor.getInt(14));
                entRuteroList.setTipo_visita(cursor.getInt(15));
                entRuteroList.setTelefono(cursor.getString(16));
                entRuteroList.setLatitud(cursor.getDouble(17));
                entRuteroList.setLongitud(cursor.getDouble(18));
                entRuteroList.setStock_seguridad_combo(cursor.getInt(19));
                entRuteroList.setStock_seguridad_sim(cursor.getInt(20));

                indicador.add(entRuteroList);

            } while (cursor.moveToNext());

        }

        return indicador;

    }

    // obtener un array de string cpon los diferentes tipos de noticia
    public ArrayList getTiposNoticia() {

        ArrayList tipoNoticias = new ArrayList<String>();

        String sql = "SELECT tipo FROM ListaNoticias WHERE 1 GROUP BY tipo ORDER BY tipo ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                tipoNoticias.add(cursor.getString(0));

            } while (cursor.moveToNext());

        }
        return tipoNoticias;

    }

    // obtener toas las noticias almacenadas vigentes
    public List<ListaNoticias> getNoticiaList() {

        List<ListaNoticias>  NoticiaArrayList = new ArrayList<>();

        ArrayList<String>  tipoNoticias = getTiposNoticia();

        for (int i=0; i<tipoNoticias.size(); i++) {
            ListaNoticias listaNoticias= new ListaNoticias();
            String[] args = new String[] {String.valueOf(tipoNoticias.get(i))};

            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "SELECT * FROM ListaNoticias WHERE tipo=? GROUP BY id ORDER BY estado ASC";
            Cursor cursor = db.rawQuery(sql, args);

            if (cursor.moveToFirst()) {
                do {
                    EntNoticia entNoticia = new EntNoticia();
                    entNoticia.setId(cursor.getInt(0));
                    entNoticia.setTipo(cursor.getString(1));
                    entNoticia.setTitle(cursor.getString(2));
                    entNoticia.setContain(cursor.getString(3));
                    entNoticia.setUrl(cursor.getString(4));
                    entNoticia.setImage(cursor.getString(5));
                    entNoticia.setDate(cursor.getString(6));
                    entNoticia.setFile_name(cursor.getString(7));
                    entNoticia.setFile_url(cursor.getString(8));
                    entNoticia.setStatus(cursor.getInt(9));
                    entNoticia.setFecha_lectura(cursor.getString(10));
                    entNoticia.setSincronizado(cursor.getInt(11));;
                    entNoticia.setKeys(cursor.getString(12));


                    listaNoticias.add(entNoticia);

                } while (cursor.moveToNext());
            }
            NoticiaArrayList.add(listaNoticias);
        }
        return NoticiaArrayList;

    }

    // obtener todas las noticias almacenadas considerndo la palabra clave ingresada en el SearchView
    public List<ListaNoticias> getNoticiaListQuery(String query) {

        List<ListaNoticias>  NoticiaArrayList = new ArrayList<>();

        ArrayList<String>  tipoNoticias = getTiposNoticia();

        for (int i=0; i<tipoNoticias.size(); i++) {
            ListaNoticias listaNoticias= new ListaNoticias();
            String[] args = new String[] { String.valueOf(tipoNoticias.get(i)),  "%"+query +"%" };

            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "SELECT * FROM ListaNoticias WHERE tipo=? AND keys LIKE ? GROUP BY id ORDER BY estado ASC";
            Cursor cursor = db.rawQuery(sql, args);

            if (cursor.moveToFirst()) {
                do {
                    EntNoticia entNoticia = new EntNoticia();
                    entNoticia.setId(cursor.getInt(0));
                    entNoticia.setTipo(cursor.getString(1));
                    entNoticia.setTitle(cursor.getString(2));
                    entNoticia.setContain(cursor.getString(3));
                    entNoticia.setUrl(cursor.getString(4));
                    entNoticia.setImage(cursor.getString(5));
                    entNoticia.setDate(cursor.getString(6));
                    entNoticia.setFile_name(cursor.getString(7));
                    entNoticia.setFile_url(cursor.getString(8));
                    entNoticia.setStatus(cursor.getInt(9));
                    entNoticia.setFecha_lectura(cursor.getString(10));
                    entNoticia.setSincronizado(cursor.getInt(11));
                    entNoticia.setKeys(cursor.getString(12));


                    listaNoticias.add(entNoticia);

                } while (cursor.moveToNext());
            }
            NoticiaArrayList.add(listaNoticias);
        }
        return NoticiaArrayList;

    }

    //Obtiene una ùnica noticia segùn el id para mostrarla en detalle
    public EntNoticia getNoticia(int id) {
        String sql = "SELECT * FROM ListaNoticias  WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        EntNoticia entNoticia = new EntNoticia();

        if (cursor.moveToFirst()) {
            entNoticia.setId(cursor.getInt(0));
            entNoticia.setTipo(cursor.getString(1));
            entNoticia.setTitle(cursor.getString(2));
            entNoticia.setContain(cursor.getString(3));
            entNoticia.setUrl(cursor.getString(4));
            entNoticia.setImage(cursor.getString(5));
            entNoticia.setDate(cursor.getString(6));
            entNoticia.setFile_name(cursor.getString(7));
            entNoticia.setFile_url(cursor.getString(8));
            entNoticia.setStatus(cursor.getInt(9));
            entNoticia.setFecha_lectura(cursor.getString(10));
            entNoticia.setSincronizado(cursor.getInt(11));
            entNoticia.setKeys(cursor.getString(12));
            return entNoticia;
        }

        return  null;

    }

    // obtiene el npumero total de noticias sin leer para mostrarlas en el navigation drawer
    public int getCantNoticias() {


        String sql = "SELECT count(*) FROM ListaNoticias where estado=0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCount = db.rawQuery(sql, null);
        if(mCount!=null){
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            mCount.close();
            return count;
        }
        else
            return 0;

    }

    // actualiza el estado de una noticia cuando es leìda
    public int updateStatusNoticiabyId(int id) {
        int success=0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            values.put("estado", 1);
            values.put("fecha_lectura",getDatePhoneFecha());
            values.put("sincronizado",1);

            success =db.update("ListaNoticias", values, "id = ?", new String[]{String.valueOf(id)});

        }
        return success;
    }

    // extrae las noticas pendiente por sincronizar con el servidor, solo cargar el id y la fecha, los demàs parametros son nulos
    public ListaNoticias sincronizarEstadoNoticia() {
        String sql = "SELECT id, fecha_lectura FROM ListaNoticias  WHERE sincronizado=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        EntNoticia array = new EntNoticia();
        ListaNoticias result = new ListaNoticias();

        if (cursor.moveToFirst()) {
            do {
                array.setId(cursor.getInt(0));
                array.setFecha_lectura(cursor.getString(1));
                result.add(array);
            } while (cursor.moveToNext());

        }
        return result;
    }

    // extrae las noticas pendiente por sincronizar con el servidor, carga todos los datos
    public ListaNoticias sincronizarNoticia() {

        ListaNoticias listaNoticias = new ListaNoticias();

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM ListaNoticias WHERE sincronizado=1 ";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                EntNoticia entNoticia = new EntNoticia();
                entNoticia.setId(cursor.getInt(0));
                entNoticia.setTipo(cursor.getString(1));
                entNoticia.setTitle(cursor.getString(2));
                entNoticia.setContain(cursor.getString(3));
                entNoticia.setUrl(cursor.getString(4));
                entNoticia.setImage(cursor.getString(5));
                entNoticia.setDate(cursor.getString(6));
                entNoticia.setFile_name(cursor.getString(7));
                entNoticia.setFile_url(cursor.getString(8));
                entNoticia.setStatus(cursor.getInt(9));
                entNoticia.setFecha_lectura(cursor.getString(10));
                entNoticia.setSincronizado(cursor.getInt(11));
                entNoticia.setKeys(cursor.getString(12));


                listaNoticias.add(entNoticia);

            } while (cursor.moveToNext());
        }
        return listaNoticias;
    }

    //cambio el estado sincronizado cuando el servicio responde success
    public void Noticias_sincronizadas(ListaNoticias lista) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sincronizado",0);
        if(db!=null){
            for (int i=0;i<lista.size();i++) {
                db.update("ListaNoticias", values, "id = ?", new String[]{String.valueOf(lista.get(i).getId())});
            }
        }

        db.close();
    }
//elimina la noticia que ya no estàn vigentes
    public  int deleteNoticiabyId(List id) {
        int filasEliminada = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            for (int i=0;i<id.size();i++) {
                filasEliminada =+db.delete("ListaNoticias",  "id = ?", new String[]{String.valueOf(id.get(i))});
            }
        }
        db.close();
        return filasEliminada;

    }

    //inserta noticias desde el servicio independiente
    public boolean insertNoticias(ListaNoticias data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            for (int i = 0; i < data.size(); i++) {
                values.put("id", data.get(i).getId());
                values.put("tipo", data.get(i).getTipo());
                values.put("title", data.get(i).getTitle());
                values.put("contenido", data.get(i).getContain());
                values.put("url", data.get(i).getUrl());
                values.put("url_image", data.get(i).getImage());
                values.put("fecha", data.get(i).getDate());
                values.put("fileName", R.string.url_images+data.get(i).getFile_name());
                values.put("fileUrl", R.string.url_files+data.get(i).getFile_name());
                values.put("estado", data.get(i).getStatus());
                values.put("fecha_lectura", data.get(i).getFecha_lectura());
                values.put("sincronizado", 0);
                values.put("keys", data.get(i).getKeys());

                db.insert("ListaNoticias", null, values);
            }


        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }

    // inserta noticia desde de el servicio de sincronizaciòn offline
    public boolean insertListaNoticias(EntLisSincronizar data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ///Mapeo los valores previamente
        values.put("id", data.getId_noticia());
        values.put("title", data.getTitle());
        values.put("contenido", data.getContenido());
        values.put("url", data.getUrl());
        values.put("url_image", data.getImg());
        values.put("fecha", data.getDate());
        values.put("fileName", R.string.url_images+data.getFile_name());
        values.put("fileUrl", R.string.url_files+data.getFile_name());
        values.put("estado", data.getEstado());
        values.put("fecha_lectura", data.getFecha_lectura());
        values.put("sincronizado", 0);
        values.put("tipo", data.getTipo());
        values.put("keys", data.getKeys());

        try {
            switch (data.getEstado_accion()) {
                case 1:
                    if (getNoticia(data.getId())==null)
                    db.insert("ListaNoticias", null, values);
                    break;
                case 0:
                    if(db!=null){
                        db.delete("ListaNoticias", "id = ?", new String[]{String.valueOf(data.getId_noticia())});
                    }
                    break;
                case 2:
                    if(db!=null){
                        db.delete("ListaNoticias", "id = ?", new String[]{String.valueOf(data.getId_noticia())});
                        db.insert("ListaNoticias", null, values);
                    }
                    break;
                default:

            }
        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;

    }
    //insertar noticia para el repartidor
    public boolean insertNoticiasRepartidor(List<EntNoticia_Repartidor> data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            for (int i = 0; i < data.size(); i++) {
                switch (Integer.parseInt(data.get(i).getEstado_accion())) {
                    case 1:
                        if (getNoticia(data.get(i).getId())==null){
                            values.put("id", data.get(i).getId());
                            values.put("tipo", data.get(i).getTipo());
                            values.put("title", data.get(i).getTitle());
                            values.put("contenido", data.get(i).getContain());
                            values.put("url", data.get(i).getUrl());
                            values.put("url_image", data.get(i).getImage());
                            values.put("fecha", data.get(i).getDate());
                            values.put("fileName", R.string.url_images+data.get(i).getFile_name());
                            values.put("fileUrl", R.string.url_files+data.get(i).getFile_name());
                            values.put("estado", data.get(i).getStatus());
                            values.put("fecha_lectura", data.get(i).getFecha_lectura());
                            values.put("sincronizado", 0);
                            values.put("keys", data.get(i).getKeys());

                            db.insert("ListaNoticias", null, values);}
                        break;
                    case 0:
                        if(db!=null){
                            db.delete("ListaNoticias", "id = ?", new String[]{String.valueOf(data.get(i).getId())});
                        }
                        break;


                }


            }


        } catch (SQLiteConstraintException e) {
            Log.d("data", "failure to insert word,", e);
            return false;
        } finally {
            db.close();
        }
        return true;
    }


}

