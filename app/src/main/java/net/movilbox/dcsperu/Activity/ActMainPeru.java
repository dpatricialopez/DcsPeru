package net.movilbox.dcsperu.Activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.movilbox.dcsperu.Adapter.Base64;
import net.movilbox.dcsperu.BuildConfig;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.EntNoticia;
import net.movilbox.dcsperu.Entry.EntSincronizar;
import net.movilbox.dcsperu.Entry.ListPuntosSincronizar;
import net.movilbox.dcsperu.Entry.ListSincronizarRepartidor;
import net.movilbox.dcsperu.Entry.ListUpdateservice;
import net.movilbox.dcsperu.Entry.ListaNoticias;
import net.movilbox.dcsperu.Entry.NoVisita;
import net.movilbox.dcsperu.Entry.RequestGuardarEditarPunto;
import net.movilbox.dcsperu.Entry.SincronizarPedidos;
import net.movilbox.dcsperu.Entry.listSincronizarNoVenta;
import net.movilbox.dcsperu.Entry.listSincronizarPedidos;
import net.movilbox.dcsperu.Fragment.FragmenEntregarPedido;
import net.movilbox.dcsperu.Fragment.FragmenMarcarvisita;
import net.movilbox.dcsperu.Fragment.FragmentAceptPedido;
import net.movilbox.dcsperu.Fragment.FragmentAceptPedidoVendedor;
import net.movilbox.dcsperu.Fragment.FragmentBajasSupervisor;
import net.movilbox.dcsperu.Fragment.Fragment_connect;
import net.movilbox.dcsperu.Fragment.FragmentHome;
import net.movilbox.dcsperu.Fragment.FragmentHomeRep;
import net.movilbox.dcsperu.Fragment.FragmentHomeSuperPrin;
import net.movilbox.dcsperu.Fragment.FragmentInventarioRepartidor;
import net.movilbox.dcsperu.Fragment.FragmentInventarioVendedor;
import net.movilbox.dcsperu.Fragment.FragmentMisBajas;
import net.movilbox.dcsperu.Fragment.FragmentMisPedidos;
import net.movilbox.dcsperu.Fragment.FragmentNoticia;
import net.movilbox.dcsperu.Fragment.FragmentPedidosSupervisor;
import net.movilbox.dcsperu.Fragment.FragmentPlanificar;
import net.movilbox.dcsperu.Fragment.FragmentReporteAprobacionPdv;
import net.movilbox.dcsperu.Fragment.FragmentReportePedidosRepartidor;
import net.movilbox.dcsperu.Fragment.FragmentRuteroVendedor;
import net.movilbox.dcsperu.Fragment.FragmentSolProducto;
import net.movilbox.dcsperu.Fragment.FragmenteAproPdv;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;
import net.movilbox.dcsperu.Services.MonitoringService;
import net.movilbox.dcsperu.Services.SetTracingServiceWeb;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.getIndicador_refres;
import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;

