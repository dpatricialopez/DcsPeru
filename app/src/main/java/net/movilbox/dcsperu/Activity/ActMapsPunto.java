package net.movilbox.dcsperu.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ListEntregarPedido;
import net.movilbox.dcsperu.Entry.ResponseHome;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.R;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ActMapsPunto extends FragmentActivity implements OnMapReadyCallback {

    private RequestQueue rq;
    private SpotsDialog alertDialog;
    private DBHelper mydb;
    private ResponseMarcarPedido responseMarcarPedido;
    private ResponseHome responseHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mydb = new DBHelper(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        alertDialog = new SpotsDialog(this, R.style.Custom);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            responseHome = (ResponseHome) bundle.getSerializable("values");
            responseMarcarPedido = mydb.getPuntoLocal(String.valueOf(responseHome.getIdpos()));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Tiene Permisos de ubicacion", Toast.LENGTH_LONG).show();

            return;
        } else {
            LatLng location;
            if(mydb.getUserLogin().getPerfil() == 3) {
                location = new LatLng(responseHome.getLatitud(), responseHome.getLongitud());
            } else {
                location = new LatLng(responseMarcarPedido.getLatitud(), responseMarcarPedido.getLongitud());
            }

            mMap.setMyLocationEnabled(true);
            UiSettings mUiSettings = mMap.getUiSettings();

            // Keep the UI Settings state in sync with the checkboxes.
            mUiSettings.setZoomControlsEnabled(true);
            mUiSettings.setCompassEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setTiltGesturesEnabled(true);
            mUiSettings.setRotateGesturesEnabled(true);
            mMap.isTrafficEnabled();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
            mMap.addMarker(new MarkerOptions().position(location));
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.dialog_markermap, null);

                    TextView txt_id_numero = (TextView) v.findViewById(R.id.txt_id_numero);
                    TextView nombre_punto = (TextView) v.findViewById(R.id.nombre_punto);
                    TextView txt_direccion = (TextView) v.findViewById(R.id.txt_direccion);
                    TextView txt_circuito = (TextView) v.findViewById(R.id.txt_circuito);
                    TextView txt_ruta = (TextView) v.findViewById(R.id.txt_ruta);

                    if(mydb.getUserLogin().getPerfil() == 3) {
                        txt_id_numero.setText(String.valueOf(responseHome.getIdpos()));
                        nombre_punto.setText(String.valueOf(responseHome.getRazon()));
                        txt_direccion.setText(String.valueOf(responseHome.getDireccion()));
                        txt_circuito.setText(String.valueOf(responseHome.getCircuito()));
                        txt_ruta.setText(String.valueOf(responseHome.getRuta()));
                    } else {
                        txt_id_numero.setText(String.valueOf(responseMarcarPedido.getId_pos()));
                        nombre_punto.setText(String.valueOf(responseMarcarPedido.getRazon_social()));
                        txt_direccion.setText(String.valueOf(responseMarcarPedido.getDireccion()));
                        txt_circuito.setText(String.valueOf(responseMarcarPedido.getTerritorio()));
                        txt_ruta.setText(String.valueOf(responseMarcarPedido.getIdZona()));
                    }




                    String texto = "Visitar";
                    if(mydb.getUserLogin().getPerfil() == 3)
                        texto = "Entregar Pedido";

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActMapsPunto.this);
                    builder.setCancelable(false);
                    builder.setTitle("Datos del Punto");
                    builder.setView(v).setPositiveButton(texto, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(mydb.getUserLogin().getPerfil() == 3) {
                                buscarIdPos(responseHome.getIdpos());
                            } else {
                                buscarIdPosVis(responseMarcarPedido.getId_pos());
                            }
                        }
                    }).setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                    // Returning the view containing InfoWindow contents
                    return null;
                }
            });

        }
    }

    private void buscarIdPos(final int idpos) {
        alertDialog.show();
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "datos_entrega");
        rq = Volley.newRequestQueue(this);
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONEntrega(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(ActMapsPunto.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActMapsPunto.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActMapsPunto.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActMapsPunto.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActMapsPunto.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
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
                params.put("idpos", String.valueOf(idpos));

                return params;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);

    }

    private void parseJSONEntrega(String response) {
        Gson gson = new Gson();

        if (!response.equals("[]")) {
            try {

                ListEntregarPedido responseEntregarPedido = gson.fromJson(response, ListEntregarPedido.class);

                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, ActEntregarPedido.class);
                bundle.putSerializable("value", responseEntregarPedido);
                intent.putExtras(bundle);
                startActivity(intent);

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                alertDialog.dismiss();
            } finally {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
            Toast.makeText(this, "No se encontraron datos para mostrar", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarIdPosVis(final int idPos) {
        alertDialog.show();
        rq = Volley.newRequestQueue(this);
        String url = String.format("%1$s%2$s", getString(R.string.url_base), "buscar_punto_visita");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONVisita(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(ActMapsPunto.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ActMapsPunto.this, "Error Servidor", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ActMapsPunto.this, "Server Error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ActMapsPunto.this, "Error de red", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ActMapsPunto.this, "Error al serializar los datos", Toast.LENGTH_LONG).show();
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
                params.put("idpos", String.valueOf(idPos));

                return params;

            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jsonRequest);
    }

    private void parseJSONVisita(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                ResponseMarcarPedido responseMarcarPedido = gson.fromJson(response, ResponseMarcarPedido.class);

                if (responseMarcarPedido.getEstado() == -1) {
                    //No tiene permisos del punto
                    Toast.makeText(this, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else if (responseMarcarPedido.getEstado() == -2) {
                    //El punto no existe
                    Toast.makeText(this, responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else {
                    //Activity Detalle
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(this, ActMarcarVisita.class);
                    bundle.putSerializable("value", responseMarcarPedido);
                    bundle.putString("page", "marcar_rutero");
                    intent.putExtras(bundle);
                    startActivity(intent);
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

}
