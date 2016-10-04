package net.movilbox.dcsperu.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
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

import net.movilbox.dcsperu.Adapter.ExpandableListAdapter;
import net.movilbox.dcsperu.Adapter.ExpandableListDataPump;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.Detalle;
import net.movilbox.dcsperu.Entry.ListPedidosPendientes;
import net.movilbox.dcsperu.Entry.ResponseHome;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.Services.ConnectionDetector;
import net.movilbox.dcsperu.Services.GpsServices;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;

public class ActMarcarVisita extends AppCompatActivity implements View.OnClickListener {

    private ResponseMarcarPedido mDescribable;
    private String accion_paga;

    private TextView text_idpos;
    private TextView text_razon;
    private TextView text_ruta;
    private TextView text_direccion;
    private TextView text_departamento;
    private TextView text_provincia;
    private TextView text_distrito;
    private TextView text_circuito;
    private TextView txt_cod_cum;
    private Button btn_pedidos_pendientes;
    private Button btn_no_venta;
    private ConnectionDetector connectionDetector;

    private SpotsDialog alertDialog;
    private RequestQueue rq;
    public static final String TAG = "MyTag";
    private GpsServices gpsServices;
    private DBHelper mydb;
    private ResponseMarcarPedido responseMarcarPedido_1;
    private ListPedidosPendientes pedidosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_visita);
        connectionDetector = new ConnectionDetector(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        mydb = new DBHelper(this);

        if (connectionDetector.isConnected()) {
            toolbar.setTitle("Marcar Visita");
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            toolbar.setBackgroundColor(Color.RED);
            toolbar.setTitle("Marcar Visita Offline");
        }

        setSupportActionBar(toolbar);

        text_idpos = (TextView) findViewById(R.id.text_idpos);
        text_razon = (TextView) findViewById(R.id.text_razon);
        text_ruta = (TextView) findViewById(R.id.text_ruta);
        text_direccion = (TextView) findViewById(R.id.text_direccion);
        text_departamento = (TextView) findViewById(R.id.text_departamento);
        text_provincia = (TextView) findViewById(R.id.text_provincia);
        text_distrito = (TextView) findViewById(R.id.text_distrito);
        text_circuito = (TextView) findViewById(R.id.text_circuito);
        txt_cod_cum = (TextView) findViewById(R.id.txt_cod_cum);

        alertDialog = new SpotsDialog(this, R.style.Custom);
        gpsServices = new GpsServices(this);

        btn_pedidos_pendientes = (Button) findViewById(R.id.btn_pedidos_pendientes);
        if (btn_pedidos_pendientes != null) {
            btn_pedidos_pendientes.setOnClickListener(this);
        }

        btn_no_venta = (Button) findViewById(R.id.btn_no_venta);
        if (btn_no_venta != null) {
            btn_no_venta.setOnClickListener(this);
        }

        Button btn_tomar_pedido = (Button) findViewById(R.id.btn_tomar_pedido);
        if (btn_tomar_pedido != null) {
            btn_tomar_pedido.setOnClickListener(this);
        }

        Button btn_inventariar = (Button) findViewById(R.id.btn_inventariar);
        if (btn_inventariar != null) {
            btn_inventariar.setOnClickListener(this);
        }

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDescribable = (ResponseMarcarPedido) bundle.getSerializable("value");
            accion_paga = bundle.getString("pages");
        }

        setDataPunto();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accion_paga == null)
                    accion_paga = "";

                switch (accion_paga) {
                    case "creacion_punto":
                        setIndicador_refres(1);
                        startActivity(new Intent(ActMarcarVisita.this, ActMainPeru.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;

                    default:
                        finish();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //if(mydb.getUserLogin().getPerfil() == 1) {
        getMenuInflater().inflate(R.menu.act_main_marca_visita, menu);
        //}

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.act_location) {
            guardarLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void guardarLocation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Pedidos PDV");
        builder.setMessage("¿Desea Actualizar la geolocalización del pdv con su posicion actual?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Bundle bundle = new Bundle();
                Intent intent = new Intent(ActMarcarVisita.this, ActMapActualizar.class);
                bundle.putDouble("latitud", mDescribable.getLatitud());
                bundle.putDouble("longitud", mDescribable.getLongitud());
                bundle.putString("nombrepos", mDescribable.getRazon_social());
                bundle.putString("direccion", mDescribable.getDireccion());
                bundle.putInt("idpos", mDescribable.getId_pos());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Pedidos PDV");
        builder.setMessage("¿Desea Actualizar la geolocalización del pdv con su posicion actual?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                servicioGuardarLocation();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();*/

    }

    private void servicioGuardarLocation() {
        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "actualiza_location_pdv");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        respuestaLocation(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActMarcarVisita.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActMarcarVisita.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActMarcarVisita.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActMarcarVisita.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActMarcarVisita.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idpos", String.valueOf(mDescribable.getId_pos()));
                params.put("latitud", String.valueOf(gpsServices.getLatitude()));
                params.put("longitud", String.valueOf(gpsServices.getLongitude()));
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

    private void respuestaLocation(String response) {
        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {
                Charset.forName("UTF-8").encode(response);
                byte ptext[] = response.getBytes(Charset.forName("ISO-8859-1"));
                String value = new String(ptext, Charset.forName("UTF-8"));

                ResponseMarcarPedido responseMarcarPedido = gson.fromJson(value, ResponseMarcarPedido.class);
                if (responseMarcarPedido.getEstado() == -1) {
                    //Error
                    Toast.makeText(this, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                }else {
                    // ok
                    final ResponseHome responseHome = new ResponseHome();
                    responseHome.setLatitud(gpsServices.getLatitude());
                    responseHome.setLongitud(gpsServices.getLongitude());

                    responseHome.setIdpos(mDescribable.getId_pos());
                    responseHome.setRazon(mDescribable.getRazon_social());
                    responseHome.setDireccion(mDescribable.getDireccion());
                    responseHome.setCircuito(mDescribable.getTerritorio());
                    responseHome.setRuta(mDescribable.getZona());


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle("Pedidos PDV");
                    builder.setMessage(responseMarcarPedido.getMsg()).setPositiveButton("Ver Mapa", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(ActMarcarVisita.this, ActMapsPunto.class);
                            bundle.putSerializable("values",responseHome);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                alertDialog.dismiss();
            } finally {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
        }
    }

    private void setDataPunto() {

        text_idpos.setText(String.format("%1$s", mDescribable.getId_pos()));
        text_razon.setText(String.format("%1$s", mDescribable.getRazon_social()));
        text_ruta.setText(String.format("%1$s", mDescribable.getZona()));
        text_direccion.setText(String.format("%1$s", mDescribable.getDireccion()));
        text_departamento.setText(String.format("%1$s", mDescribable.getDepto()));
        text_provincia.setText(String.format("%1$s", mDescribable.getProvincia()));
        text_distrito.setText(String.format("%1$s", mDescribable.getDistrito()));
        text_circuito.setText(String.format("%1$s", mDescribable.getTerritorio()));
        txt_cod_cum.setText(String.format("%1$s", mDescribable.getCod_cum()));


        if (connectionDetector.isConnected()) {
            //Internet llamar servicio
            getServicesPedidiosPen(mDescribable.getId_pos());

        }


        if (mDescribable.getEstado() == 0)
            btn_no_venta.setVisibility(View.VISIBLE);

    }

    private void getServicesPedidiosPen(final int id_pos) {

        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "consultar_pedidos");
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
                        String error_string = "";

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            error_string = "Error de tiempo de espera";
                        } else if (error instanceof AuthFailureError) {
                            error_string = "Error Servidor";
                        } else if (error instanceof ServerError) {
                            error_string = "Server Error";
                        } else if (error instanceof NetworkError) {
                            error_string = "Error de red";
                        } else if (error instanceof ParseError) {
                            error_string = "Error al serializar los datos";
                        }

                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));
                params.put("idpos", String.valueOf(id_pos));

                return params;

            }
        };

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);

    }

    private void parseJSON(String response) {
        alertDialog.dismiss();
        Gson gson = new Gson();
        pedidosList = gson.fromJson(response, ListPedidosPendientes.class);
        if (pedidosList != null) {
            if (pedidosList.size() > 0)
                btn_pedidos_pendientes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no_venta:

                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, ActNoVenta.class);
                bundle.putSerializable("value", mDescribable);
                intent.putExtras(bundle);
                startActivity(intent);

                break;

            case R.id.btn_pedidos_pendientes:

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_pedidos_pendientes, null);

                ExpandableListView expandableListView = (ExpandableListView) dialoglayout.findViewById(R.id.expandableListView);

                HashMap<String, List<Detalle>> expandableListDetail = ExpandableListDataPump.getData(pedidosList);
                ArrayList<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

                ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("Pedidos PDV");
                builder.setView(dialoglayout).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.show();

                break;

            case R.id.btn_tomar_pedido:

                Bundle bundle2 = new Bundle();
                Intent intent2 = new Intent(this, ActTomarPedido.class);
                bundle2.putSerializable("value", mDescribable);
                bundle2.putString("page", "marcar_visita");
                intent2.putExtras(bundle2);
                startActivity(intent2);

                break;

            case R.id.btn_inventariar:

                if (connectionDetector.isConnected()) {

                    Bundle bundle3 = new Bundle();
                    Intent intent3 = new Intent(this, ActInventariar.class);

                    bundle3.putSerializable("value", mDescribable);
                    intent3.putExtras(bundle3);
                    startActivity(intent3);

                } else {
                    Toast.makeText(this, "Esta opción no esta habilitada offline", Toast.LENGTH_LONG).show();
                }

                break;
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

    @Override
    public void onBackPressed() {

        if (accion_paga == null)
            accion_paga = "";

        switch (accion_paga) {

            case "creacion_punto":
                setIndicador_refres(1);
                startActivity(new Intent(ActMarcarVisita.this, ActMainPeru.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;

            default:
                finish();
                break;
        }
    }

}