public class ActMainPeru extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private FragmentManager fragmentManager;
    private DBHelper mydb;
    private int editaPunto;
    private String accion = "Guardar";
    private SpotsDialog alertDialog;
    private RequestQueue rq;
    public static final String TAG = "MyTag";
    private String responseGlobal;
    private ConnectionDetector connectionDetector;
    private Handler handler = new Handler();
    public ProgressDialog progressDialog;
    private int progressStatus = 0;
    private String mensaje = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);

        mydb = new DBHelper(this);
        alertDialog = new SpotsDialog(this, R.style.Custom);
        alertDialog.setCancelable(false);

        connectionDetector = new ConnectionDetector(this);

        setContentView(R.layout.activity_main_peru);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Inicio");
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(toolbar);

        //Colocar en una Estacion de radio..
        startService(new Intent(this, MonitoringService.class));

        startService(new Intent(this, SetTracingServiceWeb.class));

        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 1 supervisor
        // 2 vendedor
        // 3 repartidor

        if(mydb.getUserLogin() != null) {
            if (mydb.getUserLogin().getPerfil() == 2) {

                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.drawer_vendedor);

            } else if (mydb.getUserLogin().getPerfil() == 3) {

                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.drawer_repartidor);

            } else if (mydb.getUserLogin().getPerfil() == 1) {

                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.drawer_supervisor);

            }


        } else {

            Intent intent = new Intent(this, ActLoginUser.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        String versionName = BuildConfig.VERSION_NAME;

        // Accion Para la Edicion del
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            editaPunto = bundle.getInt("edit_punto");
            int accionNav = bundle.getInt("accion");

            if (accionNav == 1) {
                accion = "Editar";
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_gestion_pdv));
            }

            if (accionNav == 2) {
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_marcar_visita));
            }

            if (accionNav == 3) {
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_entregar_pedido));
            }

        } else {
            if(mydb.getUserLogin().getPerfil() == 2) {
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home));
            } else if(mydb.getUserLogin().getPerfil() == 3){
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home_repartidor));
            } else if(mydb.getUserLogin().getPerfil() == 1){
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_home_super));
            }
        }

        View header = navigationView.getHeaderView(0);

        TextView txt_sub = (TextView) header.findViewById(R.id.txt_sub);
        TextView txtVersion = (TextView) header.findViewById(R.id.txtVersion);

        if (mydb.getUserLogin().getNombre() != null) {

            byte ptext[] = mydb.getUserLogin().getNombre().getBytes(Charset.forName("ISO-8859-1"));
            String value = new String(ptext, Charset.forName("UTF-8"));

            byte ptext1[] = mydb.getUserLogin().getApellido().getBytes(Charset.forName("ISO-8859-1"));
            String value1 = new String(ptext1, Charset.forName("UTF-8"));

            txt_sub.setText(String.format("%1$s %2$s", value, value1));
            txtVersion.setText(String.format("Versión: %1$s", versionName));

        } else {
            Intent intent12 = new Intent(this, ActLoginUser.class);
            startActivity(intent12);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        String response="[{'id':'89','tipo':'Noticia','title':'noticia1','contenido':'Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos.', 'url':'', 'url_image':'http://www.w3schools.com/html/pic_mountain.jpg', 'fecha':'Oct 1','file_name':'file1.doc','file_url':'https://sites.google.com/site/cursoscei/cursos/excel/docsexcel/AcumuladosporMeses.xls?attredirects=0&d=1','estado':'0','fecha_lectura':'Oct 6','sincronizado':'1','vigencia':'1', 'keys':'no'}, {'id':'9','tipo':'Promoción','title':'noticia2','contenido':'', 'url':'http://www.w3schools.com/html/pic_mountain.jpg', 'url_image':'http://www.w3schools.com/html/pic_mountain.jpg', 'fecha':'Oct 2','status':'1','file_name':'file1.doc','file_url':'','estado':'0','fecha_lectura':'Oct 9','sincronizado':'1','vigencia':'1','keys':'nuevo'},{'id':'2','tipo':'Noticia','title':'noticia3','contenido':'', 'url':'', 'url_image':'', 'fecha':'Oct 2','status':'1','file_name':'file1.doc','file_url':'','estado':'0','fecha_lectura':'Oct 9','sincronizado':'1','vigencia':'1','keys':'descuento vida nuevo'}]";

        JSONnews(response);
    }

    @Override
    protected void onStart() {
        super.onStart();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView view = (TextView) navigationView.getMenu().findItem( R.id.nav_noticias ).getActionView().findViewById( R.id.counter_txt );

        //se setea el la notificacion; se debe poner una condicion para mostrar o no

        if(mydb.getCantNoticias()==0){
            view.setVisibility(View.GONE);
        }
        else{
            view.setText(String.valueOf(mydb.getCantNoticias()));
            view.setVisibility(View.VISIBLE);
        }
    }

    public void JSONnews(String response){
        Gson gson = new Gson();
        ListaNoticias listaNoticia =gson.fromJson(response, ListaNoticias.class);
        ArrayList<Integer> eliminar=new ArrayList<>();
        ArrayList<Integer> actualizar=new ArrayList<>();
        ListaNoticias insertar = new ListaNoticias();

        for (int i = 0; i < listaNoticia.size(); i++) {
            if (mydb.getNoticia(listaNoticia.get(i).getId())!=null){ //existe
                if (listaNoticia.get(i).getVigencia() == 0) {
                    eliminar.add(listaNoticia.get(i).getId());
                }
                else {//Actualizar
                    actualizar.add(listaNoticia.get(i).getId());
                }
            }
            else{ //Nueva
                insertar.add(listaNoticia.get(i));
            }
        }

        mydb.insertNoticias(insertar);

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Pulse otra vez para salir", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Logs para verificar que los servicios si estèn corriendo
        Log.e("serviciotracing",String.valueOf(isMyServiceRunning(SetTracingServiceWeb.class)));
        Log.e("serviciomonitoring",String.valueOf(isMyServiceRunning(MonitoringService.class)));

        int id = item.getItemId();

        if (id == R.id.nav_cerrar_sesion) {
            Intent intent = new Intent(this, ActLoginUser.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            sincronizarData();
            String title_toolbar = "";
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            if (connectionDetector.isConnected()) {
                if (mydb.getUserLogin().getPerfil() == 2) {
                    //Vendedor
                    offLineDataVendedor();
                } else if (mydb.getUserLogin().getPerfil() == 3) {
                    //Repartidor
                    offLineDataRepartidor();
                } else if (mydb.getUserLogin().getPerfil() == 1) {
                    //Supervisor
                    offLineDataVendedor();
                }

            } else {
                title_toolbar = "Offline";
                toolbar.setBackgroundColor(Color.RED);
            }


            Class fragmentClass = null;

            if (id == R.id.nav_home) {

                toolbar.setTitle("Inicio Vendedor" + title_toolbar);
                editaPunto = 0;
                accion = "Guardar";
                fragmentClass = FragmentHome.class;

            } else if (id == R.id.nav_home_repartidor) {

                toolbar.setTitle("Inicio Repartidor " + title_toolbar);
                editaPunto = 0;
                accion = "Guardar";
                fragmentClass = FragmentHomeRep.class;

            } else if (id == R.id.nav_home_super) {

                toolbar.setTitle("Inicio Supervisor " + title_toolbar);
                editaPunto = 0;
                accion = "Guardar";
                fragmentClass = FragmentHomeSuperPrin.class;

            } else if (id == R.id.nav_marcar_visita) {

                toolbar.setTitle("Marcar Visita " + title_toolbar);
                editaPunto = 0;
                accion = "Guardar";
                fragmentClass = FragmenMarcarvisita.class;

            } else if (id == R.id.nav_planificar_punto) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Planificar Visita");
                    fragmentClass = FragmentPlanificar.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_acpetar_pedido) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Aceptar Pedido");
                    fragmentClass = FragmentAceptPedido.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            }else if (id == R.id.nav_acpetar_pedido_vendedor) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Aceptar Pedido");
                    fragmentClass = FragmentAceptPedidoVendedor.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_gestion_pdv) {

                toolbar.setTitle("Gestión PDVS " + title_toolbar);
                if (connectionDetector.isConnected()) {
                    cargarVistaPunto();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("Offline");
                    builder.setMessage("¿ Estás seguro crear un punto Offline ?");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            cargarVistaPunto();
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }

            } else if (id == R.id.nav_rutero_vendedor) {
                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Mi Rutero");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentRuteroVendedor.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_pedido_vendedor) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Mis Pedidos");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentMisPedidos.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_pedido_super) {
                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Reporte de Pedidos");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentPedidosSupervisor.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_baja_vendedor) {
                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Mis Bajas");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentMisBajas.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_pdv_aprp) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Aprobación PDVS");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmenteAproPdv.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_sol_producto) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Solicitar Producto");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentSolProducto.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_aprobaciones_super) {
                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Reporte Aprobación PDVS");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentReporteAprobacionPdv.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_entregar_pedido) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Entregar Pedido");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmenEntregarPedido.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            } else if (id == R.id.nav_bajas_super) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Reporte de Bajas");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentBajasSupervisor.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }
            } else if (id == R.id.nav_noticias) {
                fragmentClass = FragmentNoticia.class;
                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Noticias");
                } else {
                    toolbar.setTitle("Noticias Offline");
                }

            } else if (id == R.id.nav_disconnect) {
                fragmentClass =Fragment_connect.class;
                toolbar.setTitle("Conexión");
             }
                else if (id == R.id.nav_inventario) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Mi Inventario");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentInventarioRepartidor.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }

            }else if (id == R.id.nav_inventario_vendedor) {

                toolbar.setTitle("Mi Inventario");
                editaPunto = 0;
                accion = "Guardar";
                fragmentClass = FragmentInventarioVendedor.class;

            } else if (id == R.id.nav_mis_pedidos_rep) {

                if (connectionDetector.isConnected()) {
                    toolbar.setTitle("Mis Pedidos");
                    editaPunto = 0;
                    accion = "Guardar";
                    fragmentClass = FragmentReportePedidosRepartidor.class;
                } else {
                    Toast.makeText(this, "Esta opción solo es permitida si tiene internet", Toast.LENGTH_LONG).show();
                }
            }

            try {

                Fragment fragment = (Fragment) fragmentClass.newInstance();

                fragmentManager.beginTransaction().replace(R.id.contentPanel, fragment).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;

    }

    public void sincronizarData() {
        if (connectionDetector.isConnected()) {
            List<RequestGuardarEditarPunto> puntoList = mydb.getPuntosSincronizar("Sincronizar");
            if (puntoList.size() > 0) {
                setPuntoSincronizar(puntoList);
            } else {
                //Toast.makeText(this, "No tiene Puntos para sincronizar!", Toast.LENGTH_LONG).show();
                List<SincronizarPedidos> sincronizarPedidosList = mydb.sincronizarPedido();
                if (sincronizarPedidosList.size() > 0) {
                    setPedidosSincroinzar();
                } else {
                    //Toast.makeText(getActivity(), "No tengo pedidos para sincronizar!", Toast.LENGTH_LONG).show();
                    List<NoVisita> noVisitaList = mydb.sincronizarNoVisita();
                    if (noVisitaList.size() > 0) {
                        setSincroinizarNoVisita();
                    }
                    else {
                        ListaNoticias noticiaList = mydb.sincronizarNoticia();
                        if (noticiaList.size() > 0) {
                            setSincroinizarNoticia();
                        }
                    }
                }
            }
        }
    }

