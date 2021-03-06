package net.movilbox.dcsperu.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import net.movilbox.dcsperu.Activity.ActMainPeru;
import net.movilbox.dcsperu.Adapter.AdapterAceptAsigProducto;
import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.AceptarAsignacionProductos;
import net.movilbox.dcsperu.Entry.AceptarAsignacionProductosContenedor;
import net.movilbox.dcsperu.Entry.AceptarComprobante;
import net.movilbox.dcsperu.Entry.ResponseMarcarPedido;
import net.movilbox.dcsperu.R;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static net.movilbox.dcsperu.Entry.EntLoginR.setIndicador_refres;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAceptPedidoVendedor extends BaseVolleyFragment {

    private SpotsDialog alertDialog;
    private ListView listView;
    private AceptarAsignacionProductosContenedor contenedor;
    private List<AceptarAsignacionProductos> aceptarAsignacionProductos;
    private AdapterAceptAsigProducto adapterAceptPedido;
    private DBHelper mydb;

    public FragmentAceptPedidoVendedor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acept_pedido, container, false);

        mydb = new DBHelper(getActivity());

        listView = (ListView) view.findViewById(R.id.listView);

        alertDialog = new SpotsDialog(getActivity(), R.style.Custom);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item2 = menu.add("Guardar");
        item2.setIcon(R.drawable.ic_save_white_24dp); // sets icon
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (contenedor != null) {
                    //Guardar Aceptacion de pedidos validaciones  aceptarComprobante.getAceptarPedidoList().get(i).marcaProducto.equals("")
                    for (int i = 0; i < contenedor.getPedidos().size(); i++) {
                        if (contenedor.getPedidos().get(i).getMarcaProducto().equals("")) {
                            Toast.makeText(getActivity(), "Es necesario que RECHACE o ACEPTE el pedido #" + contenedor.getPedidos().get(i).getId_pedido(), Toast.LENGTH_LONG).show();

                            return true;
                        }
                    }

                    setAceptacionPedido(contenedor);
                } else {
                    Toast.makeText(getActivity(), "No se tiene pedidos para aceptar o para rechazar", Toast.LENGTH_LONG).show();
                }

                return true;
            }

        });

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        getAceptarPedido();
    }

    private void getAceptarPedido() {

        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "pedidos_asignados");
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

                        onConnectionFailed(error_string);
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

                return params;

            }
        };

        addToQueue(jsonRequest);

    }

    private void parseJSONPedido(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {

                contenedor = gson.fromJson(response, AceptarAsignacionProductosContenedor.class);

                adapterAceptPedido = new AdapterAceptAsigProducto(getActivity(), contenedor.getPedidos());
                listView.setAdapter(adapterAceptPedido);

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                alertDialog.dismiss();
            } finally {
                alertDialog.dismiss();
            }
        } else {
            alertDialog.dismiss();
            Toast.makeText(getActivity(), "No se encontraron pedidos para Aceptar", Toast.LENGTH_LONG).show();
        }
    }

    private void setAceptacionPedido(final AceptarAsignacionProductosContenedor contenedor) {

        alertDialog.show();

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "aceptar_cancelar_asignacion");
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        parseJSONSetPedido(response);
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

                        onConnectionFailed(error_string);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                String parJSON = new Gson().toJson(contenedor, AceptarAsignacionProductosContenedor.class);

                params.put("datos", parJSON);

                params.put("iduser", String.valueOf(mydb.getUserLogin().getId()));
                params.put("iddis", mydb.getUserLogin().getId_distri());
                params.put("db", mydb.getUserLogin().getBd());
                params.put("perfil", String.valueOf(mydb.getUserLogin().getPerfil()));

                return params;

            }
        };

        addToQueue(jsonRequest);

    }

    private void parseJSONSetPedido(String response) {

        Gson gson = new Gson();
        if (!response.equals("[]")) {
            try {
                Charset.forName("UTF-8").encode(response);

                byte ptext[] = response.getBytes(Charset.forName("ISO-8859-1"));

                String value = new String(ptext, Charset.forName("UTF-8"));

                ResponseMarcarPedido responseMarcarPedido = gson.fromJson(value, ResponseMarcarPedido.class);

                if (responseMarcarPedido.getEstado() == -1) {

                    Toast.makeText(getActivity(), responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                } else if (responseMarcarPedido.getEstado() == 0) {

                    Toast.makeText(getActivity(), responseMarcarPedido.getMsg(), Toast.LENGTH_LONG).show();
                    //getAceptarPedido();
                    setIndicador_refres(1);
                    Intent intent = new Intent(getActivity(), ActMainPeru.class);
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