// Funciòn para verificar que los servicios si estàn corriendo
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void setPuntoSincronizar(final List<RequestGuardarEditarPunto> puntoList) {

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "guardar_punto");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                String parJSON = new Gson().toJson(puntoList, ListPuntosSincronizar.class);

                params.put("datos", parJSON);

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));

                params.put("accion", "Sincronizar");

                return params;

            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);

    }

    private void parseJSON(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {

            try {

                final ListUpdateservice sincronizar = gson.fromJson(response, ListUpdateservice.class);

                for (int i = 0; i < sincronizar.size(); i++) {

                    if (sincronizar.get(i).getEstado().equals("-1")) {
                        // Error
                        if (mydb.deletePuntoError(sincronizar.get(i).getId_movil())) {
                            Toast.makeText(this, sincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "No se pudo eliminar el punto con error", Toast.LENGTH_LONG).show();
                        }

                    } else if (sincronizar.get(i).getEstado().equals("0")) {
                        // Se guardo exitosamente
                        if (mydb.updatePuntoId(sincronizar.get(i).getId_movil(), String.valueOf(sincronizar.get(i).getId_db()))) {
                            //Se buscan los pedidos relacionados con el punto y se actualizan.
                            // update id cabezapedidos...
                            mydb.uptadeCabezaPedidoLocal(sincronizar.get(i).getIdreferecia(), String.valueOf(sincronizar.get(i).getId_db()));

                            Toast.makeText(this, "Puntos sincronizados", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(this, "Problemas al actualizar el id del punto", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                List<SincronizarPedidos> sincronizarPedidosList = mydb.sincronizarPedido();
                if (sincronizarPedidosList.size() > 0) {
                    setPedidosSincroinzar();
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setPedidosSincroinzar() {

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "guardar_pedido_offline");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONPedido(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                List<SincronizarPedidos> sincronizarPedidosList = mydb.sincronizarPedido();

                String parJSON = new Gson().toJson(sincronizarPedidosList, listSincronizarPedidos.class);

                params.put("datos", parJSON);
                params.put("bd", mydb.getUserLogin().getBd());
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));

                return params;

            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);

    }

    private void parseJSONPedido(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {

            final ListUpdateservice sincronizar = gson.fromJson(response, ListUpdateservice.class);

            for (int i = 0; i < sincronizar.size(); i++) {
                if (sincronizar.get(i).getEstado().equals("-1")) {
                    Toast.makeText(this, sincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                    mydb.deleteObject("cabeza_pedido");
                    mydb.deleteObject("detalle_pedido");
                } else if (sincronizar.get(i).getEstado().equals("0")) {
                    if (mydb.deleteObject("cabeza_pedido"))
                        mydb.deleteObject("detalle_pedido");
                    Toast.makeText(this, sincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }

        //Llamar no visitas...
        List<NoVisita> noVisitaList = mydb.sincronizarNoVisita();
        if (noVisitaList.size() > 0) {
            setSincroinizarNoVisita();
        }

    }

    private void setSincroinizarNoVisita() {

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "guardar_no_venta_offline");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONNoVenta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                List<NoVisita> noVisitaList = mydb.sincronizarNoVisita();

                String parJSON = new Gson().toJson(noVisitaList, listSincronizarNoVenta.class);

                params.put("datos", parJSON);

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));

                return params;

            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJSONNoVenta(String response) {
        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                Charset.forName("UTF-8").encode(response);

                byte ptext[] = response.getBytes(Charset.forName("ISO-8859-1"));

                String value = new String(ptext, Charset.forName("UTF-8"));

                ListUpdateservice noVentaSincronizar = gson.fromJson(value, ListUpdateservice.class);

                for (int i = 0; i < noVentaSincronizar.size(); i++) {

                    if (noVentaSincronizar.get(i).getEstado().equals("-1")) {
                        Toast.makeText(this, noVentaSincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                    } else if (noVentaSincronizar.get(i).getEstado().equals("0")) {
                        if (mydb.deleteObject("no_visita"))
                            Toast.makeText(this, noVentaSincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        //Llamar noticias
        ListaNoticias noticiaList = mydb.sincronizarNoticia();
        if (noticiaList.size() > 0) {
            setSincroinizarNoticia();
        }
    }

    private void offLineDataRepartidor() {

        if (alertDialog != null)
            alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "servicio_offline");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSONRepartidor(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActMainPeru.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActMainPeru.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActMainPeru.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActMainPeru.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActMainPeru.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }
                        if (alertDialog != null)
                            alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("fecha_sincroniza_offline", mydb.getUserLogin().getFecha_sincroniza_offline() == "" ? "": mydb.getUserLogin().getFecha_sincroniza_offline());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));
                params.put("db", mydb.getUserLogin().getBd());

                return params;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJSONRepartidor(final String response) {

        new Thread(new Runnable() {
            public void run() {

                responseGlobal = response;
                Gson gson = new Gson();
                byte[] gzipBuff = new byte[0];
                try {
                    gzipBuff = Base64.decode(responseGlobal);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                responseGlobal = ZipUtils.unzipString(new String(gzipBuff));

                ListSincronizarRepartidor sincronizar = gson.fromJson(responseGlobal, ListSincronizarRepartidor.class);

                mydb.deleteObject("pedido_entrega");
                mydb.deleteObject("pedido_repartidor");
                mydb.deleteObject("pedidos_grupo");
                mydb.deleteObject("deta_pedido");
                mydb.insertEntregaPedidos(sincronizar);

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    }
                });

            }
        }).start();
    }

    private void offLineDataVendedor() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sincronización de Información");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setIndeterminate(false);

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "servicio_offline");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        parseJSONVendedor(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActMainPeru.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActMainPeru.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActMainPeru.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActMainPeru.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActMainPeru.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }

                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());

                params.put("fecha_sincroniza_offline", mydb.getUserLogin().getFecha_sincroniza_offline() == "" ? "": mydb.getUserLogin().getFecha_sincroniza_offline());

                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));
                params.put("db", mydb.getUserLogin().getBd());

                return params;
            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJSONVendedor(String response) {

        Gson gson = new Gson();

        final EntSincronizar sincronizar = gson.fromJson(response, EntSincronizar.class);

        if (sincronizar.getEntLisSincronizars().size() > 0) {

            progressDialog.setMax(sincronizar.getEntLisSincronizars().size());
            mensaje = "";
            progressStatus = 0;

            if (sincronizar.getTerritorios() > 0) { mydb.deleteObject("territorio"); }

            if(sincronizar.getZonas() > 0) { mydb.deleteObject("zona"); }

            if(sincronizar.getPuntos() > 0) { mydb.deleteObjectPuntos("punto"); }

            if(sincronizar.getRefes_sims() > 0) { mydb.deleteObject("referencia_simcard"); }

            if(sincronizar.getRefes_combo() > 0) { mydb.deleteObject("referencia_combo"); }

            if(sincronizar.getLista_precios() > 0) { mydb.deleteObject("lista_precios"); }

            if(sincronizar.getMotivos() > 0) { mydb.deleteObject("motivos"); }

            //if(sincronizar.getInventario() > 0) { mydb.deleteObject("inventario"); }

            new Thread(new Runnable() {
                public void run() {

                    while (progressStatus < sincronizar.getEntLisSincronizars().size()) {

                        switch (sincronizar.getEntLisSincronizars().get(progressStatus).getTipoTabla()) {
                            case 1:
                                mensaje = "Sincronizando Territorios.";
                                mydb.insertTerritorio(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 2:
                                mensaje = "Sincronizando Zonas.";
                                mydb.insertZona(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 3:
                                mensaje = "Sincronizando Puntos.";
                                mydb.insertPunto(sincronizar.getEntLisSincronizars().get(progressStatus), 0);
                                break;
                            case 4:
                                mensaje = "Sincronizando Referencias SIM";
                                mydb.insertReferenciaSim(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 5:
                                mensaje = "Sincronizando Referencias COMBOS";
                                mydb.insertReferenciaCombos(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 6:
                                mensaje = "Sincronizando Listas de precios";
                                mydb.insertLisPrecios(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 7:
                                mensaje = "Sincronizando Listas de Motivos no venta";
                                mydb.insertListMotivos(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 8:
                                mensaje = "Sincronizando Listas de inventario";
                                mydb.insertLisInventario(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                            case 9:
                                mensaje = "Sincronizando noticias y promociones";
                                mydb.insertListaNoticias(sincronizar.getEntLisSincronizars().get(progressStatus));
                                break;
                        }

                        progressStatus++;

                        // Update the progress bar
                        handler.post(new Runnable() {
                            public void run() {
                                progressDialog.setMessage(mensaje);
                                progressDialog.setProgress(progressStatus);
                            }
                        });
                    }

                    if (getIndicador_refres() == 1) {
                        mydb.updateFechaSincro(sincronizar.getFecha_sincroniza(), mydb.getUserLogin().getId());
                        setIndicador_refres(0);
                        progressDialog.dismiss();
                        if (mydb.getUserLogin().getPerfil() == 2) {
                            try {

                                Class fragmentClass = FragmentHome.class;
                                Fragment fragment = (Fragment) fragmentClass.newInstance();
                                fragmentManager.beginTransaction().replace(R.id.contentPanel, fragment).commit();

                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mydb.updateFechaSincro(sincronizar.getFecha_sincroniza(), mydb.getUserLogin().getId());
                        progressDialog.dismiss();
                    }
                }

            }).start();

        } else {
            mydb.updateFechaSincro(sincronizar.getFecha_sincroniza(), mydb.getUserLogin().getId());
            progressDialog.dismiss();
        }

    }

    private void cargarVistaPunto() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, ActCrearPdvuno.class);
        bundle.putString("accion", accion);
        bundle.putInt("idpos", editaPunto);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setSincroinizarNoticia() {

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "actualizar_noticia");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONNew(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                List<SincronizarPedidos> sincronizarPedidosList = mydb.sincronizarPedido();
                String parJSON = new Gson().toJson(sincronizarPedidosList, listSincronizarPedidos.class);
                params.put("datos", parJSON);
                params.put("bd", mydb.getUserLogin().getBd());
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));

                return params;

            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);

    }

    private void parseJSONNew(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {

            final ListUpdateservice sincronizar = gson.fromJson(response, ListUpdateservice.class);

            for (int i = 0; i < sincronizar.size(); i++) {
                if (sincronizar.get(i).getEstado().equals("-1")) {
                    Toast.makeText(this, sincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                    mydb.deleteObject("cabeza_pedido");
                    mydb.deleteObject("detalle_pedido");
                } else if (sincronizar.get(i).getEstado().equals("0")) {
                    if (mydb.deleteObject("cabeza_pedido"))
                        mydb.deleteObject("detalle_pedido");
                    Toast.makeText(this, sincronizar.get(i).getMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }

        //Llamar no visitas...
        List<NoVisita> noVisitaList = mydb.sincronizarNoVisita();
        if (noVisitaList.size() > 0) {
            setSincroinizarNoVisita();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

}
